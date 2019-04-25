package org.octabyte.zeem.Queues;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import org.octabyte.zeem.API.Helper.Datastore;
import org.octabyte.zeem.API.Helper.UserHelper;
import org.octabyte.zeem.Datastore.*;
import org.octabyte.zeem.Helper.DataType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.octabyte.zeem.Helper.OfyService.ofy;

@SuppressWarnings("serial")
@WebServlet(name = "CreateFeed", description = "Create Feeds for Post",
        urlPatterns = "/queue/creating_feed")
public class CreateFeed extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Getting value from URL (userId, postId)
        Long userId = Long.valueOf(req.getParameter("userId"));
        Long postId = Long.valueOf(req.getParameter("postId"));
        String listId = req.getParameter("listId");
        Boolean isTagged = Boolean.valueOf(req.getParameter("postTag"));
        Boolean isAnonymous = Boolean.valueOf(req.getParameter("isAnonymous"));
        // Get Boolean value from String
        Boolean isPublic = !req.getParameter("isPublic").equals(String.valueOf(DataType.Mode.PRIVATE));

        // Create variable to hold Post safe key
        Key postKey;

        // Check queue is started by tag or not
        Boolean startedByTag = false;
        // Owner id who create this post is set when queue start from tag
        Long ownerId = null;
        try {
            startedByTag = Boolean.valueOf(req.getParameter("queueStartedByTag"));
            if (startedByTag){
                ownerId = Long.valueOf(req.getParameter("ownerId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // If queue is started from tag then don't enter this post into my feed
        // Post is already added into my feed when first time run this queue

        if (!startedByTag) {

            // Add Post into my own Feed
            // Create Post key to add it into Feed
            postKey = Key.create(Key.create(User.class, userId), Post.class, postId);

            if (isPublic) { // Post is Public add this into Public Feed

                //generatePrivateFeed(userId, postKey);
                generatePublicFeed(userId, postKey);

            } else { // Post is private only add it into Private Feed

                generatePrivateFeed(userId, postKey);

            }

        }else{ // Post is started from tagged

            // Get post safe key from request
            postKey = Key.create(req.getParameter("postSafeKey"));

        }

        // Check listId is Null or not
        if (listId == null) // No list is attached with this post
            friendFollowerFeed(userId, postId, isPublic, startedByTag, ownerId, postKey); // Inform all Friends and Follower about this Post via Feed
        else // List is attached, This post is only for specific users
            informListMembers(userId, listId, postId, isAnonymous); // Inform only list members via Feed and also send notification to them

        //  Check this post is tagged to someone or not
        if(isTagged) // Post is tagged someone
            tagThisPost(userId, postId, isPublic, isAnonymous);  // Tag this post to that user (use Queue)

    }

    /**
     * Create Feed for Friends and for Followers if this post is Public then add new
     * Queue for follower feed.
     * @param userId        Create feed for that user
     * @param postId        Id of the Post which is entered in Feed
     * @param isPublic      Post is public or not when it's public also inform Follower
     * @param startedByTag  Queue is started by tag or not
     * @param ownerId       Id of user who created this post
     */
    private void friendFollowerFeed(Long userId, Long postId, Boolean isPublic, Boolean startedByTag, Long ownerId, Key postKey){

        // Get list of friends
        List<Friend> friendList = UserHelper.getFriendList(userId);

        // Get only friend ids from friend list
        Long[] friendIds = friendList.stream().map(Friend::getFriendId).toArray(Long[]::new);

        // Check queue is started by tag or not if started by tag then remove users who are tagged with this post and also remove owner

        if (startedByTag) {

            // Get tagged user from this post
            List<PostTag> postTags = ofy().load().type(PostTag.class).ancestor(Key.create(Post.class, postId)).list();

            // Get only user ids from this object
            Long[] taggedUserIds = postTags.stream().map(PostTag::getUserId).toArray(Long[]::new);

            // Remove these users from friend ids
            List<Long> l1 = new ArrayList<>(Arrays.asList(friendIds));
            List<Long> l2 = new ArrayList<>(Arrays.asList(taggedUserIds));
            l1.removeAll(l2);

            // Now get the friends of owner
            List<Friend> ownerFriends = UserHelper.getFriendList(ownerId);

            // Get only friend ids from owner's friend list
            Long[] ownerFriendIds = ownerFriends.stream().map(Friend::getFriendId).toArray(Long[]::new);

            // Convert owner friend ids into list
            List<Long> ownerFriendList = new ArrayList<>(Arrays.asList(ownerFriendIds));

            // remove also these friends from list
            l1.removeAll(ownerFriendList);

            // Also remove owner from friend ids
            l1.remove(ownerId);

            friendIds = l1.toArray(new Long[0]);

        } //

        // Get list of friends and loop them
        for ( Long friendId : friendIds ) {

            // Check it's a public post or private
            if (isPublic) { // It's a Public Post

                // Save this post in public feed and in private feed Only for friends
                //generatePrivateFeed(friend.getFriendId(), postKey); // Private Feed
                generatePublicFeed(friendId,postKey); // Public Feed

            }else { // It's a private post

                // Save this post in private feed
                generatePrivateFeed(friendId, postKey);

            }

        } // end for loop

        // Check if post is public then also inform all followers via Feed
        if (isPublic) { // Post is public

            // Start new Queue for Follower Feed
            Queue followerFeed = QueueFactory.getQueue("follower-feed");
            followerFeed.add(TaskOptions.Builder.withUrl("/queue/follower_feed")
                    .param("userId", String.valueOf(userId))
                    .param("postId", String.valueOf(postId))
                    .param("ownerId", String.valueOf(ownerId))
                    .param("startedByTag", String.valueOf(startedByTag))
                    .param("postSafeKey", postKey.getString())
            );

        } // end if
    }

    /**
     * Save post in private feed
     * @param userId    Id of the user which you want to inform Alert for feed and insert post for this
     * @param postKey    Key of the post that is inserted in feed
     */
    public static void generatePrivateFeed(Long userId, Key postKey){

        // Auto Generate id for Feed
        Long feedId = Datastore.autoGeneratedId();
        // Insert this Post into every Friend Feed (Private Feed)
        PrivateFeed publicFeed = new PrivateFeed(feedId,
                Key.create(User.class, userId),
                Ref.create( postKey )
        );
        // Save this Feed
        ofy().save().entity(publicFeed).now();
        // Update the UserAlert
        Datastore.updateUserAlert(userId, feedId, DataType.Alert.FEED, true);
        //Datastore.informPrivateAlert(userId, feedId, DataType.Alert.FEED); (REMOVE)

    }

    /**
     * Save post in public feed
     * @param userId    Id of the user which you want to inform Alert for feed and insert post for this
     * @param postKey    Key of the post that is inserted in feed
     */
    public static void generatePublicFeed(Long userId, Key postKey){

        // Auto Generate id for Feed
            Long feedId = Datastore.autoGeneratedId();
        // Insert this Post into every Friend Feed (Public Feed)
        PublicFeed publicFeed = new PublicFeed(feedId,
                Key.create(User.class, userId),
                Ref.create( postKey )
        );
        // Save this Feed
        ofy().save().entity(publicFeed).now();
        // Update the UserAlert
        Datastore.updateUserAlert(userId, feedId, DataType.Alert.FEED, false);
        //Datastore.informPublicAlert(userId, feedId, DataType.Alert.FEED); (REMOVE)

    }

    /**
     * Create Feed from list members and notify them
     * @param userId            user who create the post
     * @param specialListId     user list id
     * @param postId            Id of the post which need to insert into Feed
     * @param isAnonymous       Owner of this post is anonymous or not
     */
    private void informListMembers(Long userId, String specialListId, Long postId, Boolean isAnonymous){

        // listId is special String contains int (e.g 3_12344)
        // First letter represent number of members and after (-) there is a list id
        // Convert this special String into listId
        Long listId = Long.valueOf(specialListId.substring(2));

        // Create postKey from parent userId and postId
        Key postKey = Key.create(Key.create(User.class, userId), Post.class, postId);

        // Get the list of members and loop them
        for ( ListMember listMember : UserHelper.getListMember(listId) ) {

            // Create private feed only for these members
            generatePrivateFeed(listMember.getUserRef().get().getUserId(), postKey);

            // Send notification to each member list about this post
            Datastore.sendPrivateNotification(listMember.getUserRef().get().getUserId(), userId ,
                    DataType.NotificationType.LIST_POST, postKey.getString(), isAnonymous);

        }

    }

    /**
     * Create Feed for friends of those Users who are tagged with this post
     * @param userId        Id of the user in who post this and tagged
     * @param postId        Id of the post in which user is tagged
     * @param isPublic      Post mode is public or private
     * @param isAnonymous   Owner of this post is anonymous or not
     */
    private static void tagThisPost(Long userId, Long postId, Boolean isPublic, Boolean isAnonymous){

        // Create postKey from postId
        Key postKey = Key.create(Post.class, postId);

        // Get tagged user from this post
        List<PostTag> postTags = ofy().load().type(PostTag.class).ancestor(postKey).list();

        // Create a safe key from userId and postId
        Key<Post> postSafeKey = Key.create(Key.create(User.class, userId), Post.class, postId);
        String postSafeString = postSafeKey.getString();

        // Loop each user
        for ( PostTag postTag : postTags ) {

            Boolean isApproved = approveTagForThisUser(postId, postTag.getUserId(), userId, isPublic, false, postSafeString);

            // If Tag is approved then set in postTag that tag is approved for this user
            if (isApproved){
                postTag.setTagApproved(true);

                // Save and update
                ofy().save().entity(postTag); // Async Task
            }

        } // end for loop

    }

    // Over loading function approveTagForThisUser
    public static Boolean approveTagForThisUser(
            Long postId, Long taggedUserId, Long userId, Boolean isPublic, Boolean iKnow, String postSafeKey){
        return approveTagForThisUser(postId, taggedUserId, userId, isPublic, iKnow, postSafeKey, false);
    }

    /**
     * Approve a Tag for user
     * @param postId        post in which user tagged or user is going to approve tag for this post
     * @param taggedUserId  Tagged user who is approving this post
     * @param userId        user who is created this post
     * @param isPublic      PostMode, this is a public post or a private
     * @param iKnow         user know when approving tag for this post, it usually yes when user approve tag itself
     * @param postSafeKey   Safe key of the post
     * @param isAnonymous   Owner of this post is anonymous or not
     * @return              Tag is approved or not, Sometime tag is not approved when setting is set on NEVER and user not know
     */
    public static Boolean approveTagForThisUser(
            Long postId, Long taggedUserId, Long userId, Boolean isPublic, Boolean iKnow, String postSafeKey, Boolean isAnonymous){

        // Create a variable to inform that tagged is approved or not, sometime tag is not approved when setting
        // is set on NEVER and user not know
        Boolean isTagApproved = false;

        // Get the user setting how you need to approve this tag, Public, Private, Never
        // In Never case if you user know mean itself approving it then it depend on post type if post is public
        // approve the tag as public if private approve tag as private

        UserProfile userSetting = (UserProfile) ofy().load().key(Key.create(UserProfile.class, taggedUserId)).now();

        // Check tag is approved for what kind of Post
        if (userSetting.getShowTagPost() == DataType.TagApproved.PUBLIC && isPublic
             || (userSetting.getShowTagPost() == DataType.TagApproved.NEVER && iKnow && isPublic) ) { // Tag is approved for Public Post

            // Inform user about this That he/she is tagged with this post
            // If I know don't send me a notification usually when I'm approving tag my self, else send
            if (!iKnow) { // I don't know send notification
                Datastore.sendPublicNotification(taggedUserId, userId, DataType.NotificationType.TAG_POST, postSafeKey, isAnonymous);
            }

            // Set Tag is approved
            isTagApproved = true;

            // Start new Queue for this Post Feed
            Queue publicTagPost = QueueFactory.getQueue("create-feed");
            publicTagPost.add(TaskOptions.Builder.withUrl("/queue/creating_feed")
                    .param("userId", String.valueOf(taggedUserId))
                    .param("postId", String.valueOf(postId))
                    .param("isTagged", String.valueOf(false))
                    .param("queueStartedByTag", String.valueOf(true))
                    .param("ownerId", String.valueOf(userId))
                    .param("postSafeKey", postSafeKey)
                    .param("isPublic", String.valueOf(DataType.Mode.PUBLIC))
                    .param("isAnonymous", String.valueOf(isAnonymous))
            );

        }else if (userSetting.getShowTagPost() == DataType.TagApproved.PRIVATE
                || (userSetting.getShowTagPost() == DataType.TagApproved.PUBLIC && !isPublic )
                || (userSetting.getShowTagPost() == DataType.TagApproved.NEVER && iKnow && !isPublic) ) { // Tag is approved for Private Post

            // Inform user about this That he/she is tagged with this post
            // If I know don't send me a notification usually when I'm approving tag my self, else send
            if (!iKnow) { // I don't know send notification
                Datastore.sendPrivateNotification(taggedUserId, userId, DataType.NotificationType.TAG_POST, postSafeKey, isAnonymous);
            }

            // Set Tag is approved
            isTagApproved = true;

            // Start new Queue for this Post Feed
            Queue publicTagPost = QueueFactory.getQueue("create-feed");
            publicTagPost.add(TaskOptions.Builder.withUrl("/queue/creating_feed")
                    .param("userId", String.valueOf(taggedUserId))
                    .param("postId", String.valueOf(postId))
                    .param("isTagged", String.valueOf(false))
                    .param("queueStartedByTag", String.valueOf(true))
                    .param("ownerId", String.valueOf(userId))
                    .param("postSafeKey", postSafeKey)
                    .param("isPublic", String.valueOf(DataType.Mode.PRIVATE))
                    .param("isAnonymous", String.valueOf(isAnonymous))
            );
        } // end if-else

        return isTagApproved;
    }

}

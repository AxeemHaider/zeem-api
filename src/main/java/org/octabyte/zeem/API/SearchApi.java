package org.octabyte.zeem.API;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.googlecode.objectify.Key;
import de.alpharogroup.jgeohash.GeoHashExtensions;
import org.octabyte.zeem.API.Entity.FeedItem;
import org.octabyte.zeem.API.Entity.PostFeed;
import org.octabyte.zeem.API.Entity.Relationship;
import org.octabyte.zeem.API.Helper.ApiHelper;
import org.octabyte.zeem.API.Helper.UserHelper;
import org.octabyte.zeem.Datastore.*;
import org.octabyte.zeem.Helper.DataType;
import org.octabyte.zeem.Helper.Utils;

import java.util.*;
import java.util.stream.Collectors;

import static org.octabyte.zeem.Helper.OfyService.ofy;

/**
 * Zeem API version v1 - Defining Account EndPoints
 */
@Api(
        name = "zeem",
        version = "v1",
        apiKeyRequired = AnnotationBoolean.TRUE
)
public class SearchApi {

    /**
     * Find user with username. fullName, phone or with geoHash only one parameter is applied
     * @param username  Provide username if you want to search with username
     * @param fullName  Provide fullName if you want to search with fullName
     * @param phone     Provide phone if you want to search with phone
     * @param geoHash   Provide geoHash string if you want to search with geoHash
     * @return          user object with it's information or maybe null if something happen wrong
     */
    public User findUser(
            @Nullable @Named("username") String username,
            @Nullable @Named("fullName") String fullName,
            @Nullable @Named("phone") Long phone,
            @Nullable @Named("geoHash") String geoHash){

        // Hold user object;
        User user = null;

        if (username != null){
            user = ofy().load().type(User.class).filter("username", username.toLowerCase()).first().now();
            user.setProfilePic(Utils.bucketURL + user.getProfilePic());
        }else if(fullName != null){
            user = ofy().load().type(User.class).filter("fullName", fullName.toLowerCase()).first().now();
            user.setProfilePic(Utils.bucketURL + user.getProfilePic());
        }else if(phone != null){
            user = ofy().load().type(User.class).filter("phone", phone).first().now();
            user.setProfilePic(Utils.bucketURL + user.getProfilePic());
        }else if(geoHash != null){
            user = ofy().load().type(User.class).filter("geoHash", geoHash).first().now();
            user.setProfilePic(Utils.bucketURL + user.getProfilePic());
        }

        return user;
    }

    /**
     * Search user with full name
     * @param searchName    Enter search name (prefix name)
     * @return              List of user those start with search name
     */
    public List<User> searchUser(@Named("searchName") String searchName){

        return   ofy().load().type(User.class)
                .filter("fullName >=", searchName.toLowerCase())
                .filter("fullName <", searchName.toLowerCase() + "\uFFFD").limit(10).list();
    }

    /**
     * Filter Post according to given badge
     * @param userId    Id of user who want filter post
     * @param mode      filter from private feed or from public
     * @param badge     badge int
     * @return          list of PostFeed filter with badge
     */
    public PostFeed filterBadgePost(@Named("userId") Long userId, @Named("mode") DataType.Mode mode, @Named("badge") int badge){

        // Create PostFeed object
        PostFeed postFeed = new PostFeed();

        // Create userKey from userId
        Key userKey = Key.create(User.class, userId);
        // Create list to Hold PostFeed
        List<FeedItem> feedItems = new ArrayList<>();

        // Check mode is private or public
        if (mode == DataType.Mode.PRIVATE) { // Get private Stories

            // Get Last 20 Feeds by user parent key
            List<PrivateFeed> privateFeeds = ofy().load().type(PrivateFeed.class).ancestor(userKey)
                    .limit(30).list();

            // Get Posts from list of Feed
            for (PrivateFeed privateFeed : privateFeeds) {

                // Check post is available or not
                Post post = privateFeed.getPostRef().get();
                if (post != null) {
                    // Match user badge then Create formatted Post Feed from Raw Post Object
                    if (ofy().load().key(post.getUserKey()).now().getBadge() == badge)
                        feedItems.add(ApiHelper.FormatUserFeed(privateFeed.getFeedSafeKey(), userId, privateFeed.getPostRef().get()));

                }

            } // end for loop

        }else { // Get Public Stories

            // Get Last 20 Feeds by user parent key
            List<PublicFeed> publicFeeds = ofy().load().type(PublicFeed.class).ancestor(userKey)
                    .limit(30).list();

            // Get Posts from list of Feed
            for (PublicFeed publicFeed : publicFeeds) {

                // Check post is available or not
                Post post = publicFeed.getPostRef().get();
                if (post != null) {
                    // Match user badge then Create formatted Post Feed from Raw Post Object
                    if (ofy().load().key(post.getUserKey()).now().getBadge() == badge)
                        feedItems.add(ApiHelper.FormatUserFeed(publicFeed.getFeedSafeKey(), userId, publicFeed.getPostRef().get()));

                }

            } // end for loop

        } // end if-else

        // Check some item are found or not
        if (feedItems.size() > 0) {
            // Add FeedItem in PostFeed
            postFeed.setFeedList(feedItems);

            return postFeed;
        }else {
            return null;
        }
    }

    /**
     * Filter User according to given Badge
     * @param userId    Id of use who want to filter user
     * @param relation  Relation to filter Friends, follower, following
     * @param badge     Badge int that need to filter
     * @return          list of user (User object)
     */
    public List<User> filterBadgeUser(@Named("userId") Long userId, @Named("relation") DataType.Relation relation, @Named("badge") int badge){

        // Create list to hold User object
        List<User> userList = new ArrayList<>();

        // Check Relation type
        switch (relation) {

            case FRIEND:
                // Loop each friend
                for (Friend friend : UserHelper.getFriendList(userId)){
                    // Get User from friend id
                    User user = (User) ofy().load().key(Key.create(User.class, friend.getFriendId())).now();
                    // Match badge
                    if (user.getBadge() == badge) {
                        user.setProfilePic(Utils.bucketURL + user.getProfilePic());
                        userList.add(user);
                    }

                } // end for loop
                break;
            case FOLLOWER:
                // Loop each Follower
                for (Follower follower : UserHelper.getFollowerList(userId)){
                    // Get User from follower id
                    User user = (User) ofy().load().key(Key.create(User.class, follower.getFollowerId())).now();
                    // Match badge
                    if (user.getBadge() == badge) {
                        user.setProfilePic(Utils.bucketURL + user.getProfilePic());
                        userList.add(user);
                    }

                } // end for loop
                break;
            case FOLLOWING:
                // Loop each following
                for (Following following : UserHelper.getFollowingList(userId)){
                    // Get User from following id
                    User user = (User) ofy().load().key(Key.create(User.class, following.getFollowingId())).now();
                    // Match badge
                    if (user.getBadge() == badge) {
                        user.setProfilePic(Utils.bucketURL + user.getProfilePic());
                        userList.add(user);
                    }

                } // end for loop
                break;

            case PUBLIC:
                // Search from all users (Public search)
                for (User user : ofy().load().type(User.class).list()) {
                    // Match badge
                    if (user.getBadge() == badge) {
                        user.setProfilePic(Utils.bucketURL + user.getProfilePic());
                        userList.add(user);
                    }

                } // end for loop

        } // end switch case

        if (userList.size() > 0) {
            return userList.stream().limit(30).collect(Collectors.toList());
        } else {
            return null;
        }
    }

    /**
     * Get Top Post according to like (Star)
     * @param userId    Id of user who want to get top post
     * @param mode      private stories or public top post
     * @return          list of PostFeed
     */
    public PostFeed topPost(@Named("userId") Long userId, @Named("mode") DataType.Mode mode){

        // Create PostFeed object
        PostFeed postFeed =  new PostFeed();

        // Create userKey from userId
        Key userKey = Key.create(User.class, userId);
        // Create list to Hold PostFeed
        List<FeedItem> feedItems = new ArrayList<>();

        // Check mode is private or public
        if (mode == DataType.Mode.PRIVATE) { // Get private Posts

            // Get Last 20 Feeds by user parent key
            List<PrivateFeed> feedList = ofy().load().type(PrivateFeed.class).ancestor(userKey)
                    .limit(40).list();

            // Check feedList size is there any post or not if not return back
            if (feedList.size() == 0) { // There is no feed find
                return null;
            }

            // Sorting and limit list
            PriorityQueue<PrivateFeed> queue = new PriorityQueue<>(Comparator.comparingInt(p -> p.getPostRef().get().getStarCount()));

            for (PrivateFeed privateFeed : feedList) {
                if (privateFeed.getPostRef().get() != null) {
                    queue.offer(privateFeed);
                    if (queue.size() > 20) {
                        queue.poll();
                    }
                }
            }

            LinkedList<PrivateFeed> privateFeeds = new LinkedList<>();
            while (queue.size() > 0) {
                privateFeeds.push(queue.poll());
            }


            /*  ### This method is also working but the above one is more optimize ###
            // Sort this list
            privateFeeds.sort(Comparator.comparingInt(p -> p.getPostRef().get().getStarCount()));

            // Limit only 20 post
            privateFeeds = privateFeeds.stream().limit(20).collect(Collectors.toList());
            */

            // Get Posts from list of Feed
            for (PrivateFeed privateFeed : privateFeeds) {

                // Check post is available or not
                Post post = privateFeed.getPostRef().get();
                if (post != null) { // Post is available

                    // Create formatted Post Feed from Raw Post Object
                    feedItems.add(ApiHelper.FormatUserFeed(privateFeed.getFeedSafeKey(), userId, post));

                }
            } // end for loop

        }else { // Get Public Posts
            // Get Last 20 Feeds by user parent key
            List<PublicFeed> feedList = ofy().load().type(PublicFeed.class).ancestor(userKey)
                    .limit(40).list();

            // Check feedList size is there any post or not if not return back
            if (feedList.size() == 0) { // There is no feed find
                return null;
            }

            // Sorting and limit list
            PriorityQueue<PublicFeed> queue = new PriorityQueue<>(Comparator.comparingInt(p -> p.getPostRef().get().getStarCount()));
            for (PublicFeed publicFeed : feedList) {
                if (publicFeed.getPostRef().get() != null) {
                    queue.offer(publicFeed);
                    if (queue.size() > 20) {
                        queue.poll();
                    }
                }
            }

            LinkedList<PublicFeed> publicFeeds = new LinkedList<>();
            while (queue.size() > 0) {
                publicFeeds.push(queue.poll());
            }

            /*  ### This method is also working but the above one is more optimize ###
            // Sort this list
            publicFeeds.sort(Comparator.comparingInt(p -> p.getPostRef().get().getStarCount()));

            // Limit only 20 post
            publicFeeds = publicFeeds.stream().limit(20).collect(Collectors.toList());
            */

            // Get Posts from list of Feed
            for (PublicFeed publicFeed : publicFeeds) {

                // Check post is available or not
                Post post = publicFeed.getPostRef().get();
                if (post != null) { // Post is available
                    // Create formatted Post Feed from Raw Post Object
                    feedItems.add(ApiHelper.FormatUserFeed(publicFeed.getFeedSafeKey(), userId, post));
                }

            } // end for loop
        } // end if-else

        // Add feedItems in postFeed object
        postFeed.setFeedList(feedItems);

        return postFeed;
    }

    /**
     * Search Near by user
     * @param userId        id of user that want to search nearby user
     * @param latitude      latitude - location of user that want to search
     * @param longitude     longitude - location of user that want to search
     * @param relation      search in any relation or public
     * @return              list of User object
     */
    public List<User> nearBy(@Named("userId") Long userId, @Named("latitude") Double latitude, @Named("longitude") Double longitude,
                             @Named("Relation") DataType.Relation relation){

        // Create a list to hold Keys
        List<Key> keys = new ArrayList<>();

        // Convert latitude and longitude into geoHash
        String geoHash = GeoHashExtensions.encode(latitude, longitude);

        geoHash = Utils.reducePrecision(geoHash);

        // Get Search Areas(Adjacent areas) list
        List<String> searchAreas = GeoHashExtensions.getAllAdjacentAreasList(geoHash);

        // Check Relation
        switch (relation) {

            case FRIEND:
                // Get friend and convert friend ids into friend Keys
                for (Friend friend : UserHelper.getFriendList(userId)) {
                    keys.add(Key.create(User.class, friend.getFriendId()));
                } // end for loop

                // Get search areas in friend list
                return ofy().load().type(User.class).filterKey("in", keys).filter("geoHash in", searchAreas).limit(10).list();

            case FOLLOWER:
                // Get follower and convert follower ids into friend Keys
                for (Follower follower : UserHelper.getFollowerList(userId)) {
                    keys.add(Key.create(User.class, follower.getFollowerId()));
                } // end for loop

                // Get search areas in follower list
                return ofy().load().type(User.class).filterKey("in", keys).filter("geoHash in", searchAreas).limit(10).list();

            case FOLLOWING:
                // Get following and convert following ids into friend Keys
                for (Following following : UserHelper.getFollowingList(userId)) {
                    keys.add(Key.create(User.class, following.getFollowingId()));
                } // end for loop

                // Get search areas in following list
                return ofy().load().type(User.class).filterKey("in", keys).filter("geoHash in", searchAreas).limit(10).list();

            case PUBLIC:
                // Public search
                return ofy().load().type(User.class).filter("geoHash in", searchAreas).limit(10).list();

        } // end switch case

        // Public search
        return ofy().load().type(User.class).filter("geoHash in", searchAreas).limit(10).list();
    }

    /**
     * Get relation between two user
     * @param myUserId  First user, which is my id
     * @param userId    Second user, First user relation with second user
     * @return          Relationship String, NO_RELATION, FRIEND, FOLLOWER, FOLLOWING
     */
    public Relationship getRelationship(@Named("myUserId") Long myUserId, @Named("userId") Long userId){

        // Get relation between two user
        return findRelationBetweenTwoUser(myUserId, userId);

    }

    /**
     * Find Relation between two user
     * @param myUserId  First user, which is my id
     * @param userId    Second user, First user relation with second user
     * @return          Relationship String, NO_RELATION, FRIEND, FOLLOWER, FOLLOWING
     */
    public static Relationship findRelationBetweenTwoUser(Long myUserId, Long userId){
        // Convert my user id into my user key
        Key<User> myUserKey = Key.create(User.class, myUserId);

        // Create Relationship object
        Relationship relationship = new Relationship("NO_RELATION");

        // Check this user is in my friend
        Friend inFriend = ofy().load().type(Friend.class).ancestor(myUserKey).filter("friendId", userId).first().now();

        // If user is find in my friend list then set relation as FRIEND
        if (inFriend != null) { // user is in friend list

            relationship.setRelation("FRIEND");

        }else { // User is not in friend list

            // Check in follower list
            Follower inFollower = ofy().load().type(Follower.class).ancestor(myUserKey).filter("followerId", userId).first().now();
            // If user is find in my follower list then set relation as FOLLOWER
            if (inFollower != null) { // user is in follower list

                relationship.setRelation("FOLLOWER");

                // Check also in Following list Because Follower may be also a following
                Following inFollowing = ofy().load().type(Following.class).ancestor(myUserKey).filter("followingId", userId).first().now();
                // If user is find in my following list then set isFollowing = true
                if (inFollowing != null) { // user is in following list

                    relationship.setFollowing(true);

                }

            }else { // user is not in follower list

                // Check in Following list
                Following inFollowing = ofy().load().type(Following.class).ancestor(myUserKey).filter("followingId", userId).first().now();
                // If user is find in my following list then set relation as FOLLOWING
                if (inFollowing != null) { // user is in following list

                    relationship.setRelation("FOLLOWING");

                }

            }

        }

        // If user is not find in any list it means there is no relation between these two  friends

        return relationship;

    }

    /**
     * Get mention user list for private post and for public
     * @param userId        user id who want to get mention user list
     * @param postMode      Mode of the post, Private or Public
     * @param isAnonymous   User is anonymous or not
     * @param searchName    If a Public post then add search Name to search from users
     * @return              List of users who allow tag
     */
    public List<User> getMentionUser(@Named("userId") Long userId, @Named("postType") DataType.Mode postMode,
                                     @Named("isAnonymous") Boolean isAnonymous, @Nullable @Named("searchName") String searchName){

        // Check post mode, if it's private then only search from my Friends
        if (postMode == DataType.Mode.PRIVATE) { // Post is private search only from Friends

            // Create list to hold friendKeys
            List<Key> friendKeys = new ArrayList<>();

            // Get friend list and convert into userKeys
            for (Friend friend : UserHelper.getFriendList(userId)) {
                friendKeys.add(Key.create(UserProfile.class, friend.getFriendId()));
            } // end for loop

            // Check it's an anonymous user or not

            // Batch Load User Profiles to check anonymous user is allowed or not
            if (friendKeys.size() > 0) {

                return userWhoAllowTag(friendKeys, isAnonymous);

            }

        }else{ // Post mode is Public

            // Check search name is specified or not
            if (searchName != null){

                // Create list to hold userKeys
                List<Key> userKeys = new ArrayList<>();

                // Search the user First and get only 10 user
                List<User> searchUserList = ofy().load().type(User.class)
                        .filter("fullName >=", searchName.toLowerCase())
                        .filter("fullName <", searchName.toLowerCase() + "\uFFFD").limit(10).list();

                if (searchUserList.size() > 0) {

                    // Loop each user and convert user id into user key
                    for (User user : searchUserList) {

                        userKeys.add(Key.create(UserProfile.class, user.getUserId()));

                    } // end loop

                    return userWhoAllowTag(userKeys, isAnonymous);

                } // end inner if

            } // end if


        }   // end if-else

        return null;
    }

    /**
     * Check user is allow tag or not, pass list of user keys and this function return those user key who allow tag
     * @param userKeys      List of user keys that you want to check who are allow tag from these
     * @param isAnonymous   If you also want to check anonymous tag is allowed or not
     * @return              List of user who allow tag
     */
    private List<User> userWhoAllowTag(List<Key> userKeys, Boolean isAnonymous){

        List<UserProfile> profileList = ofy().load().type(UserProfile.class).filterKey("in", userKeys).list();

        // Clear the a list to hold user keys
        List<Key> allowedUserKey = new ArrayList<>();

        // Loop each friend and check this user is Tag or not
        for (UserProfile profile : profileList) {

            if (profile.getPostTag() != DataType.HowTag.NO_ONE) { // This user allow tag

                // If user is anonymous then also check this user allow anonymous tag or not
                if (isAnonymous) { // User is Anonymous

                    if (profile.getAnonymousTag()) { // This user allow anonymous
                        allowedUserKey.add(Key.create(User.class, profile.getUserId())); // Add this into friend key
                    }

                } else { // User is not anonymous
                    allowedUserKey.add(Key.create(User.class, profile.getUserId())); // Add this into friend key
                }

            }

        } // end for loop

        // Batch Load and return
        if (allowedUserKey.size() > 0) {
            return ofy().load().type(User.class).filterKey("in", allowedUserKey).list();
        }else {
            return null;
        }
    }

}

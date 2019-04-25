package org.octabyte.zeem.API.Helper;

import com.googlecode.objectify.Key;
import org.octabyte.zeem.API.Entity.*;
import org.octabyte.zeem.Datastore.*;
import org.octabyte.zeem.Helper.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.octabyte.zeem.Helper.OfyService.ofy;

public class ApiHelper {

    /**
     * Major use CommentApi - Generate Comments in formatted form into CommentResponse
     * @param commentList   comment list which you want to convert into formatted form
     * @param userId        Id of user who is getting comment, may be null when you are generated post feed
     * @return              formatted comments in form of CommentItem list
     */
    public static List<CommentItem> generatePostComment(List<Comment> commentList, Long userId){



        // Create list to hold post comments
        List<CommentItem> commentItems = new ArrayList<>();

        // Loop each comment and format it into postComment
        for (Comment comment : commentList) {

            // Make sure comment is not null. Sometimes it is null when user delete the comment
            if (comment != null) {

                User user  = comment.getUserRef().get();
                // Create a CommentResponse object
                CommentItem commentItem = new CommentItem(
                        comment.getCommnetSafeKey(),
                        comment.getType(),
                        comment.getSource(),
                        comment.getAnonymous(),
                        comment.getStarCount(),
                        user.getUsername(),
                        user.getFullName(),
                        user.getUserId(),
                        Utils.bucketURL+user.getProfilePic(),
                        user.getBadge()
                );

                // Setting comment post on time in human readable format
                commentItem.setPostedOnHumanReadable(Utils.formatTime(comment.getPostedOn(), true));

                // Check userId is not null, it may be null when you are generating post feed
                // Because like is not showing in Feed

                if (userId != null) {

                    // Check someone like this comment or not

                    if (comment.getCommentStar() !=  null) { // Some user like this comment

                        // Get list of users who liked this comment, and loop them
                        for (Long starUserId : comment.getCommentStar()) {

                            // Check if any id is match with my id
                            if (userId.equals(starUserId)) { // matched, it means I liked this comment
                                commentItem.setStarByMe(true);
                            }

                        } // end for loop

                    } // end inner if

                } // end if

                commentItems.add(commentItem);
            }

        } // end for loop

        return commentItems;
        //return Lists.reverse(commentItems);
    }

    /**
     * Major use StoryApi - Generate Story Comments in formatted form into CommentResponse
     * @param commentList   comment list which you want to convert into formatted form
     * @return              formatted comments in form of CommentItem list
     */
    public static List<CommentItem> generateStoryComment(List<StoryComment> commentList){
        // Create list to hold post comments
        List<CommentItem> commentItems = new ArrayList<>();

        // Loop each comment and format it into postComment
        for (StoryComment comment : commentList) {

            User user = comment.getUserRef().get();
            CommentItem commentItem =  new CommentItem(
                    comment.getCommnetSafeKey(),
                    comment.getType(),
                    comment.getSource(),
                    comment.getAnonymous(),
                    0,
                    user.getUsername(),
                    user.getFullName(),
                    user.getUserId(),
                    Utils.bucketURL+user.getProfilePic(),
                    user.getBadge()
            );

            // Setting comment post on time in human readable format
            commentItem.setPostedOnHumanReadable(Utils.formatTime(comment.getPostedOn(), true));

            commentItems.add( commentItem );


        } // end for loop

        return commentItems;
    }

    // Overloading function FormatUserFeed
    public static FeedItem FormatUserFeed(Long myUserId, Post post){
        return FormatUserFeed(null, myUserId, post);
    }

    /**
     * Convert Post Objects into formatted PostFeed
     * @param feedSafeKey   Feed safe key, used to remove post from feed. Hide any post from feed
     * @param myUserId      Id of the user who is getting feed
     * @param post          Object that need to be convert into formatted form
     * @return              FeedItem - Formatted form of Post
     */
    public static FeedItem FormatUserFeed(String feedSafeKey, Long myUserId, Post post){

        // Hold post cover and post source media url
        String postCover = null;
        String postSource = null;

        // Set post cover and post source to full media link if these are not null
        if (post.getCover() != null)
            postCover = Utils.bucketURL+post.getCover();

        // Check post source
        if (post.getSource() != null)
            postSource = Utils.bucketURL+post.getSource();

        // Get Post from Feed and make object into PostFeed
        FeedItem feedItem = new FeedItem(
                post.getMode(), post.getType(), post.getLocation(), post.getAnonymous(), post.getStarCount(),
                postCover, postSource, post.getCaption(), post.getCardColor(), post.getPostTag(), post.getTaggedCount(),
                post.getAllowComment(), post.getTotalComments()
        );

        // Setting feed safe key
        feedItem.setFeedSafeKey(feedSafeKey);

        // Setting post safe key
        feedItem.setPostSafeKey(post.autoPostKey());
        // Setting post key
        feedItem.setPostId(post.getPostId());

        // Get user info who is posted this and set into post feed
        User user = (User) ofy().load().key(post.getUserKey()).now();
        // Setting user info
        feedItem.setUserId(user.getUserId());
        feedItem.setUserFullName(user.getFullName());
        feedItem.setUserProfilePic(Utils.bucketURL+user.getProfilePic());
        feedItem.setUserBadge(user.getBadge());

        // Format postedOn time into human readable
        feedItem.setPostedOnHumanReadable(Utils.formatTime(post.getPostedOn()) + " Ago");

        // Check there is any expire time or not for this post
        if (post.getExpireTime() != null) // Expire time is added for this post
            feedItem.setExpireTimeHumanReadable(" Expire in " + Utils.formatTime(post.getExpireTime()));

        // Check this is list post or not
        if (post.getListId() != null) // List Post
            feedItem.setUnFormattedListId(post.getListId());

        // Check Comment is allow in this post or not if allow then there is any comment for this post
        if (post.getAllowComment() && post.getRecentComment() != null) { // There is some comment
            // Create list to Hold CommentResponse
            List<CommentItem> commentItems = ApiHelper.generatePostComment(post.getCommentList(), null);
            // Add Post comments into post Feed
            feedItem.setCommentItems(commentItems);
        }

        // Check post is tagged to someone or not
        if (post.getPostTag()){ // Post is tagged

            // Now check post is tagged to me or not
            // Create postKey from postId
            Key postKey = Key.create(Post.class, post.getPostId());

            // Get tagged user from this post
            List<PostTag> postTags = ofy().load().type(PostTag.class).ancestor(postKey).list();

            // Loop each user
            for ( PostTag postTag : postTags ) {

                // Compare tagged user ids with my id
                if (myUserId.equals(postTag.getUserId())){ // This post is tagged to me
                    feedItem.setTaggedMe(true);
                    feedItem.setTaggedApproved(postTag.getTagApproved()); // Set tag is approved for me or not
                    break; // Break the loop
                }

            } // end for loop

        } // end for loop

        // Check this post is liked by me or not
        PostStar isLikedByMe = ofy().load().type(PostStar.class)
                .ancestor(Key.create(User.class, myUserId))
                .filter("postSafeKey", post.autoPostKey())
                .first().now();

        if (isLikedByMe != null){ // Post is liked by me
            feedItem.setStarByMe(true);
        } // end if

        return feedItem;
    }

    /**
     * Convert Story Objects into formatted StoryFeed
     * @param story  Object that need to be convert into formatted form
     * @return      StoryFeed - Formatted form of Story Feed
     */
    public static StoryFeed FormatUserStory(Story story){

        // Get Story from Feed and make object into StoryFeed
        StoryFeed storyFeed = new StoryFeed();
        storyFeed.setStory(story);

        // Get user info who is posted this story and set into story feed
        User user = (User) ofy().load().key(story.getUserKey()).now();
        user.setProfilePic(Utils.bucketURL+user.getProfilePic());
        storyFeed.setUser(user);

        // Convert story updated time into human readable
        storyFeed.setUpdatedOnHumanReadable(Utils.formatTime(story.getUpdatedOn()) + " Ago");

        return storyFeed;
    }

}

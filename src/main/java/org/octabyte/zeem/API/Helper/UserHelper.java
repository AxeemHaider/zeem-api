package org.octabyte.zeem.API.Helper;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import org.octabyte.zeem.Datastore.*;
import org.octabyte.zeem.Helper.Utils;

import java.util.List;

import static org.octabyte.zeem.Helper.OfyService.ofy;

public class UserHelper {

    /**
     * Increment user star
     * @param userId Id of user Which star need to increment
     */
    public static void incrementUserStar(Long userId){
        // Get userInfo object
        UserProfile userProfile = (UserProfile) ofy().load().key(Key.create(UserProfile.class, userId)).now();
        // Add 1 into star count
        int newStarCount = userProfile.getStarCount() + 1;
        userProfile.setStarCount( newStarCount );
        // Save and update user info
        ofy().save().entity(userProfile); // Async way

        // Check there is any need to change the user badge
        if (Utils.getBadgeByStar(userProfile.getStarCount()) != Utils.getBadgeByStar(newStarCount)){ // Previous badge and new badge is not matching
            // Because badges are not matching so update user badge to newer one
            // Get user
            User user = (User) ofy().load().key(Key.create(User.class, userId)).now();

            // Set new badge to user
            user.setBadge(Utils.getBadgeByStar(newStarCount));

            // Save and update
            ofy().save().entity(user); // Async Task

        } // end if

    }

    /**
     * Increment user post count
     * @param userId Id of user Which post need to increment
     */
    public static void incrementPostCount(Long userId){
        // Get userInfo object
        UserProfile userProfile = (UserProfile) ofy().load().key(Key.create(UserProfile.class, userId)).now();
        // Add 1 into post count
        userProfile.setPostCount( userProfile.getPostCount() + 1 );
        // Save and update user info
        ofy().save().entity(userProfile); // Async way
    }

    /**
     * Increment user friend count
     * @param userId Id of user Which friends need to increment
     */
    public static void incrementFriendCount(Long userId){
        // Get userInfo object
        UserProfile userProfile = (UserProfile) ofy().load().key(Key.create(UserProfile.class, userId)).now();
        // Add 1 into friend count
        userProfile.setFriendCount( userProfile.getFriendCount() + 1 );
        // Save and update user info
        ofy().save().entity(userProfile); // Async way
    }

    /**
     * Increment user follower count
     * @param userId Id of user Which follower need to increment
     */
    public static void incrementFollowerCount(Long userId){
        // Get userInfo object
        UserProfile userProfile = (UserProfile) ofy().load().key(Key.create(UserProfile.class, userId)).now();
        // Add 1 into follower count
        userProfile.setFollowerCount( userProfile.getFollowingCount() + 1 );
        // Save and update user info
        ofy().save().entity(userProfile); // Async way
    }

    /**
     * Increment user following count
     * @param userId Id of user Which following need to increment
     */
    public static void incrementFollowingCount(Long userId){
        // Get userInfo object
        UserProfile userProfile = (UserProfile) ofy().load().key(Key.create(UserProfile.class, userId)).now();
        // Add 1 into following count
        userProfile.setFollowingCount( userProfile.getFollowingCount() + 1 );
        // Save and update user info
        ofy().save().entity(userProfile); // Async way
    }

    /**
     * Decrement user friend count
     * @param userId Id of user Which friend need to decrement
     */
    public static void decrementFriendCount(Long userId){
        // Get userInfo object
        UserProfile userProfile = (UserProfile) ofy().load().key(Key.create(UserProfile.class, userId)).now();
        // subtract 1 into friend count
        userProfile.setFriendCount( userProfile.getFriendCount() - 1 );
        // Save and update user info
        ofy().save().entity(userProfile); // Async way
    }

    /**
     * Decrement user follower count
     * @param userId Id of user Which follower need to decrement
     */
    public static void decrementFollowerCount(Long userId){
        // Get userInfo object
        UserProfile userProfile = (UserProfile) ofy().load().key(Key.create(UserProfile.class, userId)).now();
        // subtract 1 into follower count
        userProfile.setFollowerCount( userProfile.getFollowerCount() - 1 );
        // Save and update user info
        ofy().save().entity(userProfile); // Async way
    }

    /**
     * Decrement user following count
     * @param userId Id of user Which following need to decrement
     */
    public static void decrementFollowingCount(Long userId){
        // Get userInfo object
        UserProfile userProfile = (UserProfile) ofy().load().key(Key.create(UserProfile.class, userId)).now();
        // subtract 1 into friend count
        userProfile.setFollowingCount( userProfile.getFollowingCount() - 1 );
        // Save and update user info
        ofy().save().entity(userProfile); // Async way
    }

    /**
     * Get Friend list from userId
     * @param userId Id of the user which you want to get friends
     * @param limit  it's optional parameter specific limit of friend list
     * @return  List of friends (Friend object)
     */
    public static List<Friend> getFriendList(Long userId, int limit){
        // Create userKey from userId
        Key userKey = Key.create(User.class, userId);
        // Get all friends By userId
        Query friends = ofy().load().type(Friend.class).ancestor(userKey);

        // Check there is any limit for friend list or not
        if(limit == 0) // There is no limt
            return friends.list();
        else // There is limit
            return friends.limit(limit).list();
    }

    /**
     * Get Friend list from userId
     * @param userId Id of the user which you want to get friends
     * @return  List of friends (Friend object)
     */
    public static List<Friend> getFriendList(Long userId){
        return getFriendList(userId, 0);
    }

    public static List<BlockFriend> getBlockedFriendList(Long userId){
        return ofy().load().type(BlockFriend.class).filter("userId", userId).list();
    }

    /**
     * Get Follower list from userId
     * @param userId  Id of the user which you want to get follower
     * @param limit  it's optional parameter specific limit of follower list
     * @return  List of followers
     */
    public static List<Follower> getFollowerList(Long userId, int limit){

        // Create userKey from userId
        Key userKey = Key.create(User.class, userId);
        // Get all Follower by userId
        Query followers = ofy().load().type(Follower.class).ancestor(userKey);

        // Check there is any limit for friend list or not
        if(limit == 0) // There is no limt
            return followers.list();
        else // There is limit
            return followers.limit(limit).list();
    }

    /**
     * Get Follower list from userId
     * @param userId  Id of the user which you want to get follower
     * @return  List of followers
     */
    public static List<Follower> getFollowerList(Long userId){
        return getFollowerList(userId, 0);
    }

    /**
     * Get Following list from userId
     * @param userId  Id of the user which you want to get following
     * @param limit  it's optional parameter specific limit of following list
     * @return  List of following
     */
    public static List<Following> getFollowingList(Long userId, int limit){

        // Create userKey from userId
        Key userKey = Key.create(User.class, userId);
        // Get all Follower by userId
        Query following = ofy().load().type(Following.class).ancestor(userKey);

        // Check there is any limit for friend list or not
        if(limit == 0) // There is no limt
            return following.list();
        else // There is limit
            return following.limit(limit).list();
    }

    /**
     * Get Follower list from userId
     * @param userId  Id of the user which you want to get following
     * @return  List of following
     */
    public static List<Following> getFollowingList(Long userId){
        return getFollowingList(userId, 0);
    }

    /**
     * Get List members using list id
     * @param listId  Id of the list which you want to get members
     * @return  List of members
     */
    public static List<ListMember> getListMember(Long listId){

        // Create listKey from listId
        Key listKey = Key.create(UserList.class, listId);
        // Get all members from list
        return ofy().load().type(ListMember.class).ancestor(listKey).list();
    }

}

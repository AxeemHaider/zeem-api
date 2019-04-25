package org.octabyte.zeem.API;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.Work;
import org.octabyte.zeem.API.Entity.TaskComplete;
import org.octabyte.zeem.API.Helper.Datastore;
import org.octabyte.zeem.API.Helper.UserHelper;
import org.octabyte.zeem.Datastore.*;
import org.octabyte.zeem.Helper.DataType;
import org.octabyte.zeem.Helper.Utils;
import org.octabyte.zeem.Queues.CreateFeed;
import org.octabyte.zeem.Queues.Friendship;

import static org.octabyte.zeem.Helper.OfyService.ofy;

/**
 * Zeem API version v1 - Defining User EndPoints
 */
@Api(
        name = "zeem",
        version = "v1",
        apiKeyRequired = AnnotationBoolean.TRUE
)
public class FriendApi {

    /**
     * Add New friend
     * @param userId        Id of that user Who is making friend
     * @param userPhone     User phone who is making friend
     * @param friendPhone   Phone of that user who is going to become friend
     */
    public void addFriend(@Named("userId") Long userId, @Named("userPhone") Long userPhone, @Named("friendPhone") Long friendPhone){

        // Create userKey from userId
        Key userKey = Key.create(User.class, userId);

        // Save this phone into user Contact
        Contact contact = new Contact(friendPhone, userKey);
        ofy().save().entity(contact); // Async Task

        Long friendId = Friendship.addFriend(userId, userPhone, friendPhone);

        // Check request is send or both become friend
        if(friendId != 0L){  // Both become friend for each other
            Friend friend = new Friend(userKey, friendId);
            ofy().save().entity(friend); // Async Task
        }

    }

    /**
     * Accept Friend request and create feed for both user and friend
     * @param userId    Id of user which is send to be request
     * @param requestId Id of the request
     * @param friendId  Id of user how send the request
     */
    public TaskComplete acceptFriendRequest(@Named("userId") Long userId, @Named("requestId") Long requestId, @Named("friendId") Long friendId){

        // Make them friend
        // First make friendId as My Friend and send notification to friendId
        Friendship.makeFriend(userId, friendId, true);
        // Second make me as a Friend of friendId and no notification for me
        Friendship.makeFriend(friendId, userId, false);

        // increment in both friend count
        UserHelper.incrementFriendCount(userId);
        UserHelper.incrementFriendCount(friendId);

        try {
            // load last friend private post into user private feed
            // Get last post from Friend posts
            Post friendLastPost = ofy().load().type(Post.class).ancestor(Key.create(User.class, friendId)).first().safe();
            // Make last post as a Feed of userId
            CreateFeed.generatePrivateFeed(userId, Key.create(friendLastPost.getWebSafeKey()));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        try {
            // load last user private post into friend private feed
            // Get last post from User posts
            Post userLastPost = ofy().load().type(Post.class).ancestor(Key.create(User.class, userId)).first().safe();
            // Make last post as a Feed of friendId
            CreateFeed.generatePrivateFeed(friendId, Key.create(userLastPost.getWebSafeKey()));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        // delete friend request from user FriendRequest
        Key requestKey = Key.create( Key.create(User.class, userId), FriendRequest.class, requestId);
        ofy().delete().key(requestKey);

        return new TaskComplete(true);
    }

    /**
     * Block Friend
     * @param userId    Who want to block friend
     * @param friendId  Which need to be block
     * @return          TaskComplete TRUE | FALSE
     */
    public TaskComplete blockFriend(@Named("userId") Long userId, @Named("friendId") Long friendId){

        // Start new Transaction
        TaskComplete taskComplete = ofy().transact(new Work<TaskComplete>() {
            @Override
            public TaskComplete run() {

                try {

                    // Fetch friend to remove it
                    Friend friend = ofy().load().type(Friend.class).ancestor(Key.create(User.class, userId))
                            .filter("friendId", friendId).first().safe();

                    // Save Friend in block list
                    ofy().save().entity(new BlockFriend(userId, friendId)); // Async Task

                    // Remove friend from my friend list
                    ofy().delete().entity(friend); // Async Task

                    // Decrement in friend count
                    UserHelper.decrementFriendCount(userId);

                    // Fetch my id in friend list
                    Friend myself = ofy().load().type(Friend.class).ancestor(Key.create(User.class, friendId))
                            .filter("friendId", userId).first().now();
                    // Remove me from my friend, friend list
                    ofy().delete().entity(myself); // Async Task

                    // Decrement in friend count
                    UserHelper.decrementFriendCount(friendId);

                    return new TaskComplete(true);

                } catch (NotFoundException e) {
                    return new TaskComplete(false);
                }
            }
        });

        return taskComplete;
    }

    /**
     * Un Block a blocked friend
     * @param userId        Id of user who want to unblock friend
     * @param friendId      Id of user which need to be unblock
     */
    public void unBlockFriend(@Named("userId") Long userId, @Named("friendId") Long friendId){

        // Get entity key to delete it
        Key<BlockFriend> blockFriendKey = ofy().load().type(BlockFriend.class)
                .filter("userId", userId).filter("friendId", friendId).keys()
                .first().now();

        // check block friend is not null if null return function
        if (blockFriendKey == null) {
            return;
        }

        // Start transaction
        ofy().transact(new VoidWork() {
            @Override
            public void vrun() {

                // Make Both to friend each other
                Friendship.makeFriend(userId, friendId, false);
                Friendship.makeFriend(friendId, userId, false);

                // increment in both friend count
                UserHelper.incrementFriendCount(userId);
                UserHelper.incrementFriendCount(friendId);

                // Delete it from block friend list
                ofy().delete().key(blockFriendKey); // Async Task

            }
        });

    }

    /**
     * Follow any user
     * @param userId        Who want to Follow any user
     * @param followingId   Id of user that need to be follow
     */
    public void follow(@Named("userId") Long userId, @Named("followingId") Long followingId){

        // Start new Transaction
        ofy().transact(new VoidWork() {
            @Override
            public void vrun() {
                // Save this followingId(friendId or userId) into Following list
                Following following = new Following(Key.create(User.class, userId), followingId);
                // Save this
                ofy().save().entity(following); // Async Task

                // Make userId as the Follower of followingId
                Follower follower = new Follower(Key.create(User.class, followingId), userId);
                // Save this
                ofy().save().entity(follower); // Async Task

                // Send Notification to follower(followingId)
                Datastore.sendPublicNotification(followingId, userId, DataType.NotificationType.FOLLOWER, null);

                // Increment in Following count for userId
                UserHelper.incrementFollowingCount(userId);
                // Increment in Follower count for followingId
                UserHelper.incrementFollowerCount(followingId);
            }
        });

    }

    /**
     * Un-Follow any user from following list
     * @param userId    Who want to unFollow
     * @param followId  Which need to unFollow
     */
    public void unFollow(@Named("userId") Long userId, @Named("followId") Long followId){

        // Start Transaction
        ofy().transact(new VoidWork() {
            @Override
            public void vrun() {
                //  Remove followId from following list
                Key<Following> following = ofy().load().type(Following.class).ancestor(Key.create(User.class, userId))
                        .filter("followingId", followId).keys().first().now();
                // Delete it
                ofy().delete().entity(following); // Async Task

                // Remove userId from Follower list
                Key<Follower> follower = ofy().load().type(Follower.class).ancestor(Key.create(User.class, followId))
                        .filter("followerId", userId).keys().first().now();
                // Delete it
                ofy().delete().entity(follower); // Async Task

                // Decrement in Following count for userId
                UserHelper.decrementFollowingCount(userId);
                // Decrement in Follower cont for followId
                UserHelper.decrementFollowerCount(followId);
            }
        });

    }

}

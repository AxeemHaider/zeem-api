package org.octabyte.zeem.API;

import com.google.api.server.spi.config.*;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.googlecode.objectify.*;
import com.googlecode.objectify.cmd.Query;
import de.alpharogroup.jgeohash.GeoHashExtensions;
import org.octabyte.zeem.API.Entity.*;
import org.octabyte.zeem.API.Helper.*;
import org.octabyte.zeem.Datastore.*;
import org.octabyte.zeem.Firebase.ChatMessage;
import org.octabyte.zeem.Firebase.PushNotification;
import org.octabyte.zeem.Helper.DataType;
import org.octabyte.zeem.Helper.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.octabyte.zeem.Helper.OfyService.ofy;

/**
 * Zeem API version v1 - Defining User EndPoints
 */
@Api(
        name = "zeem",
        version = "v1",
        apiKeyRequired = AnnotationBoolean.TRUE
)

public class UserApi {

    /**
     * Send instant chat messages
     * @param chatMessage       chatMessage object contain all the information for example where and what to send
     * @return TaskComplete     return TRUE if task is complete else FALSE in case on error
     */
    public TaskComplete sendInstantMessage(ChatMessage chatMessage){
        TaskComplete taskComplete = new TaskComplete();

        Boolean messageSend = PushNotification.sendChatMessage(chatMessage);

        taskComplete.setComplete(messageSend);

        return taskComplete;
    }

    /**
     * Refresh user firebase token, this token is mostly used to send push notifications
     * @param userId            user who need to update token
     * @param firebaseToken     newly firebase token
     */
    public void refreshFirebaseToken(@Named("userId") Long userId, @Named("firebaseToken") String firebaseToken){

        // Get user
        try {

            User user = (User) ofy().load().key(Key.create(User.class, userId)).safe();

            // Set user to a new firebase token
            user.setFirebaseToken(firebaseToken);

            // Save and update
            ofy().save().entity(user).now();

        } catch (NotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Function used to update user location. Set current location of user in DataStore
     * @param userId    user how's location is going to set
     * @param lat       Latitude of location
     * @param lng       longitude of location
     */
    public void updateUserLocation(@Named("userId") Long userId, @Named("lat") Double lat, @Named("lng") Double lng){

        // Convert user lat and lng into geoHash using Library
        String geoHash = GeoHashExtensions.encode(lat, lng);

        geoHash = Utils.reducePrecision(geoHash);

        // Get user
        try {
            User user = (User) ofy().load().key(Key.create(User.class, userId)).safe();
            // Set current location of the user
            user.setGeoHash(geoHash);

            // Save user
            ofy().save().entity(user); // Async Task

        } catch (NotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Get User profile view, contains user info, profile info, relation with second user, and post
     * @param appUserId  First user id which is my user id or app user id
     * @param userId    Second user id, For relation between first user and second user
     * @param username  When userId is not available find user with username
     * @return          Profile view contain, user, userProfile, relationship and post
     */
    public ProfileView getProfileView(@Named("appUserId") Long appUserId, @Nullable @Named("userId") Long userId,
                                      @Nullable @Named("username") String username){

        // Hold user id which you need a profile By default it's appUserId
        Long profileUserId = appUserId;

        ProfileView profileView = new ProfileView();
        Relationship relationship = null;

        // Check it's a self user or not
        if (userId != null && !appUserId.equals(userId)) { // Not a self user

            // Set profileUserId to userId If not self user we need to get profile of userId
            profileUserId = userId;

            // get user info
            User user = ofy().load().key(Key.create(User.class, profileUserId)).now();
            user.setProfilePic(Utils.bucketURL + user.getProfilePic());
            profileView.setUser(user);

            // Get relation between users
            relationship = SearchApi.findRelationBetweenTwoUser(appUserId, profileUserId);
            // Set relationship in profile view
            profileView.setRelationship( relationship );
        }

        // Check username is not null it mean userId is null and now you need to get user first
        if (username != null){

            // Find user if not find return null that's mean username is invalid (not exist)
            try {

                User user = ofy().load().type(User.class).filter("username", username).first().safe();

                // Check it's a self user or not
                if (!appUserId.equals(user.getUserId())) { // Not a self user

                    // Set profileUserId to this userId
                    profileUserId = user.getUserId();

                    // Setting user
                    user.setProfilePic(Utils.bucketURL + user.getProfilePic());
                    profileView.setUser(user);

                    // Get relation between users
                    relationship = SearchApi.findRelationBetweenTwoUser(appUserId, profileUserId);
                    // Set relationship in profile view
                    profileView.setRelationship(relationship);
                }

            } catch (NotFoundException e) {
                e.printStackTrace();

                return null;
            }

        }


        // Get user profile
        profileView.setUserProfile(ofy().load().key(Key.create(UserProfile.class, profileUserId)).now());

        DataType.Mode postMode = null;

        // Check relationship is not null, it is null when selfUser is true
        if (relationship != null ){ // Relation not null, it's also mean this is not a selfUser

            // Check if it's not a friend then show only public post otherwise show public and private both
            if (!relationship.getRelation().equals("FRIEND")){ // It's not a friend show only public post
                postMode = DataType.Mode.PUBLIC;
            } // end inner if

        } // end if

        // Get user posts
        profileView.setPost( fetchUserPost(profileUserId, postMode, null) );

        return profileView;
    }

    /**
     * Get updation for mobile app, which things need to update
     * @param userId                    Id of user that want to get updation
     * @return                          UserAlert object, which return Ids for feed and notification
     */
    public UserAlert getUpdationAlert(@Named("userId") Long userId){

        return ofy().load().key(Key.create(UserAlert.class, userId)).now();

    }

    /**
     * Get the user
     * @param userId Id of user that you want to get
     * @return  Object of user
     */
    public User getUser(@Named("userId") Long userId){

        User user = ofy().load().key(Key.create(User.class, userId)).now();
        user.setProfilePic(Utils.bucketURL + user.getProfilePic());
        return user;
    }

    /**
     * Get user profile
     * @param userId    id of user that you want to get info
     * @return          user profile
     */

    public UserProfile getUserProfile(@Named("userId") Long userId){

        return ofy().load().key(Key.create(UserProfile.class, userId)).now();
    }

    /**
     * Update user profile
     * @param myUserId        Id of user who want to update profile
     * @param fullName      Nullable - Name of user
     * @param profilePic    Nullable - profile pic of user
     * @param email         Nullable - Email of user
     * @param status        Nullable - Status of user
     * @param dob           Nullable - Date of Birth of user
     * @param gender        Nullable - Gender of user M | F
     */

    public void updateProfile(@Named("myUserId") Long myUserId,
                              @Nullable @Named("fullName") String fullName,
                              @Nullable @Named("profilePic") String profilePic,
                              @Nullable @Named("email") String email,
                              @Nullable @Named("status") String status,
                              @Nullable @Named("dob") Integer dob,
                              @Nullable @Named("gender") DataType.Gender gender){


        // Check if full Name or profile pic is not null then update it
        if (fullName != null || profilePic != null) {

            fullName = fullName != null ? fullName.toLowerCase() : null;

            // Update user object
            User user = (User) ofy().load().key(Key.create(User.class, myUserId)).now();
            user.updateUser(fullName, profilePic);
            // Save and update
            ofy().save().entity(user); // Async Task
        }

        // Check if there is something not null in UserProfile
        if (email != null || status != null || dob != null || gender != null){

            email = email != null ? email.toLowerCase() : null;

            // Update UserProfile
            UserProfile userProfile = (UserProfile) ofy().load().key(Key.create(UserProfile.class, myUserId)).now();
            userProfile.updateProfile(email, status, dob, gender);
            // save and update
            ofy().save().entity(userProfile); // Async Task
        }


    }

    /**
     * Update user setting
     * @param userId        Id of user who want to update setting
     * @param showTagPost   How tag post show in profile Public, private or never
     * @param postTag       How can Tag a post Both, Friend, follower or no one
     * @param anonymousTag  Can anonymous person you
     */
    public TaskComplete updateSetting(@Named("userId") Long userId,
                               @Nullable @Named("showTagPost") DataType.TagApproved showTagPost,
                               @Nullable @Named("postTag") DataType.HowTag postTag,
                               @Nullable @Named("anonymousTag") Boolean anonymousTag){

        // Get UserProfile object and update setting inside it
        UserProfile userProfile = (UserProfile) ofy().load().key(Key.create(UserProfile.class, userId)).now();
        userProfile.updateSetting(showTagPost, postTag, anonymousTag);
        // Save and update
        ofy().save().entity(userProfile); // Async Task

        return new TaskComplete(true);
    }

    /**
     * Get Notifications
     * @param userId    User who want to get notification
     * @param mode      Notification mode Private, public
     * @param cursor    Cursor where to start next query, It can be null
     * @return          Formatted form in Notification object
     */
    public Notification getNotification(@Named("userId") Long userId, @Named("mode") DataType.Mode mode,
                                        @Nullable @Named("cursor") String cursor){

        // Create Notification object
        Notification notification = new Notification();

        // Create userKey from userId
        Key userKey = Key.create(User.class, userId);

        // Check Type of the Notification
        if ( mode == DataType.Mode.PRIVATE ) { // Private Notification

            // Create a list to hold PrivateNotification
            List<PrivateNotification> notifications = new ArrayList<>();

            Query<PrivateNotification> notificationQuery = ofy().load().type(PrivateNotification.class).ancestor(userKey)
                    .limit(30);

            // If cursor is set then start query from cursor
            if (cursor != null)
                notificationQuery = notificationQuery.startAt(Cursor.fromWebSafeString(cursor));

            // Create variable to check there are more item or not
            boolean moreItem = false;

            // Create iterator to loop query
            QueryResultIterator<PrivateNotification> iterator = notificationQuery.iterator();

            // Loop query and convert into list
            while (iterator.hasNext()){
                notifications.add(iterator.next());
                moreItem = true;
            }

            // If there is more items get the cursor
            if (moreItem)
                notification.setCursor(iterator.getCursor().toWebSafeString());

            // Reset Notification from UserAlert
            Datastore.updateUserAlert(userId, 0L, DataType.Alert.NOTIFICATION, true);

            // Create formatted Notifications and set into Notification object
            notification.setNotificationList(NotificationHelper.generatePrivateNotification(notifications));

        }else{ // Public Notification

            // Create a list to hold PublicNotification
            List<PublicNotification> notifications = new ArrayList<>();

            Query<PublicNotification> notificationQuery = ofy().load().type(PublicNotification.class).ancestor(userKey)
                    .limit(30);

            // If cursor is set then start query from cursor
            if (cursor != null)
                notificationQuery = notificationQuery.startAt(Cursor.fromWebSafeString(cursor));

            // Create variable to check there are more item or not
            boolean moreItem = false;

            // Create iterator to loop query
            QueryResultIterator<PublicNotification> iterator = notificationQuery.iterator();

            // Loop query and convert into list
            while (iterator.hasNext()){
                notifications.add(iterator.next());
                moreItem = true;
            }

            // If there is more items get the cursor
            if (moreItem)
                notification.setCursor(iterator.getCursor().toWebSafeString());

            // Reset Notification from UserAlert
            Datastore.updateUserAlert(userId, 0L, DataType.Alert.NOTIFICATION, true);

            // Create formatted Notifications and set into Notification object
            notification.setNotificationList(NotificationHelper.generatePublicNotification(notifications));

        } // end if-else

        return notification;
    }

    /**
     * Clear Notification or delete notification that those are old
     * @param userId                User who want to delete notification
     * @param mode                  Which notification need to remove private or public
     * @param lastNotificationId    Last notification id, notifications will be removed which is greater than or equal to this id
     */
    public void deleteNotification(@Named("userId") Long userId, @Named("mode") DataType.Mode mode,
                                   @Named("lastNotificationId") Long lastNotificationId){

        // Check which notification need to clear private or public
        if (mode == DataType.Mode.PRIVATE){ // Remove private notification

            // Get keys to remove notifications
            List<Key<PrivateNotification>> notificationKeys = ofy().load().type(PrivateNotification.class).ancestor(Key.create(User.class, userId))
                    .filterKey(">=", Key.create(PrivateNotification.class, lastNotificationId))
                    .keys().list();

            // Delete these notifications
            ofy().delete().keys(notificationKeys); // Async Task

        }else {

            // Get keys to remove notifications
            List<Key<PublicNotification>> notificationKeys = ofy().load().type(PublicNotification.class).ancestor(Key.create(User.class, userId))
                    .filterKey(">=", Key.create(PublicNotification.class, lastNotificationId))
                    .keys().list();

            // Delete these notifications
            ofy().delete().keys(notificationKeys); // Async Task

        }

    }

    /**
     * Get Update User Notification private OR public
     * @param userId    Id of the user which need to get notification
     * @param mode      Notification type Private, Public
     * @param alertId   First id of notification where to start
     * @return          A list of formatted Notifications
     */
    public Notification getUpdateNotification(@Named("userId") Long userId, @Named("mode") DataType.Mode mode, @Named("alertId") Long alertId){

        // Create Notification object
        Notification notification = new Notification();

        // Create userKey from userId
        Key userKey = Key.create(User.class, userId);

        // Check Type of the Notification
        if ( mode == DataType.Mode.PRIVATE ) { // Private Notification

            // Removing equal sign(=) from filterKey start next notification where alertId is greater
            // This will reduce the duplication in last notification But it didn't show the first notification
            // TODO THINK
            List<PrivateNotification> notifications = ofy().load().type(PrivateNotification.class).ancestor(userKey)
                    .filterKey("<=", Key.create(userKey, PrivateNotification.class,alertId))
                    .limit(10).list();

            Long lastNotificationId = 0L;

            // Check if notification are equal to 50 then maybe there are some more notifications, So, set to last notification id
            if (notifications.size() == 10) { // May be there are more notifications
                // Get last notification id from list
                lastNotificationId = notifications.get(0).getNotificationId();
            }

            // Reset Notification from UserAlert
            Datastore.updateUserAlert(userId, lastNotificationId, DataType.Alert.NOTIFICATION, true, true);

            // Create formatted Notifications
            notification.setNotificationList(NotificationHelper.generatePrivateNotification(notifications));

        }else{ // Public Notification

            List<PublicNotification> notifications = ofy().load().type(PublicNotification.class).ancestor(userKey)
                    .filterKey("<=", Key.create(userKey,PublicNotification.class,alertId))
                    .limit(10).list();

            Long lastNotificationId = 0L;

            // Check if notification are equal to 50 then maybe there are some more notifications, So, set to last notification id
            if (notifications.size() == 10) { // May be there are more notifications
                // Get last notification id from list
                lastNotificationId = notifications.get(0).getNotificationId();
            }
            // Reset Notification from UserAlert
            Datastore.updateUserAlert(userId, lastNotificationId, DataType.Alert.NOTIFICATION, false, true);

            // Create formatted Notifications
            notification.setNotificationList(NotificationHelper.generatePublicNotification(notifications));

        } // end if-else

        return notification;
    }

    /**
     * Get User Feed
     * @param userId    user who want to get feed
     * @param mode      Feed mode private or public
     * @param cursor    Cursor where to start next query, It can be null
     * @return          Formatted form of feed in PostFeed object
     */
    public PostFeed getFeed(@Named("userId") Long userId, @Named("mode") DataType.Mode mode,
                                  @Nullable @Named("cursor") String cursor){

        // Create PostFeed object
        PostFeed postFeed = new PostFeed();

        // Create userKey from userId
        Key userKey = Key.create(User.class, userId);
        // Create list to Hold PostFeed
        List<FeedItem> feedItems = new ArrayList<>();

        if (mode == DataType.Mode.PRIVATE) { // Get Private Feed
            // Get First 30 Feeds in by user parent key

            // Create a list of privateFeeds
            List<PrivateFeed> privateFeeds = new ArrayList<>();

            // Query private Feeds
            Query<PrivateFeed> privateFeedQuery = ofy().load().type(PrivateFeed.class).ancestor(userKey)
                    .limit(30);

            // If cursor is set then start query from cursor
            if (cursor != null)
                privateFeedQuery = privateFeedQuery.startAt(Cursor.fromWebSafeString(cursor));

            // Create variable to check there are more item or not
            boolean moreItem = false;

            // Create iterator to loop query
            QueryResultIterator<PrivateFeed> iterator = privateFeedQuery.iterator();

            while (iterator.hasNext()){
                privateFeeds.add(iterator.next());
                moreItem = true;
            }

            // If there is more items get the cursor
            if (moreItem)
                postFeed.setCursor(iterator.getCursor().toWebSafeString());

            // Reset user alert
            Datastore.updateUserAlert(userId, 0L, DataType.Alert.FEED, true);

            // Get Posts from list of Feed
            for (PrivateFeed privateFeed : privateFeeds) {
                // Create formatted Post Feed from Raw Post Object
                Post post = privateFeed.getPostRef().get();
                if (post != null) { // Sometime post is delete so check it's not null
                    feedItems.add(ApiHelper.FormatUserFeed(privateFeed.getFeedSafeKey(), userId, post));
                }
            } // end for loop

            // Set Feed items in PostFeed
            postFeed.setFeedList(feedItems);

        }else { // Get Public Feed
            // Get Last 5 Feeds by user parent key

            // Create a list of privateFeeds
            List<PublicFeed> publicFeeds = new ArrayList<>();

            // Query private Feeds
            Query<PublicFeed> publicFeedQuery = ofy().load().type(PublicFeed.class).ancestor(userKey)
                    .limit(30);

            // If cursor is set then start query from cursor
            if (cursor != null)
                publicFeedQuery = publicFeedQuery.startAt(Cursor.fromWebSafeString(cursor));

            // Create variable to check there are more item or not
            boolean moreItem = false;

            // Create iterator to loop query
            QueryResultIterator<PublicFeed> iterator = publicFeedQuery.iterator();

            while (iterator.hasNext()){
                publicFeeds.add(iterator.next());
                moreItem = true;
            }

            // If there is more items get the cursor
            if (moreItem)
                postFeed.setCursor(iterator.getCursor().toWebSafeString());

            // Reset user alert
            Datastore.updateUserAlert(userId, 0L, DataType.Alert.FEED, true);

            // Get Posts from list of Feed
            for (PublicFeed publicFeed : publicFeeds) {
                // Create formatted Post Feed from Raw Post Object
                Post post = publicFeed.getPostRef().get();
                if (post != null) { // Sometime post is delete so check it's not null
                    feedItems.add(ApiHelper.FormatUserFeed(publicFeed.getFeedSafeKey(), userId, post));
                }
            } // end for loop

            // Set Feed items in PostFeed
            postFeed.setFeedList(feedItems);

        }

        return postFeed;
    }

    /**
     * Delete any post from user feed. User can delete any post from his/her feed
     * @param feedSafeKey   Feed safe string that he want to delete
     */
    public void deleteFeed(@Named("feedSafeKey") String feedSafeKey){

        // Delete feed, Automatically get it is private feed or public feed
        ofy().delete().key(Key.create(feedSafeKey)); // Async Task

    }

    /**
     * Update the Feed in above direction
     * @param userId    How want to get Feed
     * @param alertId   First id of the Feed where to start
     * @param mode      Private or Public Feed
     * @return          List of Post
     */
    public PostFeed getUpdateFeed(@Named("userId") Long userId, @Named("alertId") Long alertId, @Named("mode") DataType.Mode mode){

        // Create PostFeed object
        PostFeed postFeed = new PostFeed();

        // Create userKey from userId
        Key userKey = Key.create(User.class, userId);
        // Create list to Hold PostFeed
        List<FeedItem> feedItems = new ArrayList<>();


        if (mode == DataType.Mode.PRIVATE) { // Get Private Feed
            // Get Last 5 Feeds in by user parent key
            List<PrivateFeed> privateFeeds = ofy().load().type(PrivateFeed.class).ancestor(userKey)
                    .filterKey("<=", Key.create(userKey, PrivateFeed.class, alertId))
                    .limit(5).list();

            Long lastFeedId = 0L;

            // Check if Feeds are equal to 20 then maybe there are some more feeds, So, set to last feed id
            if (privateFeeds.size() == 5) { // May be there are more feeds
                // Get last notification id from list
                lastFeedId = privateFeeds.get(0).getId();
            }
            // Reset user alert
            Datastore.updateUserAlert(userId, lastFeedId, DataType.Alert.FEED, true, true);

            // Get Posts from list of Feed
            for (PrivateFeed privateFeed : privateFeeds) {
                // Create formatted Post Feed from Raw Post Object
                Post post = privateFeed.getPostRef().get();
                if (post != null) { // Sometime post is delete so check it's not null
                    feedItems.add(ApiHelper.FormatUserFeed(privateFeed.getFeedSafeKey(), userId, post));
                }
            } // end for loop

            // Add feed item in post feed
            postFeed.setFeedList(feedItems);

        } else { // Get Public Feed
            // Get Last 5 Feeds by user parent key
            List<PublicFeed> publicFeeds = ofy().load().type(PublicFeed.class).ancestor(userKey)
                    .filterKey("<=", Key.create(userKey, PublicFeed.class, alertId))
                    .limit(5).list();

            Long lastFeedId = 0L;

            // Check if Feeds are equal to 20 then maybe there are some more feeds, So, set to last feed id
            if (publicFeeds.size() == 5) { // May be there are more feeds
                // Get last notification id from list
                lastFeedId = publicFeeds.get(0).getId();
            }
            // Reset user alert
            Datastore.updateUserAlert(userId, lastFeedId, DataType.Alert.FEED, false, true);

            // Get Posts from list of Feed
            for (PublicFeed publicFeed : publicFeeds) {
                // Create formatted Post Feed from Raw Post Object
                feedItems.add(ApiHelper.FormatUserFeed(publicFeed.getFeedSafeKey(), userId, publicFeed.getPostRef().get()));
            } // end for loop

            // Add feed item in post feed
            postFeed.setFeedList(feedItems);

        } // end If-else

        return postFeed;
    }

    /**
     * Get Stories Feed
     * @param userId    Id of user who want to get Stories feed
     * @param mode      Private or Public
     * @return          List of users that are created stories
     */
    @ApiMethod(path = "getStoriesFeed/{userId}/{mode}")
    public List<StoryFeed> getStoriesFeed(@Named("userId") Long userId, @Named("mode") DataType.Mode mode){

        // Create userKey from userId
        Key userKey = Key.create(User.class, userId);
        // Create list to hold userKeys
        List<StoryFeed> storyFeeds = new ArrayList<>();


        if (mode == DataType.Mode.PRIVATE) { // Get Private stories

            // Create list to Hold Stories that need to be removed
            List<PrivateStories> storiesRemoveList = new ArrayList<>();

            List<PrivateStories> privateStories = ofy().load().type(PrivateStories.class).ancestor(userKey)
                    .limit(30).list();

            // Loop each stories to check it is still exist or not
            for (PrivateStories privateStory: privateStories){

                Story story = privateStory.getStoryRef().get();
                // Check story is exist or not
                if (story != null){ // Story still exist
                    // Add user key in list
                    storyFeeds.add(ApiHelper.FormatUserStory(story));

                }else{ // Story not exist
                    storiesRemoveList.add(privateStory);
                } // end if else

            } // end for loop

            // Remove stories from feed which are not exist anymore
            if (storiesRemoveList.size() > 0){
                ofy().delete().entities(storiesRemoveList); // Async Task
            } // end if

        }else{ // Get Public stories

            // Create list to Hold Stories that need to be removed
            List<PublicStories> storiesRemoveList = new ArrayList<>();

            List<PublicStories> publicStories = ofy().load().type(PublicStories.class).ancestor(userKey)
                    .limit(30).list();

            // Loop each stories to check it is still exist or not
            for (PublicStories publicStory: publicStories){

                Story story = publicStory.getStoryRef().get();
                // Check story is exist or not
                if (story != null){ // Story still exist
                    // Add user key in list
                    storyFeeds.add(ApiHelper.FormatUserStory(story));

                }else{ // Story not exist
                    storiesRemoveList.add(publicStory);
                } // end if else

            } // end for loop

            // Remove stories from feed which are not exist anymore
            if (storiesRemoveList.size() > 0){
                ofy().delete().entities(storiesRemoveList); // Async Task
            } // end if
        } // end if-else

        return storyFeeds;
    }

    /**
     * Get All User post Public and Private
     * @param userId    Who want to get post
     * @param postMode  Post mode, it can be private, public or null. Null when you want to get all posts
     * @param cursor    Cursor where to start next query, It can be null
     * @return          Formatted form of post
     */
    @ApiMethod(path = "getUserPost/{userId}")
    public PostFeed getUserPost(@Named("userId") Long userId, @Nullable @Named("postMode") DataType.Mode postMode,
                                @Nullable @Named("cursor") String cursor){

        return fetchUserPost(userId, postMode, cursor);
    }

    /**
     * Get All User post Public and Private
     * @param userId    Who want to get post
     * @param postMode  Post mode, it can be private, public or null. Null when you want to get all posts
     * @param cursor    Cursor where to start next query, It can be null
     * @return          Formatted form of post
     */
    private static PostFeed fetchUserPost(Long userId, DataType.Mode postMode, String cursor){
        // Create PostFeed object
        PostFeed postFeed = new PostFeed();

        // Hold list of Feed item
        List<FeedItem> feedItems = new ArrayList<>();

        // Create a list to hold user posts;
        List<Post> posts = new ArrayList<>();

        // Create a post query
        Query<Post> postQuery = null;

        if (postMode == null){ // No mode is set get all posts private as well as public
            // Get Last 20 User Post by user parent key
            postQuery = ofy().load().type(Post.class).ancestor(Key.create(User.class, userId))
                    .limit(20);
        }else if (postMode == DataType.Mode.PRIVATE){
            postQuery = ofy().load().type(Post.class).ancestor(Key.create(User.class, userId))
                    .filter("mode", DataType.Mode.PRIVATE)
                    .limit(20);
        }else if (postMode == DataType.Mode.PUBLIC){
            postQuery = ofy().load().type(Post.class).ancestor(Key.create(User.class, userId))
                    .filter("mode", DataType.Mode.PUBLIC)
                    .limit(20);
        }

        // Check post query is not null
        if (postQuery != null) {

            // If cursor is not null then start it from cursor
            if (cursor != null)
                postQuery = postQuery.startAt(Cursor.fromWebSafeString(cursor));

            // Create variable to check there are more item or not
            boolean moreItem = false;

            // Create iterator to loop query
            QueryResultIterator<Post> iterator = postQuery.iterator();

            // Loop query and convert into list
            while (iterator.hasNext()) {
                posts.add(iterator.next());
                moreItem = true;
            }

            // If there is more items get the cursor
            if (moreItem)
                postFeed.setCursor(iterator.getCursor().toWebSafeString());

            // Get Posts from list of Feed
            for (Post post : posts) {
                // Create formatted Post Feed from Raw Post Object
                feedItems.add(ApiHelper.FormatUserFeed(userId, post));
            } // end for loop

            // Add Feed items in PostFeed
            postFeed.setFeedList(feedItems);

        } // end if

        return postFeed;
    }

    /**
     * Get user saved post
     * @param userId    Who want to get saved post
     * @param cursor    Cursor where to start next query, It can be null
     * @return          Formatted form of saved post
     */
    public PostFeed getSavedPost(@Named("userId") Long userId, @Nullable @Named("cursor") String cursor){

        // Create a post feed object
        PostFeed postFeed = new PostFeed();

        // Hold list of Post
        List<FeedItem> feedItems = new ArrayList<>();

        // Create a list to hold save posts
        List<SavedPost> savedPosts = new ArrayList<>();

        // Get Last 20 Saved Post by user parent key
        Query<SavedPost> savedPostQuery = ofy().load().type(SavedPost.class).ancestor(Key.create(User.class, userId))
                .limit(20);

        // If cursor is set then start query from cursor
        if (cursor != null)
            savedPostQuery = savedPostQuery.startAt(Cursor.fromWebSafeString(cursor));

        // Create variable to check there are more item or not
        boolean moreItem = false;

        // Create iterator to loop query
        QueryResultIterator<SavedPost> iterator = savedPostQuery.iterator();

        // Loop query and convert into list
        while (iterator.hasNext()){
            savedPosts.add(iterator.next());
            moreItem = true;
        }

        // If there is more items get the cursor
        if (moreItem)
            postFeed.setCursor(iterator.getCursor().toWebSafeString());

        // Get Posts from list of Feed
        for (SavedPost savedPost : savedPosts) {

            // Check the post is not null
            Post post = savedPost.getPostRef().get();

            if(post != null) { // Create Feed for this post

                // Create formatted Post Feed from Raw Post Object
                feedItems.add(ApiHelper.FormatUserFeed(userId, post));

            }

        } // end for loop

        // Add FeedItems in PostFeed
        postFeed.setFeedList(feedItems);

        return postFeed;
    }

    /**
     * Get User Friend Requests
     * @param userId    Id of the User which need to get Friend Requests
     * @return  List of formatted user friend request into UserFriendRequest
     */
    public List<UserFriendRequest> getFriendRequest(@Named("userId") Long userId){

        // Create userKey from userId
        Key userKey = Key.create(User.class, userId);

        // Get All Friends request where this user is parent
        List<FriendRequest> friendRequests = ofy().load().type(FriendRequest.class).ancestor(userKey).list();

        // Create UserFriendRequest list to Hold formatted request
        List<UserFriendRequest> friendRequestList = new ArrayList<>();

        // Loop each friend request to create formatted Request
        for ( FriendRequest friendRequest : friendRequests ) {

            // Convert each request into formatted UserFriendRequest
            User friend = friendRequest.getFriendRef().get();
            friendRequestList.add( new UserFriendRequest(
                    friendRequest.getId(),
                    friend.getUserId(),
                    Utils.bucketURL +friend.getProfilePic(),
                    friend.getBadge(),
                    friend.getFullName(),
                    friendRequest.getPhone()) );

        } // end for loop

        // return list of formatted friend requests
        return friendRequestList;
    }

    /**
     * Get List of Friends
     * @param userId    Id of user who want to get list of friend
     * @param relation  Type of the relation e.g Friend, Follower, Following, Block Friend
     * @return          List of friend (User)
     */
    public List<User> getRelation(@Named("userId") Long userId, @Named("relation") DataType.Relation relation){

        // Create list to hold userKeys
        List<Key> userKeys = new ArrayList<>();

        // Check what kind of relation
        switch (relation) {
            case FRIEND:
                // Get friend list and convert into userKeys
                for (Friend friend : UserHelper.getFriendList(userId, 30)) {
                    userKeys.add(Key.create(User.class, friend.getFriendId()));
                } // end for loop
                break;
            case FOLLOWER:
                // Get follower list and convert into userKeys
                for (Follower follower : UserHelper.getFollowerList(userId, 30)) {
                    userKeys.add(Key.create(User.class, follower.getFollowerId()));
                } // end for loop
                break;
            case FOLLOWING:
                // Get following list and convert into userKeys
                for (Following following : UserHelper.getFollowingList(userId, 30)) {
                    userKeys.add(Key.create(User.class, following.getFollowingId()));
                } // end for loop
                break;
            case BLOCK_FRIEND:
                // Get block friend list and convert into userKeys
                for (BlockFriend blockFriend : UserHelper.getBlockedFriendList(userId)) {
                    userKeys.add(Key.create(User.class, blockFriend.getFriendId()));
                } // end for loop
                break;

        } // end switch case

        // Batch Load and return
        if(userKeys.size() > 0) {
            return ofy().load().type(User.class).filterKey("in", userKeys).list();
        }else{
            return null;
        }
    }

    /**
     * Discover post for user (User suggestion)
     * @param userId    Who to discover
     * @param offset    Where to start post
     * @return          Formatted form of Post (PostFeed)
     */
    public PostFeed discover(@Named("userId") Long userId, @Named("offset") int offset){

        // Create PostFeed object
        PostFeed postFeed = new PostFeed();

        // Set the limit for post
        int limit = 30;
        // Create Array to hold postIds
        List<String> postIds = new ArrayList<>();
        // Create list to hold post keys
        List<Key> postKeys = new ArrayList<>();
        // Create list to hold formatted post feed
        List<FeedItem> feedItems = new ArrayList<>();
        UserSuggestion userSuggestion = null;

        try {
            // Check suggestion is available for this user or not
            userSuggestion = (UserSuggestion) ofy().load().key(Key.create(UserSuggestion.class, userId)).safe();
        } catch (NotFoundException e) {
            e.printStackTrace();

            return null;
        }

        if(userSuggestion.getPostIds() != null) { // suggestion is available
            // check if postIds are greater than 30
            if(userSuggestion.getPostIds().length > 30) {
                // split postIds array
                postIds = Arrays.asList(Arrays.copyOfRange(userSuggestion.getPostIds(), offset, offset + limit));
            }else{
                postIds = Arrays.asList(userSuggestion.getPostIds());
            }

            // convert postIds into post keys
            for ( String postId : postIds ) {
                postKeys.add( Key.create(postId) );
            } // end for loop

            // Get a list of post
            List<Post> postList = ofy().load().type(Post.class).filterKey("in", postKeys).list();


            if (postList.size() > 0) {
                // Format each post into PostFeed
                for(Post post : postList) {
                    feedItems.add( ApiHelper.FormatUserFeed(userId, post) );
                } // end for loop
            }

            // Add feedItems in PostFeed
            postFeed.setFeedList(feedItems);

            return postFeed;
        } // end if


        return null;
    }

    /**
     * Generate suggestion post for user, This function only called once a day
     * @param userId  Id of the user who want to create suggestion
     */
    public void generateUserSuggestion(@Named("userId") Long userId){

        // Create Push Queue for making suggestion
        Queue userSuggestion = QueueFactory.getQueue("user-suggestion");
        userSuggestion.add(TaskOptions.Builder.withUrl("/queue/suggestion_queue")
                .param("userId", String.valueOf(userId)));
    }

}

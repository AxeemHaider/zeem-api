package org.octabyte.zeem.API.Helper;

import org.octabyte.zeem.API.Entity.NotificationItem;
import org.octabyte.zeem.Datastore.Post;
import org.octabyte.zeem.Datastore.PrivateNotification;
import org.octabyte.zeem.Datastore.PublicNotification;
import org.octabyte.zeem.Datastore.User;
import org.octabyte.zeem.Helper.DataType;
import org.octabyte.zeem.Helper.Utils;

import java.util.ArrayList;
import java.util.List;

public class NotificationHelper {

    /**
     * Generate notification title for firebase push notification
     * @param notificationType  Type of notification
     * @return                  String title for notification
     */
    public static String getNotificationTitle(DataType.NotificationType notificationType){
        // Check what type of the notification it is
        switch (notificationType){
            case POST_LIKE:
                return "Like Post";
            case POST_COMMENT:
                return "Comment on post";
            case TAG_POST:
                return "Tag in post";
            case LIST_POST:
                return "Circle Post";
            case POST_MENTION:
                return "Mention in post";
            case COMMENT_MENTION:
                return "Mention in comment";
            case FOLLOWER:
                return "Follower";
            case FRIEND_REQUEST:
                return "Friend Request";
            case NEW_FRIEND:
                return "New Friend";
            case POST_DELETE:
                return "Post deleted";
            case COMMENT_LIKE:
                return "Like Comment";
            case REPORT_OTHER:
            case REPORT_ANTI_RELIGION:
            case REPORT_SEXUAL_CONTENT:
            case REPORT_SPAM:
                return "Violation of terms";

        }

        return "Notification from ZEEM";
    }

    /**
     * Format Notification text, text of notification that show to the user
     * @param notificationType  Type of the notification
     * @param fullName          name of user how perform any action
     * @return                  format form of text that display to user
     */
    public static String formatNotificationText(DataType.NotificationType notificationType, String fullName){

        String Like_Post = "[%s] liked your post";
        String Post_Comment = "[%s] comment on post";
        String Tag_Post = "You are tagged with [%s] post";
        String List_Post = "You are tagged in a Post By [%s]";
        String Post_Mention = "[%s] Mentioned you in post";
        String Comment_Mention = "[%s] Mentioned you in comment";
        String Follower = "[%s] start following you";
        String Friend_Request = "[%s] wants to become a friend";
        String New_Friend = "You and [%s] are Friends now";
        String Post_Delete = "Your post is deleted due to violation of terms and condition. Please read the terms and condition and follow them otherwise your account will be blocked. If you have further queries feel free to contact us";
        String Comment_Like = "[%s] like your comment";
        String Report_spam = "Someone reported about your post, that it is containing a Spam content, Continuous reporting about your posts can block your account. Please read terms and conditions. If you have further queries, feel free to contact us. ";
        String Report_anti_religion = "Someone reported about your post, that it is against any religion, Continuous reporting about your posts can block your account. Please read terms and conditions. If you have further queries, feel free to contact us. ";
        String Report_sexual_content = "Someone reported about your post, that it is containing a Sexual content, Continuous reporting about your posts can block your account. Please read terms and conditions. If you have further queries, feel free to contact us. ";
        String Report_other = "Someone reported about your post, that your post is violating terms and condition, Continuous reporting about your posts can block your account. Please read terms and conditions. If you have further queries, feel free to contact us. ";

        // Check what type of the notification it is
        switch (notificationType){
            case POST_LIKE:
                return String.format(Like_Post, Utils.capitalize(fullName));
            case POST_COMMENT:
                return String.format(Post_Comment, Utils.capitalize(fullName));
            case TAG_POST:
                return String.format(Tag_Post, Utils.capitalize(fullName));
            case LIST_POST:
                return String.format(List_Post, Utils.capitalize(fullName));
            case POST_MENTION:
                return String.format(Post_Mention, Utils.capitalize(fullName));
            case COMMENT_MENTION:
                return String.format(Comment_Mention, Utils.capitalize(fullName));
            case FOLLOWER:
                return String.format(Follower, Utils.capitalize(fullName));
            case FRIEND_REQUEST:
                return String.format(Friend_Request, Utils.capitalize(fullName));
            case NEW_FRIEND:
                return String.format(New_Friend, Utils.capitalize(fullName));
            case POST_DELETE:
                return Post_Delete;
            case COMMENT_LIKE:
                return String.format(Comment_Like, Utils.capitalize(fullName));
            case REPORT_SPAM:
                return Report_spam;
            case REPORT_ANTI_RELIGION:
                return Report_anti_religion;
            case REPORT_SEXUAL_CONTENT:
                return Report_sexual_content;
            case REPORT_OTHER:
                return Report_other;

        }

        return fullName;
    }

    /**
     * Get Private Notification list from Datastore
     * @param notifications list of notification
     * @return  formatted list of notification
     */
    public static List<NotificationItem> generatePrivateNotification(List<PrivateNotification> notifications){

        // Create list of Notification
        List<NotificationItem> notificationItems = new ArrayList<>();

        // Hold notification item
        NotificationItem notificationItem;

        // loop for each notification
        for ( PrivateNotification notification : notifications ) {

            // Check Type of the notification
            switch (notification.getType()){

                case POST_LIKE:
                case POST_COMMENT:
                case TAG_POST:
                case LIST_POST:
                case POST_MENTION:
                case COMMENT_MENTION:
                case REPORT_SPAM:
                case REPORT_ANTI_RELIGION:
                case REPORT_SEXUAL_CONTENT:
                case REPORT_OTHER:

                    // Generate new Notification and add it in notificationList
                    notificationItem = formatPrivateNotification(notification, notification.getType(), true);
                    // Before adding notification check it's not null
                    if (notificationItem != null)
                        notificationItems.add( notificationItem );
                    break;

                case FOLLOWER:
                case FRIEND_REQUEST:
                case NEW_FRIEND:
                case COMMENT_LIKE:
                case POST_DELETE:

                    notificationItem = formatPrivateNotification(notification, notification.getType(), false);
                    // Before adding notification check it's not null
                    if (notificationItem != null)
                    notificationItems.add( notificationItem );
                    break;

            }

        }

        return notificationItems;
    }

    /**
     * Get Public Notification list from Datastore
     * @param notifications list of notification
     * @return  formatted list of notification item
     */
    public static List<NotificationItem> generatePublicNotification(List<PublicNotification> notifications){

        // Create list of Notification
        List<NotificationItem> notificationItems = new ArrayList<>();

        // Hold notification item
        NotificationItem notificationItem;

        // loop for each notification
        for ( PublicNotification notification : notifications ) {

            // Check Type of the notification
            switch (notification.getType()){

                case POST_LIKE:
                case POST_COMMENT:
                case TAG_POST:
                case LIST_POST:
                case POST_MENTION:
                case COMMENT_MENTION:
                case REPORT_SPAM:
                case REPORT_ANTI_RELIGION:
                case REPORT_SEXUAL_CONTENT:
                case REPORT_OTHER:

                    // Generate new Notification and add it in notificationList
                    notificationItem = formatPublicNotification(notification, notification.getType(), true);
                    // Before adding notification check it's not null
                    if (notificationItem != null)
                        notificationItems.add( notificationItem );
                    break;

                case FOLLOWER:
                case FRIEND_REQUEST:
                case NEW_FRIEND:
                case COMMENT_LIKE:
                case POST_DELETE:

                    notificationItem = formatPublicNotification(notification, notification.getType(), false);
                    // Before adding notification check it's not null
                    if (notificationItem != null)
                        notificationItems.add( notificationItem );
                    break;

            }

        }

        return notificationItems;
    }

    /**
     * Generate Notification from PrivateNotification
     * @param notification private notification object
     * @param type Notification type
     * @param withPost  When post pic is required
     * @return  Formatted NotificationItem object
     */
    private static NotificationItem formatPrivateNotification(PrivateNotification notification, DataType.NotificationType type, Boolean withPost){

        if (withPost) { // When post pic is required in notification

            // Check post is exist or not
            if (notification.getPostRef().get() != null) { // Post exist
                User user = notification.getUserRef().get();
                Post post = notification.getPostRef().get();
                String postPic;

                // Check what is the type to post and set postPic according to it
                // IMAGE post does not contain any cover that's why set source to it
                // AUDIO and CARD post doesn't contains any photo that'w why set AUDIO and CARD to handle on android
                switch (post.getType()) {
                    case IMAGE:
                        postPic = Utils.bucketURL + post.getSource();
                        break;
                    case AUDIO:
                        postPic = "AUDIO";
                        break;
                    case CARD:
                        postPic = "CARD";
                        break;
                    default:
                        postPic = Utils.bucketURL + post.getCover();
                        break;
                }

                // If notification is anonymous then profile pic set Anonymous and set full name Anonymous
                String userProfilePic = "Anonymous";
                String userFullName = "Anonymous Person";

                if (!notification.getAnonymous()){ // notification is not anonymous set original pic and original name
                    userProfilePic = Utils.bucketURL + user.getProfilePic();
                    userFullName = user.getFullName();
                }

                return (new NotificationItem(
                        notification.getNotificationId(),
                        user.getUserId(),
                        userProfilePic,
                        user.getBadge(),
                        formatNotificationText(type, userFullName),
                        type,
                        post.getWebSafeKey(),
                        postPic
                ));
            }else{
                return null;
            }

        }else{ // Post pic is not required
            User user = notification.getUserRef().get();
            return (new NotificationItem(
                    notification.getNotificationId(),
                    user.getUserId(),
                    Utils.bucketURL +user.getProfilePic(),
                    user.getBadge(),
                    formatNotificationText(type, user.getFullName()),
                    type
            ));
        }
    }

    /**
     * Generate Notification from PublicNotification
     * @param notification private notification object
     * @param type Notification type
     * @param withPost  When post pic is required
     * @return  Formatted NotificationItem object
     */
    private static NotificationItem formatPublicNotification(PublicNotification notification, DataType.NotificationType type, Boolean withPost){

        if (withPost) { // When post pic is required in notification

            // Check post is exist or not
            if (notification.getPostRef().get() != null) { // Post exist
                User user = notification.getUserRef().get();
                Post post = notification.getPostRef().get();
                String postPic;

                // Check what is the type to post and set postPic according to it
                // IMAGE post does not contain any cover that's why set source to it
                // AUDIO and CARD post doesn't contains any photo that'w why set AUDIO and CARD to handle on android
                switch (post.getType()) {
                    case IMAGE:
                        postPic = Utils.bucketURL + post.getSource();
                        break;
                    case AUDIO:
                        postPic = "AUDIO";
                        break;
                    case CARD:
                        postPic = "CARD";
                        break;
                    default:
                        postPic = Utils.bucketURL + post.getCover();
                        break;
                }

                // If notification is anonymous then profile pic set Anonymous and set full name Anonymous
                String userProfilePic = "Anonymous";
                String userFullName = "Anonymous Person";

                if (!notification.getAnonymous()){ // notification is not anonymous set original pic and original name
                    userProfilePic = Utils.bucketURL + user.getProfilePic();
                    userFullName = user.getFullName();
                }

                return (new NotificationItem(
                        notification.getNotificationId(),
                        user.getUserId(),
                        userProfilePic,
                        user.getBadge(),
                        formatNotificationText(type, userFullName),
                        type,
                        post.getWebSafeKey(),
                        postPic
                ));
            }else{
                return null;
            }

        }else{ // Post pic is not required
            User user = notification.getUserRef().get();
            return (new NotificationItem(
                    notification.getNotificationId(),
                    user.getUserId(),
                    Utils.bucketURL +user.getProfilePic(),
                    user.getBadge(),
                    formatNotificationText(type, user.getFullName()),
                    type
            ));
        }
    }

}

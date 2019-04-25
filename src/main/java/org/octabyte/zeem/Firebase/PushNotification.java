package org.octabyte.zeem.Firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import com.googlecode.objectify.Key;
import org.octabyte.zeem.API.Helper.NotificationHelper;
import org.octabyte.zeem.Datastore.User;
import org.octabyte.zeem.Helper.DataType;
import org.octabyte.zeem.Helper.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.octabyte.zeem.Helper.OfyService.ofy;

public class PushNotification {

    static {

        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .setProjectId("octabyte-zeem")
                    .setDatabaseUrl("https://octabyte-zeem.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public PushNotification() {
    }

    // Over loading function send
    public static void send(Long receiverId, Long userId, DataType.NotificationType type, String mode){
        send(receiverId, userId, type, mode, "Post Key not available", false);
    }

    // Over loading function send
    public static void send(Long receiverId, Long userId, DataType.NotificationType type, String mode, String postSafeKey){
        send(receiverId, userId, type, mode, postSafeKey, false);
    }

    /**
     * Send push notification to user mobile app
     * @param receiverId  Id of that user to whom you want to send notification
     * @param userId    Id of that user how perform any action e.g like a post, friend request
     * @param type  Custom Data type for Notification
     * @param mode  Mode of notification PRIVATE | PUBLIC
     * @param postSafeKey   Post safe key if this notification is related to post
     */
    public static void send(Long receiverId, Long userId, DataType.NotificationType type, String mode, String postSafeKey, boolean anonymous){

        // Get receiver user from receiver id
        User receiver = (User) ofy().load().key(Key.create(User.class, receiverId)).now();
        // Get fireBase token of receiver
        String firebaseToken = receiver.getFirebaseToken();

        // make sure firebase token is not null
        if (firebaseToken == null){ // Firebase token is null end this function
            return;
        }

        // Get user how did preform any action
        User user = (User) ofy().load().key(Key.create(User.class, userId)).now();

        // Create extra data to send mobile app
        Map<String, String> dataMap = new HashMap<>();
            dataMap.put("notificationType", String.valueOf(type));
            dataMap.put("profilePic", user.getProfilePic());
            dataMap.put("userId", String.valueOf(userId));
            dataMap.put("mode", mode);
            dataMap.put("postSafeKey", postSafeKey);
            dataMap.put("anonymous", String.valueOf(anonymous));

         // Check notification is anonymous or not
        // If anonymous set user full name to anonymous
        String userFullName = user.getFullName();

        if (anonymous){ // Notification is anonymous set name as Anonymous
            userFullName = "Anonymous Person";
        }

        String notificationBody = NotificationHelper.formatNotificationText(type, userFullName);
        notificationBody = notificationBody.replace("[", "");
        notificationBody = notificationBody.replace("]","");

        // Create a message for push Notification
        try {

            Message message = Message.builder()
                    .setAndroidConfig(AndroidConfig.builder()
                            .setPriority(AndroidConfig.Priority.NORMAL)
                            .setNotification(AndroidNotification.builder()
                                    .setTitle(NotificationHelper.getNotificationTitle(type))
                                    .setBody(notificationBody)
                                    .setIcon("notification_icon")
                                    .build())
                            .build())
                    .setToken(firebaseToken)
                    .putAllData(dataMap)
                    .build();


            FirebaseMessaging.getInstance().sendAsync(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * This function is used for instant chat
     * @param chatMessage   ChatMessage object contain all the information about message where and what to send
     * @return Boolean      Return true if message successfully send else return false in case of any error
     */
    public static Boolean sendChatMessage(ChatMessage chatMessage){

        // Check if profile pic is not null then remove bucket url from profile pic
        if (chatMessage.getProfilePic() != null) {
            String profilePic = chatMessage.getProfilePic().replace(Utils.bucketURL, "");
            chatMessage.setProfilePic(profilePic);
        }

        // Capitalize the username
        if (chatMessage.getUsername() != null){
            chatMessage.setUsername(Utils.capitalize(chatMessage.getUsername()));

            // If no chat title is set then set username as a chat title
            if (chatMessage.getChatTitle() == null){
                chatMessage.setChatTitle(Utils.capitalize(chatMessage.getUsername()));
            }
        }

        // Check chat is anonymous or not if anonymous then hide username and profile pic
        if (Boolean.valueOf(chatMessage.getIsAnonymous())){
            chatMessage.setUsername("Anonymous Person");
            chatMessage.setProfilePic("anonymous");
        }

        // Create extra data to send mobile app
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("senderToken", chatMessage.getSenderToken());
        dataMap.put("messageId", String.valueOf(chatMessage.getMessageId()));
        dataMap.put("messageText", chatMessage.getMessageText());

        if (chatMessage.getProfilePic() != null)    dataMap.put("profilePic", chatMessage.getProfilePic());

        if (chatMessage.getUsername() != null)  dataMap.put("userFullName", chatMessage.getUsername());

        dataMap.put("anonymous", chatMessage.getIsAnonymous());
        dataMap.put("notificationType", "INSTANT_CHAT");
        dataMap.put("instantChatMessageInfo", chatMessage.getMessageReceived());
        dataMap.put("chatAnonymous", chatMessage.getChatAnonymous());
        dataMap.put("instantChat", String.valueOf(chatMessage.isInstantChat()));

        // Create a message for push Notification which is used as a instant chat message
        try {

            Message message = Message.builder()
                    .setAndroidConfig(AndroidConfig.builder()
                            .setPriority(AndroidConfig.Priority.NORMAL)
                            .setNotification(AndroidNotification.builder()
                                    .setTitle(chatMessage.getChatTitle())
                                    .setBody(chatMessage.getMessageText())
                                    .setIcon("notification_chat")
                                    .build())
                            .build())
                    .setToken(chatMessage.getReceiverToken())
                    .putAllData(dataMap)
                    .build();


            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

}

package org.octabyte.zeem.API.Entity;

import org.octabyte.zeem.Helper.DataType;

public class NotificationItem {

    private Long notificationId;
    private Long userId;
    private String userPic;
    private int userBadge;
    private String notification;
    private DataType.NotificationType type;
    private String postSafeKey;
    private String postPic;

    public NotificationItem() {
    }

    public NotificationItem(Long notificationId, Long userId, String userPic, int userBadge, String notification, DataType.NotificationType type) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.userPic = userPic;
        this.userBadge = userBadge;
        this.notification = notification;
        this.type = type;
    }

    public NotificationItem(Long notificationId, Long userId, String userPic, int userBadge, String notification, DataType.NotificationType type, String postSafeKey, String postPic) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.userPic = userPic;
        this.userBadge = userBadge;
        this.notification = notification;
        this.type = type;
        this.postSafeKey = postSafeKey;
        this.postPic = postPic;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPostSafeKey() {
        return postSafeKey;
    }

    public void setPostSafeKey(String postSafeKey) {
        this.postSafeKey = postSafeKey;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public int getUserBadge() {
        return userBadge;
    }

    public void setUserBadge(int userBadge) {
        this.userBadge = userBadge;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public DataType.NotificationType getType() {
        return type;
    }

    public void setType(DataType.NotificationType type) {
        this.type = type;
    }

    public String getPostPic() {
        return postPic;
    }

    public void setPostPic(String postPic) {
        this.postPic = postPic;
    }

}

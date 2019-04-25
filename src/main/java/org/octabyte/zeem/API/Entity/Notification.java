package org.octabyte.zeem.API.Entity;

import java.util.List;

public class Notification {

    private String cursor;
    private List<NotificationItem> notificationList;

    public Notification() {
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public List<NotificationItem> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<NotificationItem> notificationList) {
        this.notificationList = notificationList;
    }


}

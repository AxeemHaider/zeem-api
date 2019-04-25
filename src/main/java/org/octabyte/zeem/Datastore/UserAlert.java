package org.octabyte.zeem.Datastore;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class UserAlert {

    @Id private Long userId;
    private Long privateFeedId, privateNotificationId, publicFeedId, publicNotificationId;

    public UserAlert() {
    }

    public UserAlert(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getPublicFeedId() {
        return publicFeedId;
    }

    public void setPublicFeedId(Long publicFeedId) {
        this.publicFeedId = publicFeedId;
    }

    public Long getPublicNotificationId() {
        return publicNotificationId;
    }

    public void setPublicNotificationId(Long publicNotificationId) {
        this.publicNotificationId = publicNotificationId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPrivateFeedId() {
        return privateFeedId;
    }

    public void setPrivateFeedId(Long privateFeedId) {
        this.privateFeedId = privateFeedId;
    }

    public Long getPrivateNotificationId() {
        return privateNotificationId;
    }

    public void setPrivateNotificationId(Long privateNotificationId) {
        this.privateNotificationId = privateNotificationId;
    }
}

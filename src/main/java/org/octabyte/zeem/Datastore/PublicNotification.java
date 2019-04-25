package org.octabyte.zeem.Datastore;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;
import org.octabyte.zeem.Helper.DataType;

@Entity
public class PublicNotification {

    @Id private Long notificationId; // Create from TimeStamp
    @Parent private Key<User> userKey;
    @Load private Ref<Post> postRef;
    @Load private Ref<User> userRef; // User how perform action e.g id of that user how like the post
    private DataType.NotificationType type; // 1-Post, 2-Friend Request, 3-Follower, 4-Simple, 5-List Post, 6-Tag Post
    private Boolean anonymous = false;

    public PublicNotification() {
    }

    public PublicNotification(Long notificationId, Key<User> userKey, Ref<User> userId, DataType.NotificationType type) {
        this.notificationId = notificationId;
        this.userKey = userKey;
        this.userRef = userId;
        this.type = type;
    }

    public PublicNotification(Long notificationId, Key<User> userKey, Ref<User> userId, DataType.NotificationType type, Ref<Post> postId, Boolean anonymous) {
        this.notificationId = notificationId;
        this.userKey = userKey;
        this.postRef = postId;
        this.userRef = userId;
        this.type = type;
        this.anonymous = anonymous;
    }

    public PublicNotification(Long notificationId, Key<User> userKey, Ref<User> userId, DataType.NotificationType type, Ref<Post> postId) {
        this.notificationId = notificationId;
        this.userKey = userKey;
        this.postRef = postId;
        this.userRef = userId;
        this.type = type;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Key<User> getUserKey() {
        return userKey;
    }

    public void setUserKey(Key<User> userKey) {
        this.userKey = userKey;
    }

    public Ref<Post> getPostRef() {
        return postRef;
    }

    public void setPostRef(Ref<Post> postRef) {
        this.postRef = postRef;
    }

    public Ref<User> getUserRef() {
        return userRef;
    }

    public void setUserRef(Ref<User> userRef) {
        this.userRef = userRef;
    }

    public DataType.NotificationType getType() {
        return type;
    }

    public void setType(DataType.NotificationType type) {
        this.type = type;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }
}
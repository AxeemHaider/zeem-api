package org.octabyte.zeem.Datastore;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class FriendRequest {

    @Id private Long id; // Create from TimeStamp (Removed)
    @Parent private Key<User> userKey;
    @Load private Ref<User> friendRef; // This is a user id
    private Long phone;

    public FriendRequest() {
    }

    public FriendRequest(Long id, Key<User> userKey, Ref<User> friendId, Long phone) {
        this.id = id;
        this.userKey = userKey;
        this.friendRef = friendId;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Key<User> getUserKey() {
        return userKey;
    }

    public void setUserKey(Key<User> userKey) {
        this.userKey = userKey;
    }

    public Ref<User> getFriendRef() {
        return friendRef;
    }

    public void setFriendRef(Ref<User> friendRef) {
        this.friendRef = friendRef;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }
}

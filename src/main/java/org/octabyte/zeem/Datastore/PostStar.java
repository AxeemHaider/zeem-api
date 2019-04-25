package org.octabyte.zeem.Datastore;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class PostStar {

    @Id private Long id; // Manual created with timestamp
    @Parent private Key<User> userKey;
    @Index private String postSafeKey;
    @Index private Long userId;

    public PostStar() {
    }

    public PostStar(Long id, Key<User> userKey, String postSafeKey) {
        this.id = id;
        this.userKey = userKey;
        this.postSafeKey = postSafeKey;
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

    public String getPostSafeKey() {
        return postSafeKey;
    }

    public void setPostSafeKey(String postSafeKey) {
        this.postSafeKey = postSafeKey;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

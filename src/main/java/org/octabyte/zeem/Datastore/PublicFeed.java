package org.octabyte.zeem.Datastore;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.*;

@Entity
public class PublicFeed {

    @Id private Long id; // Create from TimeStamp
    @Parent private Key<User> userKey;
    @Load private Ref<Post> postRef;
    @Ignore private String feedSafeKey;

    public PublicFeed() {
    }

    public PublicFeed(Long id, Key<User> userKey, Ref<Post> postRef) {
        this.id = id;
        this.userKey = userKey;
        this.postRef = postRef;
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

    public Ref<Post> getPostRef() {
        return postRef;
    }

    public void setPostRef(Ref<Post> postRef) {
        this.postRef = postRef;
    }

    public String getFeedSafeKey() {
        return Key.create(this.userKey, PrivateFeed.class, this.id).getString();
    }

    public void setFeedSafeKey(String feedSafeKey) {
        this.feedSafeKey = feedSafeKey;
    }
}

package org.octabyte.zeem.Datastore;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class SavedPost {

    @Id private Long id; // Create from TimeStamp
    @Parent private Key<User> userKey;
    @Load private Ref<Post> postRef;

    public SavedPost() {
    }

    public SavedPost(Long id, Key<User> userKey, Ref<Post> postRef) {
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
}

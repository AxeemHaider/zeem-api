package org.octabyte.zeem.Datastore;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.*;

@Entity
public class PrivateStories {

    @Id Long id;
    @Parent Key<User> userKey;
    @Index @Load Ref<Story> storyRef;

    public PrivateStories() {
    }

    public PrivateStories(Key<User> userKey, Ref<Story> storyRef) {
        this.userKey = userKey;
        this.storyRef = storyRef;
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

    public Ref<Story> getStoryRef() {
        return storyRef;
    }

    public void setStoryRef(Ref<Story> storyRef) {
        this.storyRef = storyRef;
    }
}

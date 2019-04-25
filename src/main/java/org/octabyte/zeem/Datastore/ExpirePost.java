package org.octabyte.zeem.Datastore;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class ExpirePost {

    @Id private Long id;
    @Index private Long expireOn; // Time in milli sec
    private String postSafeKey;

    public ExpirePost() {
    }

    public ExpirePost(Long expireOn, String postSafeKey) {
        this.expireOn = expireOn;
        this.postSafeKey = postSafeKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExpireOn() {
        return expireOn;
    }

    public void setExpireOn(Long expireOn) {
        this.expireOn = expireOn;
    }

    public String getPostSafeKey() {
        return postSafeKey;
    }

    public void setPostSafeKey(String postSafeKey) {
        this.postSafeKey = postSafeKey;
    }
}

package org.octabyte.zeem.Datastore;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class PostTag {

    @Id private Long id;
    @Parent private Key<Post> postKey;
    private Long userId;
    private Boolean tagApproved = false;

    public PostTag() {
    }

    public PostTag(Key<Post> postKey, Long userId) {
        this.postKey = postKey;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Key<Post> getPostKey() {
        return postKey;
    }

    public void setPostKey(Key<Post> postKey) {
        this.postKey = postKey;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getTagApproved() {
        return tagApproved;
    }

    public void setTagApproved(Boolean tagApproved) {
        this.tagApproved = tagApproved;
    }
}

package org.octabyte.zeem.Datastore;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class PostBlocked {

    @Id private Long id;
    private String postContent;

    public PostBlocked() {
    }

    public PostBlocked(String postContent) {
        this.postContent = postContent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }
}

package org.octabyte.zeem.Datastore;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;


@Entity
public class UserSuggestion {
    @Id private Long userId;
    private String[] postIds;

    public UserSuggestion() {
    }

    public UserSuggestion(Long userId, String[] postIds) {
        this.userId = userId;
        this.postIds = postIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String[] getPostIds() {
        return postIds;
    }

    public void setPostIds(String[] postIds) {
        this.postIds = postIds;
    }
}

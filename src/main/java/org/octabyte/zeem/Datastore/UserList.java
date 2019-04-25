package org.octabyte.zeem.Datastore;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class UserList {

    @Id private Long listId;
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    @Parent private Key<User> userKey;
    private String name;
    private int memberCount;
    @Ignore private String listSafeKey;

    public UserList() {
    }

    public UserList(Key<User> userKey, String name, int memberCount) {
        this.userKey = userKey;
        this.name = name;
        this.memberCount = memberCount;
    }

    public void setListSafeKey(String listSafeKey) {
        this.listSafeKey = Key.create(this.userKey, UserList.class, listId).getString();
    }

    public String getListSafeKey() {
        return listSafeKey;
    }

    public Long getListId() {
        return listId;
    }

    public void setListId(Long listId) {
        this.listId = listId;
    }

    public Key<User> getUserKey() {
        return userKey;
    }

    public void setUserKey(Key<User> userKey) {
        this.userKey = userKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }
}

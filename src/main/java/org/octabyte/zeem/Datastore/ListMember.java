package org.octabyte.zeem.Datastore;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.*;

@Entity
public class ListMember {

    @Id private Long id;
    @Parent private Key<UserList> listKey;
    @Index @Load private Ref<User> userRef;

    public ListMember() {
    }

    public ListMember(Key<UserList> listKey, Ref<User> userRef) {
        this.listKey = listKey;
        this.userRef = userRef;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Key<UserList> getListKey() {
        return listKey;
    }

    public void setListKey(Key<UserList> listKey) {
        this.listKey = listKey;
    }

    public Ref<User> getUserRef() {
        return userRef;
    }

    public void setUserRef(Ref<User> userRef) {
        this.userRef = userRef;
    }
}

package org.octabyte.zeem.Datastore;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class Contact {

    @Id private Long phone; // Phone numbers are always unique so it is used for id
    @Parent private Key<User> userKey;

    public Contact() {
    }

    public Contact(Long phone, Key<User> userKey) {
        this.phone = phone;
        this.userKey = userKey;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public Key<User> getUserKey() {
        return userKey;
    }

    public void setUserKey(Key<User> userKey) {
        this.userKey = userKey;
    }

}

package org.octabyte.zeem.API.Helper;

import org.octabyte.zeem.Datastore.User;

import java.util.List;

public class UserInfoHolder {

    private User user;
    private List<Long> contactList;

    public UserInfoHolder() {
    }

    public UserInfoHolder(User user, List<Long> contactList) {
        this.user = user;
        this.contactList = contactList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Long> getContactList() {
        return contactList;
    }

    public void setContactList(List<Long> contactList) {
        this.contactList = contactList;
    }
}

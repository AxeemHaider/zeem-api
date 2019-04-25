package org.octabyte.zeem.API.Entity;

import org.octabyte.zeem.Datastore.User;

public class Registered {

    private Boolean isRegistered;
    private User user;

    public Registered() {
    }

    public Boolean getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(Boolean isRegistered) {
        this.isRegistered = isRegistered;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

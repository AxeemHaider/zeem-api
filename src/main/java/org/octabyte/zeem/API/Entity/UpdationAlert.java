package org.octabyte.zeem.API.Entity;

public class UpdationAlert {

    private Boolean privateFeed = false;
    private Boolean privateNotification = false;
    private Boolean publicFeed = false;
    private Boolean publicNotification = false;

    public UpdationAlert() {
    }

    public Boolean getPrivateFeed() {
        return privateFeed;
    }

    public void setPrivateFeed(Boolean privateFeed) {
        this.privateFeed = privateFeed;
    }

    public Boolean getPrivateNotification() {
        return privateNotification;
    }

    public void setPrivateNotification(Boolean privateNotification) {
        this.privateNotification = privateNotification;
    }

    public Boolean getPublicFeed() {
        return publicFeed;
    }

    public void setPublicFeed(Boolean publicFeed) {
        this.publicFeed = publicFeed;
    }

    public Boolean getPublicNotification() {
        return publicNotification;
    }

    public void setPublicNotification(Boolean publicNotification) {
        this.publicNotification = publicNotification;
    }
}

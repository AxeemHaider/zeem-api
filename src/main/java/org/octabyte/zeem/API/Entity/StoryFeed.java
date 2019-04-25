package org.octabyte.zeem.API.Entity;

import org.octabyte.zeem.Datastore.Story;
import org.octabyte.zeem.Datastore.User;

public class StoryFeed {

    private User user;
    private Story story;
    private String updatedOnHumanReadable;

    public StoryFeed() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
        this.story.setStorySafeKey();
    }

    public String getUpdatedOnHumanReadable() {
        return updatedOnHumanReadable;
    }

    public void setUpdatedOnHumanReadable(String updatedOnHumanReadable) {
        this.updatedOnHumanReadable = updatedOnHumanReadable;
    }
}

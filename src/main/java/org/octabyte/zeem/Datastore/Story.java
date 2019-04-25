package org.octabyte.zeem.Datastore;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.*;
import org.octabyte.zeem.Helper.DataType;

import java.util.List;

@Entity
@Cache
public class Story {

    @Id private Long storyId;
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    @Parent private Key<User> userKey;
    @Ignore private String storySafeKey; // Only when returning in cloud endpoint
    protected DataType.Mode mode; // 0- for private AND 1- for public
    @Index private Long updatedOn; // This used simpleDateFormat to create TimeStamp (dd-MM-yyyy hh:mm:ss)
    private List<Stories> stories;
    private int storiesCount = 1;


    public Story() {
    }

    public Story(Long storyId, Key<User> userKey, DataType.Mode mode) {
        this.storyId = storyId;
        this.userKey = userKey;
        this.mode = mode;
    }

    public int getStoriesCount() {
        return storiesCount;
    }

    public void setStoriesCount(int storiesCount) {
        this.storiesCount = storiesCount;
    }

    public Long getStoryId() {
        return storyId;
    }

    public void setStoryId(Long storyId) {
        this.storyId = storyId;
    }

    public Key<User> getUserKey() {
        return userKey;
    }

    public void setUserKey(Key<User> userKey) {
        this.userKey = userKey;
    }

    public DataType.Mode getMode() {
        return mode;
    }

    public void setMode(DataType.Mode mode) {
        this.mode = mode;
    }

    public Long getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Long updatedOn) {
        this.updatedOn = updatedOn;
    }

    public List<Stories> getStories() {
        return stories;
    }

    public void setStories(List<Stories> stories) {
        this.stories = stories;
    }

    public void setStorySafeKey() {
        this.storySafeKey = Key.create(this.userKey, Story.class, storyId).getString();
    }
    public String getStorySafeKey(){
        return storySafeKey;
    }

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public String getWebSafeKey(){
        return Key.create(this.userKey, Story.class, storyId).getString();
    }

}

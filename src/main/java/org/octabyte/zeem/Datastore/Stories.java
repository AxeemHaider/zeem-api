package org.octabyte.zeem.Datastore;

import org.octabyte.zeem.Helper.DataType;

// #### This class is only helper class of Story class. It's not a Entity
public class Stories {

    private int storyNum; // This is used to find comment for this
    private DataType.PostType type; // 0-card, 1-image, 2-audio, 3-video, 4-gif, 5-talking photo, 6-Story
    private String source; // This is used for Audio, Video, Talking Photo and Gif
    private Long postedOn; // Create with TimeStamp and used to remove after 23 hours (1 hour for safety)

    public Stories() {
    }

    public Stories(int storyNum, String source) {
        this.storyNum = storyNum;
        this.source = source;
    }

    public Long getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(Long postedOn) {
        this.postedOn = postedOn;
    }

    public DataType.PostType getType() {
        return type;
    }

    public void setType(DataType.PostType type) {
        this.type = type;
    }

    public int getStoryNum() {
        return storyNum;
    }

    public void setStoryNum(int storyNum) {
        this.storyNum = storyNum;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}

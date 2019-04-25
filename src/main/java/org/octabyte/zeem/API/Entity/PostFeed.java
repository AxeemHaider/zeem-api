package org.octabyte.zeem.API.Entity;

import java.util.List;

public class PostFeed{

    private String cursor;
    private List<FeedItem> feedList;

    public PostFeed() {
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public List<FeedItem> getFeedList() {
        return feedList;
    }

    public void setFeedList(List<FeedItem> feedList) {
        this.feedList = feedList;
    }

}


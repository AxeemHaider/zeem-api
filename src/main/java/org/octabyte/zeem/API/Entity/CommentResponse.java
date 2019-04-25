package org.octabyte.zeem.API.Entity;


import java.util.List;

public class CommentResponse {

    private String cursor;
    private List<CommentItem> commentList;

    public CommentResponse() {
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public List<CommentItem> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentItem> commentList) {
        this.commentList = commentList;
    }

}

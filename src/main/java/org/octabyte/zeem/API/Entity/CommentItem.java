package org.octabyte.zeem.API.Entity;

import org.octabyte.zeem.Helper.DataType;

public class CommentItem {

    private String commentSafeKey;
    private DataType.CommentType type; // 0-Text, 1-Image, 2-Audio
    protected String source; // This is used for all, Text, image and for audio
    private Boolean anonymous = false;
    private int starCount = 0;
    private String username;
    private String userFullName;
    protected Long userId;
    private String profilePic;
    private int userBadge;
    private Boolean isStarByMe = false;
    private String postedOnHumanReadable;

    public CommentItem() {
    }

    public CommentItem(String commentSafeKey, DataType.CommentType type, String source, Boolean anonymous, int starCount, String username, String userFullName, Long userId, String profilePic, int userBadge) {
        this.commentSafeKey = commentSafeKey;
        this.type = type;
        this.source = source;
        this.anonymous = anonymous;
        this.starCount = starCount;
        this.username = username;
        this.userFullName = userFullName;
        this.userId = userId;
        this.profilePic = profilePic;
        this.userBadge = userBadge;
    }

    public String getPostedOnHumanReadable() {
        return postedOnHumanReadable;
    }

    public void setPostedOnHumanReadable(String postedOnHumanReadable) {
        this.postedOnHumanReadable = postedOnHumanReadable;
    }

    public String getCommentSafeKey() {
        return commentSafeKey;
    }

    public int getUserBadge() {
        return userBadge;
    }

    public void setUserBadge(int userBadge) {
        this.userBadge = userBadge;
    }

    public void setCommentSafeKey(String commentSafeKey) {
        this.commentSafeKey = commentSafeKey;
    }

    public Boolean getStarByMe() {
        return isStarByMe;
    }

    public void setStarByMe(Boolean starByMe) {
        isStarByMe = starByMe;
    }

    public DataType.CommentType getType() {
        return type;
    }

    public void setType(DataType.CommentType type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

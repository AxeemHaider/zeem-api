package org.octabyte.zeem.API.Entity;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import org.octabyte.zeem.Datastore.Post;
import org.octabyte.zeem.Helper.DataType;

import java.util.List;

public class FeedItem extends Post {

    private String feedSafeKey;
    private Long postId;
    private Long userId;
    private String userFullName;
    private String userProfilePic;
    private int userBadge;
    private String postedOnHumanReadable;
    private String expireTimeHumanReadable;
    private int listCount;
    private Long postListId;
    private Boolean taggedMe = false; // Post is tagged to me or not
    private Boolean taggedApproved = false; // Tag is approved by me or not
    private Boolean isStarByMe = false; // Post is liked by me or not
    private List<CommentItem> commentItems;
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private String unFormattedListId;

    public FeedItem() {
    }

    public FeedItem(DataType.Mode mode, DataType.PostType type, String location,
                    Boolean anonymous, int starCount, String cover, String source,
                    String caption, String cardColor, Boolean postTag,
                    int taggedCount, Boolean allowComment, int totalComments) {
        super(mode, type, location, anonymous, starCount, cover, source,
                caption, cardColor, postTag, taggedCount, allowComment, totalComments);
    }

    @Override
    public void setPostSafeKey(String postSafeKey) {
        super.postSafeKey = postSafeKey;
    }

    @Override
    public Long getPostId() {
        return postId;
    }

    @Override
    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getFeedSafeKey() {
        return feedSafeKey;
    }

    public void setFeedSafeKey(String feedSafeKey) {
        this.feedSafeKey = feedSafeKey;
    }

    public Long getUserId() {
        return userId;
    }

    public Boolean getStarByMe() {
        return isStarByMe;
    }

    public void setStarByMe(Boolean starByMe) {
        isStarByMe = starByMe;
    }

    public Boolean getTaggedApproved() {
        return taggedApproved;
    }

    public void setTaggedApproved(Boolean taggedApproved) {
        this.taggedApproved = taggedApproved;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    public int getUserBadge() {
        return userBadge;
    }

    public void setUserBadge(int userBadge) {
        this.userBadge = userBadge;
    }

    public void setUnFormattedListId(String unFormattedListId) {
        this.unFormattedListId = unFormattedListId;

        String[] specialList = unFormattedListId.split("_");
        setListCount(Integer.parseInt(specialList[0]));
        setPostListId(Long.valueOf(specialList[1]));
    }

    public String getPostedOnHumanReadable() {
        return postedOnHumanReadable;
    }

    public void setPostedOnHumanReadable(String postedOnHumanReadable) {
        this.postedOnHumanReadable = postedOnHumanReadable;
    }

    public String getExpireTimeHumanReadable() {
        return expireTimeHumanReadable;
    }

    public void setExpireTimeHumanReadable(String expireTimeHumanReadable) {
        this.expireTimeHumanReadable = expireTimeHumanReadable;
    }

    public int getListCount() {
        return listCount;
    }

    public void setListCount(int listCount) {
        this.listCount = listCount;
    }

    public Long getPostListId() {
        return postListId;
    }

    public void setPostListId(Long postListId) {
        this.postListId = postListId;
    }

    public Boolean getTaggedMe() {
        return taggedMe;
    }

    public void setTaggedMe(Boolean taggedMe) {
        this.taggedMe = taggedMe;
    }

    public List<CommentItem> getCommentItems() {
        return commentItems;
    }

    public void setCommentItems(List<CommentItem> commentItems) {
        this.commentItems = commentItems;
    }
}
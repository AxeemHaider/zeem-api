package org.octabyte.zeem.Datastore;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.*;
import org.octabyte.zeem.Helper.DataType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Cache
public class Post {

    @Id private Long postId; // Generate from time stamp
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    @Parent private Key<User> userKey;
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    @Load List<Ref<Comment>> recentComment; // Only contain last three text comments
    @Ignore private List<Comment> commentList = new ArrayList<>(); // Hold List of comment object
    @Ignore protected String postSafeKey; // Only when returning in cloud endpoint
    @Index protected DataType.Mode mode; // 0- for private AND 1- for public
    protected DataType.PostType type; // 0-card, 1-image, 2-audio, 3-video, 4-gif, 5-talking photo, 6-Story
    protected String location;
    private Long postedOn = System.currentTimeMillis(); // This used simpleDateFormat to create TimeStamp (dd-MM-yyyy hh:mm:ss)
    private Long expireTime;
    protected Boolean anonymous = false; // By default it is false
    private String listId; // (Nullable) Special id first letter contain member count after (-) id of the list
    protected int starCount = 0;
    private String cardColor; // When post type is CARD, used for Background and Text color (e.g #fff,#000,#a23) last one Text color
    protected String cover; // it can be NULL for Card post
    protected String source; // This is used for Audio, Video, Talking Photo and Gif
    protected String caption;
    private Boolean postTag = false; // Post is Tag to someone or not
    private int taggedCount = 0; // How many user is tagged with this post
    protected Boolean allowComment = true; // Allow comment on post or not
    private int totalComments = 0;

    @OnLoad
    private void fillCommentList(){
        if(recentComment != null) {
            for (Ref<Comment> commentRef : recentComment) {
                commentList.add(commentRef.get());
            }
        }
    }

    public Post() {
    }

    public Post(DataType.Mode mode, DataType.PostType type, String location,
                Boolean anonymous, int starCount, String cover, String source,
                String caption, String cardColor, Boolean postTag,
                int taggedCount, Boolean allowComment, int totalComments) {
        this.mode = mode;
        this.type = type;
        this.location = location;
        this.anonymous = anonymous;
        this.starCount = starCount;
        this.cover = cover;
        this.source = source;
        this.caption = caption;
        this.cardColor = cardColor;
        this.postTag = postTag;
        this.taggedCount = taggedCount;
        this.allowComment = allowComment;
        this.totalComments = totalComments;
    }

    public Post(Key<User> userKey, DataType.Mode mode, DataType.PostType type, Long postedOn) {
        this.userKey = userKey;
        this.mode = mode;
        this.type = type;
        this.postedOn = postedOn;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }

    public Boolean getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(Boolean allowComment) {
        this.allowComment = allowComment;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public void setPostSafeKey(String postSafeKey) {
        this.postSafeKey = Key.create(this.userKey, Post.class, postId).getString();
    }

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public String getWebSafeKey(){
        return Key.create(this.userKey, Post.class, postId).getString();
    }

    public String getPostSafeKey(){
        // REMOVE this.postSafeKey = Key.create(this.userKey, Post.class, postId).getString();
        return postSafeKey;
    }

    public String autoPostKey(){
        return Key.create(this.userKey, Post.class, postId).getString();
    }

    public int getTaggedCount() {
        return taggedCount;
    }

    public void setTaggedCount(int taggedCount) {
        this.taggedCount = taggedCount;
    }

    public Boolean getPostTag() {
        return postTag;
    }

    public void setPostTag(Boolean postTag) {
        this.postTag = postTag;
    }

    public List<Ref<Comment>> getRecentComment() {
        return recentComment;
    }

    public void setRecentComment(List<Ref<Comment>> recentComment) {
        this.recentComment = recentComment;
    }

    public String getCardColor() {
        return cardColor;
    }

    public void setCardColor(String cardColor) {
        this.cardColor = cardColor;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
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

    public DataType.PostType getType() {
        return type;
    }

    public void setType(DataType.PostType type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(Long postedOn) {
        this.postedOn = postedOn;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}

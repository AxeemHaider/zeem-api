package org.octabyte.zeem.Datastore;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.*;
import org.octabyte.zeem.Helper.DataType;

import java.util.List;

@Entity
public class StoryComment {

    @Id private Long commentId; // Create it using TimeStamp for order sorting. Also used for commented on
    @Index private int storyNum; // Identify the story position. Find comment on this base
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    @Parent private Key<Story> storyKey;
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    @Load private Ref<User> userRef;
    private DataType.CommentType type; // 0-Text, 1-Image, 2-Audio
    private String source; // This is used for all, Text, image and for audio
    private Boolean anonymous = false;
    private List<Long> taggedUser;
    private Long postedOn = System.currentTimeMillis();
    @Ignore private String commnetSafeKey; // Only when returning in cloud endpoint
    @Ignore private Long userId; // Id of user who create this comment
    @OnLoad private void loadUserId(){
        userId = this.getUserRef().get().getUserId();
    }

    public StoryComment() {
    }

    public StoryComment(Long commentId, Key<Story> storyKey, Ref<User> userRef,
                   DataType.CommentType type, String source, Boolean anonymous, List<Long> taggedUser, int storyNum) {
        this.commentId = commentId;
        this.storyKey = storyKey;
        this.userRef = userRef;
        this.type = type;
        this.source = source;
        this.anonymous = anonymous;
        this.taggedUser = taggedUser;
        this.storyNum = storyNum;
    }

    public String getCommnetSafeKey(){
        this.commnetSafeKey = Key.create(this.storyKey, Comment.class, commentId).getString();
        return commnetSafeKey;
    }

    public Long getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(Long postedOn) {
        this.postedOn = postedOn;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public int getStoryNum() {
        return storyNum;
    }

    public void setStoryNum(int storyNum) {
        this.storyNum = storyNum;
    }

    public Key<Story> getStoryKey() {
        return storyKey;
    }

    public void setStoryKey(Key<Story> storyKey) {
        this.storyKey = storyKey;
    }

    public Ref<User> getUserRef() {
        return userRef;
    }

    public void setUserRef(Ref<User> userRef) {
        this.userRef = userRef;
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

    public List<Long> getTaggedUser() {
        return taggedUser;
    }

    public void setTaggedUser(List<Long> taggedUser) {
        this.taggedUser = taggedUser;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

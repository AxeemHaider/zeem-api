package org.octabyte.zeem.Datastore;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.*;
import org.octabyte.zeem.Helper.DataType;

import java.util.List;

@Entity
@Cache
public class Comment {

    @Id private Long commentId; // Create it using TimeStamp for order sorting. Also used for commented on
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    @Parent private Key<Post> postKey;
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    @Load private Ref<User> userRef;
    private DataType.CommentType type; // 0-Text, 1-Image, 2-Audio
    private String source; // This is used for all, Text, image and for audio
    private Boolean anonymous = false;
    private List<Long> taggedUser;
    private int starCount = 0;
    private List<Long> commentStar; // Hold userIds who star the comment
    @Ignore private String commnetSafeKey; // Only when returning in cloud endpoint
    @Ignore private Long userId; // Id of user who create this comment
    private Long postedOn = System.currentTimeMillis();

    @OnLoad
    private void loadUserId(){
        userId = this.getUserRef().get().getUserId();
    }

    public Comment() {
    }

    public Comment(Long commentId, Key<Post> postKey, Ref<User> userRef,
                   DataType.CommentType type, String source, Boolean anonymous, List<Long> taggedUser) {
        this.commentId = commentId;
        this.postKey = postKey;
        this.userRef = userRef;
        this.type = type;
        this.source = source;
        this.anonymous = anonymous;
        this.taggedUser = taggedUser;
    }

    public String getCommnetSafeKey(){
        this.commnetSafeKey = Key.create(this.postKey, Comment.class, commentId).getString();
        return commnetSafeKey;
    }

    public Long getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(Long postedOn) {
        this.postedOn = postedOn;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Ref<User> getUserRef() {
        return userRef;
    }

    public void setUserRef(Ref<User> userRef) {
        this.userRef = userRef;
    }

    public List<Long> getCommentStar() {
        return commentStar;
    }

    public void setCommentStar(List<Long> commentStar) {
        this.commentStar = commentStar;
    }

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public List<Long> getTaggedUser() {
        return taggedUser;
    }

    public void setTaggedUser(List<Long> taggedUser) {
        this.taggedUser = taggedUser;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Key<Post> getPostKey() {
        return postKey;
    }

    public void setPostKey(Key<Post> postKey) {
        this.postKey = postKey;
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
}

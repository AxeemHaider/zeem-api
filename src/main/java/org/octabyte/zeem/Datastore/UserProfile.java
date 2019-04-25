package org.octabyte.zeem.Datastore;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import org.octabyte.zeem.Helper.DataType;

@Entity
public class UserProfile {

    @Id private Long userId;
    private String email, status;
    private DataType.Gender gender = DataType.Gender.M;
    private int
            starCount = 0,
            friendCount = 0,
            followerCount = 0,
            followingCount = 0,
            postCount = 0,
            dob;
    private DataType.TagApproved showTagPost = DataType.TagApproved.PUBLIC; // How Tag post show on your profile - Special int 1-Public, 2-Private, 3-Never
    private DataType.HowTag postTag = DataType.HowTag.PUBLIC; // Who can Tag you - Special int 1-Both, 2-Friends, 3-Follower, 4-NoOne
    private Boolean anonymousTag = true; // Can Anonymous person tag you

    public UserProfile() {
    }

    public UserProfile(Long userId) {
        this.userId = userId;
    }

    public void updateProfile(String email, String status, Integer dob, DataType.Gender gender){

        if (email != null){
            this.email = email;
        }
        if (status != null) {
            this.status = status;
        }
        if (dob != null){
            this.dob = dob;
        }
        if (gender != null){
            this.gender = gender;
        }

    }

    public void updateSetting(DataType.TagApproved showTagPost, DataType.HowTag postTag, Boolean anonymousTag){
        if (showTagPost != null){
            this.showTagPost = showTagPost;
        }

        if (postTag != null){
            this.postTag = postTag;
        }

        if (anonymousTag != null){
            this.anonymousTag = anonymousTag;
        }

    }

    public DataType.Gender getGender() {
        return gender;
    }

    public void setGender(DataType.Gender gender) {
        this.gender = gender;
    }

    public DataType.TagApproved getShowTagPost() {
        return showTagPost;
    }

    public void setShowTagPost(DataType.TagApproved showTagPost) {
        this.showTagPost = showTagPost;
    }

    public DataType.HowTag getPostTag() {
        return postTag;
    }

    public void setPostTag(DataType.HowTag postTag) {
        this.postTag = postTag;
    }

    public Boolean getAnonymousTag() {
        return anonymousTag;
    }

    public void setAnonymousTag(Boolean anonymousTag) {
        this.anonymousTag = anonymousTag;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public int getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(int friendCount) {
        this.friendCount = friendCount;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public int getDob() {
        return dob;
    }

    public void setDob(int dob) {
        this.dob = dob;
    }
}

package org.octabyte.zeem.API.Entity;

import org.octabyte.zeem.Datastore.User;
import org.octabyte.zeem.Datastore.UserProfile;

public class ProfileView {

    private User user;
    private UserProfile userProfile;
    private Relationship relationship;
    private PostFeed post;

    public ProfileView() {
    }

    public ProfileView(User user, UserProfile userProfile, Relationship relationship, PostFeed post) {
        this.user = user;
        this.userProfile = userProfile;
        this.relationship = relationship;
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }

    public PostFeed getPost() {
        return post;
    }

    public void setPost(PostFeed post) {
        this.post = post;
    }
}

package org.octabyte.zeem.API.Entity;

public class Relationship {

    private String relation;
    private Boolean isFollowing = false;

    public Relationship() {
    }

    public Relationship(String relation) {
        this.relation = relation;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public Boolean getFollowing() {
        return isFollowing;
    }

    public void setFollowing(Boolean following) {
        isFollowing = following;
    }
}

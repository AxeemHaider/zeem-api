package org.octabyte.zeem.Datastore;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Cache
public class User {

    @Id private Long userId;
    @Index private String username;
    @Index private String fullName;
    @Index private Long phone;
    @Index private String geoHash;
    private String profilePic = "PIC/default_user_pic.jpg"; // Must have some default value
    private int badge = 0;
    private String firebaseToken;
    private boolean active = true;

    public User() {}

    public User(String username, String fullName, Long phone, String geoHash) {
        this.username = username;
        this.fullName = fullName;
        this.phone = phone;
        this.geoHash = geoHash;
    }

    public int getBadge() {
        return badge;
    }

    public Boolean updateUser(String fullName, String profilePic){

        Boolean flag = false;

        if (fullName != null){
            this.fullName = fullName;
            flag = true;
        }
        if (profilePic != null) {
            this.profilePic = profilePic;
            flag = true;
        }

        return flag;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public String getGeoHash() {
        return geoHash;
    }

    public void setGeoHash(String geoHash) {
        this.geoHash = geoHash;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }
}

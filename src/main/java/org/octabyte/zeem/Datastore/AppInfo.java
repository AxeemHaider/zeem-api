package org.octabyte.zeem.Datastore;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache
public class AppInfo {

    @Id private Long id;
    private int versionCode;
    private String versionNumber;

    public AppInfo() {
    }

    public AppInfo(Long id, int versionCode, String versionNumber) {
        this.id = id;
        this.versionCode = versionCode;
        this.versionNumber = versionNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }
}

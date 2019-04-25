package org.octabyte.zeem.Datastore;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import org.octabyte.zeem.Helper.DataType;

import java.util.List;

@Entity
public class PostReport {

    @Id private Long postId;
    private List<Long> userIds;
    private List<DataType.Report> reportTypes;
    private int reportCount = 1;

    public PostReport() {
    }

    public PostReport(Long postId) {
        this.postId = postId;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public List<DataType.Report> getReportTypes() {
        return reportTypes;
    }

    public void setReportTypes(List<DataType.Report> reportTypes) {
        this.reportTypes = reportTypes;
    }

    public int getReportCount() {
        return reportCount;
    }

    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }
}

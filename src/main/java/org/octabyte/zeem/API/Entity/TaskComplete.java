package org.octabyte.zeem.API.Entity;

public class TaskComplete {

    private Boolean isComplete = false;

    public TaskComplete() {
    }

    public Boolean getComplete() {
        return isComplete;
    }

    public TaskComplete(Boolean isComplete) {
        this.isComplete = isComplete;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }
}

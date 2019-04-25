package org.octabyte.zeem.API.Entity;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;

public class UserFriendRequest {

    private Long requestId;
    private Long friendId;
    private String friendPic;
    private int friendBadge;
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private String friendName;
    private Long friendPhone;

    private String requestMessage = "[%s] wants to become friend. Phone: [%d]";

    public UserFriendRequest() {
    }

    public UserFriendRequest(Long requestId, Long friendId, String friendPic, int friendBadge, String friendName, Long friendPhone) {
        this.requestId = requestId;
        this.friendId = friendId;
        this.friendPic = friendPic;
        this.friendBadge = friendBadge;
        this.requestMessage = String.format(requestMessage, friendName, friendPhone);
        this.friendPhone = friendPhone;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public Long getFriendId() {
        return friendId;
    }

    public Long getFriendPhone() {
        return friendPhone;
    }

    public void setFriendPhone(Long friendPhone) {
        this.friendPhone = friendPhone;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    public String getFriendPic() {
        return friendPic;
    }

    public void setFriendPic(String friendPic) {
        this.friendPic = friendPic;
    }

    public int getFriendBadge() {
        return friendBadge;
    }

    public void setFriendBadge(int friendBadge) {
        this.friendBadge = friendBadge;
    }
}

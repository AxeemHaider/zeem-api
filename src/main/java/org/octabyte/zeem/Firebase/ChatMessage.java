package org.octabyte.zeem.Firebase;

public class ChatMessage {

    private Long messageId;
    private String chatAnonymous = "chat_anonymous";
    private String senderToken, receiverToken, isAnonymous, messageReceived;

    private String chatTitle = "";
    private String messageText = "";
    private String profilePic;
    private String username;

    private boolean instantChat = false;

    public ChatMessage() {
    }

    public boolean isInstantChat() {
        return instantChat;
    }

    public void setInstantChat(boolean instantChat) {
        this.instantChat = instantChat;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getChatAnonymous() {
        return chatAnonymous;
    }

    public void setChatAnonymous(String chatAnonymous) {
        this.chatAnonymous = chatAnonymous;
    }

    public String getChatTitle() {
        return chatTitle;
    }

    public void setChatTitle(String chatTitle) {
        this.chatTitle = chatTitle;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSenderToken() {
        return senderToken;
    }

    public void setSenderToken(String senderToken) {
        this.senderToken = senderToken;
    }

    public String getReceiverToken() {
        return receiverToken;
    }

    public void setReceiverToken(String receiverToken) {
        this.receiverToken = receiverToken;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(String isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public String getMessageReceived() {
        return messageReceived;
    }

    public void setMessageReceived(String messageReceived) {
        this.messageReceived = messageReceived;
    }
}

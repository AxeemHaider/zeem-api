/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://github.com/google/apis-client-generator/
 * (build: 2018-05-04 17:28:03 UTC)
 * on 2018-09-27 at 12:43:59 UTC 
 * Modify at your own risk.
 */

package com.appspot.octabyte_zeem.zeem.model;

/**
 * Model definition for ChatMessage.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the zeem. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class ChatMessage extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String chatAnonymous;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String chatTitle;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Boolean instantChat;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String isAnonymous;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long messageId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String messageReceived;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String messageText;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String profilePic;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String receiverToken;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String senderToken;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String username;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getChatAnonymous() {
    return chatAnonymous;
  }

  /**
   * @param chatAnonymous chatAnonymous or {@code null} for none
   */
  public ChatMessage setChatAnonymous(java.lang.String chatAnonymous) {
    this.chatAnonymous = chatAnonymous;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getChatTitle() {
    return chatTitle;
  }

  /**
   * @param chatTitle chatTitle or {@code null} for none
   */
  public ChatMessage setChatTitle(java.lang.String chatTitle) {
    this.chatTitle = chatTitle;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getInstantChat() {
    return instantChat;
  }

  /**
   * @param instantChat instantChat or {@code null} for none
   */
  public ChatMessage setInstantChat(java.lang.Boolean instantChat) {
    this.instantChat = instantChat;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getIsAnonymous() {
    return isAnonymous;
  }

  /**
   * @param isAnonymous isAnonymous or {@code null} for none
   */
  public ChatMessage setIsAnonymous(java.lang.String isAnonymous) {
    this.isAnonymous = isAnonymous;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getMessageId() {
    return messageId;
  }

  /**
   * @param messageId messageId or {@code null} for none
   */
  public ChatMessage setMessageId(java.lang.Long messageId) {
    this.messageId = messageId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getMessageReceived() {
    return messageReceived;
  }

  /**
   * @param messageReceived messageReceived or {@code null} for none
   */
  public ChatMessage setMessageReceived(java.lang.String messageReceived) {
    this.messageReceived = messageReceived;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getMessageText() {
    return messageText;
  }

  /**
   * @param messageText messageText or {@code null} for none
   */
  public ChatMessage setMessageText(java.lang.String messageText) {
    this.messageText = messageText;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getProfilePic() {
    return profilePic;
  }

  /**
   * @param profilePic profilePic or {@code null} for none
   */
  public ChatMessage setProfilePic(java.lang.String profilePic) {
    this.profilePic = profilePic;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getReceiverToken() {
    return receiverToken;
  }

  /**
   * @param receiverToken receiverToken or {@code null} for none
   */
  public ChatMessage setReceiverToken(java.lang.String receiverToken) {
    this.receiverToken = receiverToken;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getSenderToken() {
    return senderToken;
  }

  /**
   * @param senderToken senderToken or {@code null} for none
   */
  public ChatMessage setSenderToken(java.lang.String senderToken) {
    this.senderToken = senderToken;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getUsername() {
    return username;
  }

  /**
   * @param username username or {@code null} for none
   */
  public ChatMessage setUsername(java.lang.String username) {
    this.username = username;
    return this;
  }

  @Override
  public ChatMessage set(String fieldName, Object value) {
    return (ChatMessage) super.set(fieldName, value);
  }

  @Override
  public ChatMessage clone() {
    return (ChatMessage) super.clone();
  }

}

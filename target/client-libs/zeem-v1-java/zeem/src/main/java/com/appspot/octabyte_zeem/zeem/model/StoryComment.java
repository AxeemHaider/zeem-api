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
 * Model definition for StoryComment.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the zeem. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class StoryComment extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Boolean anonymous;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long commentId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String commnetSafeKey;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long postedOn;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String source;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer storyNum;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.util.List<java.lang.Long> taggedUser;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String type;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long userId;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getAnonymous() {
    return anonymous;
  }

  /**
   * @param anonymous anonymous or {@code null} for none
   */
  public StoryComment setAnonymous(java.lang.Boolean anonymous) {
    this.anonymous = anonymous;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getCommentId() {
    return commentId;
  }

  /**
   * @param commentId commentId or {@code null} for none
   */
  public StoryComment setCommentId(java.lang.Long commentId) {
    this.commentId = commentId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getCommnetSafeKey() {
    return commnetSafeKey;
  }

  /**
   * @param commnetSafeKey commnetSafeKey or {@code null} for none
   */
  public StoryComment setCommnetSafeKey(java.lang.String commnetSafeKey) {
    this.commnetSafeKey = commnetSafeKey;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getPostedOn() {
    return postedOn;
  }

  /**
   * @param postedOn postedOn or {@code null} for none
   */
  public StoryComment setPostedOn(java.lang.Long postedOn) {
    this.postedOn = postedOn;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getSource() {
    return source;
  }

  /**
   * @param source source or {@code null} for none
   */
  public StoryComment setSource(java.lang.String source) {
    this.source = source;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getStoryNum() {
    return storyNum;
  }

  /**
   * @param storyNum storyNum or {@code null} for none
   */
  public StoryComment setStoryNum(java.lang.Integer storyNum) {
    this.storyNum = storyNum;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<java.lang.Long> getTaggedUser() {
    return taggedUser;
  }

  /**
   * @param taggedUser taggedUser or {@code null} for none
   */
  public StoryComment setTaggedUser(java.util.List<java.lang.Long> taggedUser) {
    this.taggedUser = taggedUser;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getType() {
    return type;
  }

  /**
   * @param type type or {@code null} for none
   */
  public StoryComment setType(java.lang.String type) {
    this.type = type;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getUserId() {
    return userId;
  }

  /**
   * @param userId userId or {@code null} for none
   */
  public StoryComment setUserId(java.lang.Long userId) {
    this.userId = userId;
    return this;
  }

  @Override
  public StoryComment set(String fieldName, Object value) {
    return (StoryComment) super.set(fieldName, value);
  }

  @Override
  public StoryComment clone() {
    return (StoryComment) super.clone();
  }

}

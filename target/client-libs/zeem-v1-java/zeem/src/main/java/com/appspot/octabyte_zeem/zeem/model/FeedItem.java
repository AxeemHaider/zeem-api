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
 * Model definition for FeedItem.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the zeem. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class FeedItem extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Boolean allowComment;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Boolean anonymous;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String caption;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String cardColor;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<CommentItem> commentItems;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<Comment> commentList;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String cover;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long expireTime;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String expireTimeHumanReadable;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String feedSafeKey;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer listCount;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String listId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String location;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String mode;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long postId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long postListId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String postSafeKey;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Boolean postTag;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long postedOn;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String postedOnHumanReadable;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String source;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Boolean starByMe;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer starCount;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Boolean taggedApproved;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer taggedCount;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Boolean taggedMe;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer totalComments;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String type;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer userBadge;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String userFullName;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long userId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String userProfilePic;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getAllowComment() {
    return allowComment;
  }

  /**
   * @param allowComment allowComment or {@code null} for none
   */
  public FeedItem setAllowComment(java.lang.Boolean allowComment) {
    this.allowComment = allowComment;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getAnonymous() {
    return anonymous;
  }

  /**
   * @param anonymous anonymous or {@code null} for none
   */
  public FeedItem setAnonymous(java.lang.Boolean anonymous) {
    this.anonymous = anonymous;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getCaption() {
    return caption;
  }

  /**
   * @param caption caption or {@code null} for none
   */
  public FeedItem setCaption(java.lang.String caption) {
    this.caption = caption;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getCardColor() {
    return cardColor;
  }

  /**
   * @param cardColor cardColor or {@code null} for none
   */
  public FeedItem setCardColor(java.lang.String cardColor) {
    this.cardColor = cardColor;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<CommentItem> getCommentItems() {
    return commentItems;
  }

  /**
   * @param commentItems commentItems or {@code null} for none
   */
  public FeedItem setCommentItems(java.util.List<CommentItem> commentItems) {
    this.commentItems = commentItems;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<Comment> getCommentList() {
    return commentList;
  }

  /**
   * @param commentList commentList or {@code null} for none
   */
  public FeedItem setCommentList(java.util.List<Comment> commentList) {
    this.commentList = commentList;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getCover() {
    return cover;
  }

  /**
   * @param cover cover or {@code null} for none
   */
  public FeedItem setCover(java.lang.String cover) {
    this.cover = cover;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getExpireTime() {
    return expireTime;
  }

  /**
   * @param expireTime expireTime or {@code null} for none
   */
  public FeedItem setExpireTime(java.lang.Long expireTime) {
    this.expireTime = expireTime;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getExpireTimeHumanReadable() {
    return expireTimeHumanReadable;
  }

  /**
   * @param expireTimeHumanReadable expireTimeHumanReadable or {@code null} for none
   */
  public FeedItem setExpireTimeHumanReadable(java.lang.String expireTimeHumanReadable) {
    this.expireTimeHumanReadable = expireTimeHumanReadable;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getFeedSafeKey() {
    return feedSafeKey;
  }

  /**
   * @param feedSafeKey feedSafeKey or {@code null} for none
   */
  public FeedItem setFeedSafeKey(java.lang.String feedSafeKey) {
    this.feedSafeKey = feedSafeKey;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getListCount() {
    return listCount;
  }

  /**
   * @param listCount listCount or {@code null} for none
   */
  public FeedItem setListCount(java.lang.Integer listCount) {
    this.listCount = listCount;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getListId() {
    return listId;
  }

  /**
   * @param listId listId or {@code null} for none
   */
  public FeedItem setListId(java.lang.String listId) {
    this.listId = listId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getLocation() {
    return location;
  }

  /**
   * @param location location or {@code null} for none
   */
  public FeedItem setLocation(java.lang.String location) {
    this.location = location;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getMode() {
    return mode;
  }

  /**
   * @param mode mode or {@code null} for none
   */
  public FeedItem setMode(java.lang.String mode) {
    this.mode = mode;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getPostId() {
    return postId;
  }

  /**
   * @param postId postId or {@code null} for none
   */
  public FeedItem setPostId(java.lang.Long postId) {
    this.postId = postId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getPostListId() {
    return postListId;
  }

  /**
   * @param postListId postListId or {@code null} for none
   */
  public FeedItem setPostListId(java.lang.Long postListId) {
    this.postListId = postListId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getPostSafeKey() {
    return postSafeKey;
  }

  /**
   * @param postSafeKey postSafeKey or {@code null} for none
   */
  public FeedItem setPostSafeKey(java.lang.String postSafeKey) {
    this.postSafeKey = postSafeKey;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getPostTag() {
    return postTag;
  }

  /**
   * @param postTag postTag or {@code null} for none
   */
  public FeedItem setPostTag(java.lang.Boolean postTag) {
    this.postTag = postTag;
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
  public FeedItem setPostedOn(java.lang.Long postedOn) {
    this.postedOn = postedOn;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getPostedOnHumanReadable() {
    return postedOnHumanReadable;
  }

  /**
   * @param postedOnHumanReadable postedOnHumanReadable or {@code null} for none
   */
  public FeedItem setPostedOnHumanReadable(java.lang.String postedOnHumanReadable) {
    this.postedOnHumanReadable = postedOnHumanReadable;
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
  public FeedItem setSource(java.lang.String source) {
    this.source = source;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getStarByMe() {
    return starByMe;
  }

  /**
   * @param starByMe starByMe or {@code null} for none
   */
  public FeedItem setStarByMe(java.lang.Boolean starByMe) {
    this.starByMe = starByMe;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getStarCount() {
    return starCount;
  }

  /**
   * @param starCount starCount or {@code null} for none
   */
  public FeedItem setStarCount(java.lang.Integer starCount) {
    this.starCount = starCount;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getTaggedApproved() {
    return taggedApproved;
  }

  /**
   * @param taggedApproved taggedApproved or {@code null} for none
   */
  public FeedItem setTaggedApproved(java.lang.Boolean taggedApproved) {
    this.taggedApproved = taggedApproved;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getTaggedCount() {
    return taggedCount;
  }

  /**
   * @param taggedCount taggedCount or {@code null} for none
   */
  public FeedItem setTaggedCount(java.lang.Integer taggedCount) {
    this.taggedCount = taggedCount;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getTaggedMe() {
    return taggedMe;
  }

  /**
   * @param taggedMe taggedMe or {@code null} for none
   */
  public FeedItem setTaggedMe(java.lang.Boolean taggedMe) {
    this.taggedMe = taggedMe;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getTotalComments() {
    return totalComments;
  }

  /**
   * @param totalComments totalComments or {@code null} for none
   */
  public FeedItem setTotalComments(java.lang.Integer totalComments) {
    this.totalComments = totalComments;
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
  public FeedItem setType(java.lang.String type) {
    this.type = type;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getUserBadge() {
    return userBadge;
  }

  /**
   * @param userBadge userBadge or {@code null} for none
   */
  public FeedItem setUserBadge(java.lang.Integer userBadge) {
    this.userBadge = userBadge;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getUserFullName() {
    return userFullName;
  }

  /**
   * @param userFullName userFullName or {@code null} for none
   */
  public FeedItem setUserFullName(java.lang.String userFullName) {
    this.userFullName = userFullName;
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
  public FeedItem setUserId(java.lang.Long userId) {
    this.userId = userId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getUserProfilePic() {
    return userProfilePic;
  }

  /**
   * @param userProfilePic userProfilePic or {@code null} for none
   */
  public FeedItem setUserProfilePic(java.lang.String userProfilePic) {
    this.userProfilePic = userProfilePic;
    return this;
  }

  @Override
  public FeedItem set(String fieldName, Object value) {
    return (FeedItem) super.set(fieldName, value);
  }

  @Override
  public FeedItem clone() {
    return (FeedItem) super.clone();
  }

}
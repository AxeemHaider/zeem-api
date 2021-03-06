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
 * Model definition for UserAlert.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the zeem. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class UserAlert extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long privateFeedId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long privateNotificationId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long publicFeedId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long publicNotificationId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long userId;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getPrivateFeedId() {
    return privateFeedId;
  }

  /**
   * @param privateFeedId privateFeedId or {@code null} for none
   */
  public UserAlert setPrivateFeedId(java.lang.Long privateFeedId) {
    this.privateFeedId = privateFeedId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getPrivateNotificationId() {
    return privateNotificationId;
  }

  /**
   * @param privateNotificationId privateNotificationId or {@code null} for none
   */
  public UserAlert setPrivateNotificationId(java.lang.Long privateNotificationId) {
    this.privateNotificationId = privateNotificationId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getPublicFeedId() {
    return publicFeedId;
  }

  /**
   * @param publicFeedId publicFeedId or {@code null} for none
   */
  public UserAlert setPublicFeedId(java.lang.Long publicFeedId) {
    this.publicFeedId = publicFeedId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getPublicNotificationId() {
    return publicNotificationId;
  }

  /**
   * @param publicNotificationId publicNotificationId or {@code null} for none
   */
  public UserAlert setPublicNotificationId(java.lang.Long publicNotificationId) {
    this.publicNotificationId = publicNotificationId;
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
  public UserAlert setUserId(java.lang.Long userId) {
    this.userId = userId;
    return this;
  }

  @Override
  public UserAlert set(String fieldName, Object value) {
    return (UserAlert) super.set(fieldName, value);
  }

  @Override
  public UserAlert clone() {
    return (UserAlert) super.clone();
  }

}

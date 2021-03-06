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
 * Model definition for Story.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the zeem. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class Story extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String mode;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<Stories> stories;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer storiesCount;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long storyId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String storySafeKey;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long updatedOn;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getMode() {
    return mode;
  }

  /**
   * @param mode mode or {@code null} for none
   */
  public Story setMode(java.lang.String mode) {
    this.mode = mode;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<Stories> getStories() {
    return stories;
  }

  /**
   * @param stories stories or {@code null} for none
   */
  public Story setStories(java.util.List<Stories> stories) {
    this.stories = stories;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getStoriesCount() {
    return storiesCount;
  }

  /**
   * @param storiesCount storiesCount or {@code null} for none
   */
  public Story setStoriesCount(java.lang.Integer storiesCount) {
    this.storiesCount = storiesCount;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getStoryId() {
    return storyId;
  }

  /**
   * @param storyId storyId or {@code null} for none
   */
  public Story setStoryId(java.lang.Long storyId) {
    this.storyId = storyId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getStorySafeKey() {
    return storySafeKey;
  }

  /**
   * @param storySafeKey storySafeKey or {@code null} for none
   */
  public Story setStorySafeKey(java.lang.String storySafeKey) {
    this.storySafeKey = storySafeKey;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getUpdatedOn() {
    return updatedOn;
  }

  /**
   * @param updatedOn updatedOn or {@code null} for none
   */
  public Story setUpdatedOn(java.lang.Long updatedOn) {
    this.updatedOn = updatedOn;
    return this;
  }

  @Override
  public Story set(String fieldName, Object value) {
    return (Story) super.set(fieldName, value);
  }

  @Override
  public Story clone() {
    return (Story) super.clone();
  }

}

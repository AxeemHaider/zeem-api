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
 * Model definition for ProfileView.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the zeem. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class ProfileView extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private PostFeed post;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Relationship relationship;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private User user;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private UserProfile userProfile;

  /**
   * @return value or {@code null} for none
   */
  public PostFeed getPost() {
    return post;
  }

  /**
   * @param post post or {@code null} for none
   */
  public ProfileView setPost(PostFeed post) {
    this.post = post;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public Relationship getRelationship() {
    return relationship;
  }

  /**
   * @param relationship relationship or {@code null} for none
   */
  public ProfileView setRelationship(Relationship relationship) {
    this.relationship = relationship;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public User getUser() {
    return user;
  }

  /**
   * @param user user or {@code null} for none
   */
  public ProfileView setUser(User user) {
    this.user = user;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public UserProfile getUserProfile() {
    return userProfile;
  }

  /**
   * @param userProfile userProfile or {@code null} for none
   */
  public ProfileView setUserProfile(UserProfile userProfile) {
    this.userProfile = userProfile;
    return this;
  }

  @Override
  public ProfileView set(String fieldName, Object value) {
    return (ProfileView) super.set(fieldName, value);
  }

  @Override
  public ProfileView clone() {
    return (ProfileView) super.clone();
  }

}

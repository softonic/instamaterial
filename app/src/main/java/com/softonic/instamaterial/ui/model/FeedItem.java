package com.softonic.instamaterial.ui.model;

import java.util.List;

public class FeedItem {
  private final String photoId;
  private final String photoSourceUrl;
  private final String photoDescription;
  private final String userId;
  private final String userNickname;
  private final String userAvatarUrl;
  private final List<String> userLikes;

  private FeedItem(Builder builder) {
    photoId = builder.photoId;
    photoSourceUrl = builder.photoSourceUrl;
    photoDescription = builder.photoDescription;
    userId = builder.userId;
    userNickname = builder.userNickname;
    userAvatarUrl = builder.userAvatarUrl;
    userLikes = builder.userLikes;
  }

  public String getPhotoId() {
    return photoId;
  }

  public String getPhotoSourceUrl() {
    return photoSourceUrl;
  }

  public String getPhotoDescription() {
    return photoDescription;
  }

  public String getUserId() {
    return userId;
  }

  public String getUserNickname() {
    return userNickname;
  }

  public String getUserAvatarUrl() {
    return userAvatarUrl;
  }

  public List<String> getUserLikes() {
    return userLikes;
  }

  public static Builder Builder() {
    return new Builder();
  }

  public static class Builder {
    private String photoId;
    private String photoSourceUrl;
    private String photoDescription;
    private String userId;
    private String userNickname;
    private String userAvatarUrl;
    private List<String> userLikes;

    private Builder() {

    }

    public Builder photoId(String photoId) {
      this.photoId = photoId;
      return this;
    }

    public Builder photoSourceUrl(String photoSourceUrl) {
      this.photoSourceUrl = photoSourceUrl;
      return this;
    }

    public Builder photoDescription(String photoDescription) {
      this.photoDescription = photoDescription;
      return this;
    }

    public Builder userId(String userId) {
      this.userId = userId;
      return this;
    }

    public Builder userNickname(String userNickname) {
      this.userNickname = userNickname;
      return this;
    }

    public Builder userAvatarUrl(String userAvatarUrl) {
      this.userAvatarUrl = userAvatarUrl;
      return this;
    }

    public Builder userLikes(List<String> userLikes) {
      this.userLikes = userLikes;
      return this;
    }

    public FeedItem build() {
      return new FeedItem(this);
    }
  }
}

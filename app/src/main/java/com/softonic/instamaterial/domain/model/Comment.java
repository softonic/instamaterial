package com.softonic.instamaterial.domain.model;

public class Comment {
  private String id;
  private String photoId;
  private String userId;
  private String content;

  private Comment(Builder builder) {
    id = builder.id;
    photoId = builder.photoId;
    userId = builder.userId;
    content = builder.content;
  }

  public String getId() {
    return id;
  }

  public String getPhotoId() {
    return photoId;
  }

  public String getUserId() {
    return userId;
  }

  public String getContent() {
    return content;
  }

  public static Builder Builder() {
    return new Builder();
  }

  public static class Builder {
    private String id;
    private String photoId;
    private String userId;
    private String content;

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder photoId(String photoId) {
      this.photoId = photoId;
      return this;
    }

    public Builder userId(String userId) {
      this.userId = userId;
      return this;
    }

    public Builder content(String content) {
      this.content = content;
      return this;
    }

    public Comment build() {
      return new Comment(this);
    }
  }
}

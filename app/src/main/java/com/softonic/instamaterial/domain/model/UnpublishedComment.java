package com.softonic.instamaterial.domain.model;

public class UnpublishedComment {
  private String photoId;
  private String userId;
  private String content;

  private UnpublishedComment(Builder builder) {
    photoId = builder.photoId;
    userId = builder.userId;
    content = builder.content;
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
    private String photoId;
    private String userId;
    private String content;

    private Builder() {
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

    public UnpublishedComment build() {
      return new UnpublishedComment(this);
    }
  }
}

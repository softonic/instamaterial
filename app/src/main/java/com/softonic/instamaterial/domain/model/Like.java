package com.softonic.instamaterial.domain.model;

public class Like {
  private String photoId;
  private String userId;

  private Like(Builder builder) {
    photoId = builder.photoId;
    userId = builder.userId;
  }

  public String getPhotoId() {
    return photoId;
  }

  public String getUserId() {
    return userId;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Like like = (Like) o;

    if (photoId != null ? !photoId.equals(like.photoId) : like.photoId != null) return false;
    return userId != null ? userId.equals(like.userId) : like.userId == null;
  }

  @Override public int hashCode() {
    int result = photoId != null ? photoId.hashCode() : 0;
    result = 31 * result + (userId != null ? userId.hashCode() : 0);
    return result;
  }

  public static Like.Builder Builder() {
    return new Like.Builder();
  }

  public static class Builder {
    private String photoId;
    private String userId;

    public Builder photoId(String photoId) {
      this.photoId = photoId;
      return this;
    }

    public Builder userId(String userId) {
      this.userId = userId;
      return this;
    }

    public Like build() {
      return new Like(this);
    }
  }
}

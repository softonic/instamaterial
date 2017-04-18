package com.softonic.instamaterial.domain.model;

public class User {
  private String id;
  private String username;
  private String photoUrl;

  private User(Builder builder) {
    id = builder.id;
    username = builder.username;
    photoUrl = builder.photoUrl;
  }

  public String getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public static Builder Builder() {
    return new Builder();
  }

  public static class Builder {
    private String id;
    private String username;
    private String photoUrl;

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder username(String nickname) {
      this.username = nickname;
      return this;
    }

    public Builder photoUrl(String avatarUrl) {
      this.photoUrl = avatarUrl;
      return this;
    }

    public User build() {
      return new User(this);
    }
  }
}

package com.softonic.instamaterial.domain.model;

public class User {
  private String id;
  private String nickname;
  private String avatarUrl;

  private User(Builder builder) {
    id = builder.id;
    nickname = builder.nickname;
    avatarUrl = builder.avatarUrl;
  }

  public String getId() {
    return id;
  }

  public String getNickname() {
    return nickname;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public static Builder Builder() {
    return new Builder();
  }

  public static class Builder {
    private String id;
    private String nickname;
    private String avatarUrl;

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder nickname(String nickname) {
      this.nickname = nickname;
      return this;
    }

    public Builder avatarUrl(String avatarUrl) {
      this.avatarUrl = avatarUrl;
      return this;
    }

    public User build() {
      return new User(this);
    }
  }
}

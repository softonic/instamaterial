package com.softonic.instamaterial.ui.model;

public class LikeItem {
  private final String userId;

  private LikeItem(Builder builder) {
    userId = builder.userId;
  }

  public String getUserId() {
    return userId;
  }

  public static Builder Builder() {
    return new Builder();
  }

  public static class Builder {
    private String userId;

    private Builder() {
    }

    public Builder userId(String userId) {
      this.userId = userId;
      return this;
    }

    public LikeItem build() {
      return new LikeItem(this);
    }
  }
}

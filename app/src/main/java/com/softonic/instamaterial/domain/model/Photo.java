package com.softonic.instamaterial.domain.model;

public class Photo {
  private String id;
  private String userId;
  private String sourceUrl;
  private String description;

  private Photo(Builder builder) {
    id = builder.id;
    userId = builder.userId;
    sourceUrl = builder.sourceUrl;
    description = builder.description;
  }

  public String getId() {
    return id;
  }

  public String getUserId() {
    return userId;
  }

  public String getSourceUrl() {
    return sourceUrl;
  }

  public String getDescription() {
    return description;
  }

  public static Builder Builder() {
    return new Builder();
  }

  public static class Builder {
    private String id;
    private String userId;
    private String sourceUrl;
    private String description;

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder userId(String userId) {
      this.userId = userId;
      return this;
    }

    public Builder sourceUrl(String sourceUrl) {
      this.sourceUrl = sourceUrl;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Photo build() {
      return new Photo(this);
    }
  }
}

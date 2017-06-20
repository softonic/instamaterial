package com.softonic.instamaterial.domain.model;

public class Publication {
  private final String userId;
  private final String photoUri;
  private final String description;

  private Publication(Builder builder) {
    userId = builder.userId;
    photoUri = builder.photoUri;
    description = builder.description;
  }

  public String getUserId() {
    return userId;
  }

  public String getPhotoUri() {
    return photoUri;
  }

  public String getDescription() {
    return description;
  }

  public Publication withPhotoUri(String photoUri) {
    return Builder()
        .userId(userId)
        .photoUri(photoUri)
        .description(description)
        .build();
  }

  public static Builder Builder() {
    return new Builder();
  }

  public static class Builder {
    private String userId;
    private String photoUri;
    private String description;

    private Builder() {

    }

    public Builder userId(String userId) {
      this.userId = userId;
      return this;
    }

    public Builder photoUri(String photoUri) {
      this.photoUri = photoUri;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Publication build() {
      return new Publication(this);
    }
  }
}

package com.softonic.instamaterial.data.repository.user;

public class UserData {
  private String displayName;
  private String photoUrl;

  @SuppressWarnings("unused")
  UserData() {
  }

  UserData(String displayName, String photoUrl) {
    this.displayName = displayName;
    this.photoUrl = photoUrl;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }
}

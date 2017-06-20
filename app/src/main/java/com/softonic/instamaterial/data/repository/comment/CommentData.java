package com.softonic.instamaterial.data.repository.comment;

public class CommentData {
  private String userId;
  private String content;

  @SuppressWarnings("unused") CommentData() {
  }

  CommentData(String userId, String content) {
    this.userId = userId;
    this.content = content;
  }

  public String getUserId() {
    return userId;
  }

  public String getContent() {
    return content;
  }
}

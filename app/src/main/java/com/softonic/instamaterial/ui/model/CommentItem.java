package com.softonic.instamaterial.ui.model;

public class CommentItem {
  private final String commentId;
  private final String userNickname;
  private final String userAvatarUrl;
  private final String content;

  private CommentItem(Builder builder) {
    commentId = builder.commentId;
    userNickname = builder.userNickname;
    userAvatarUrl = builder.userAvatarUrl;
    content = builder.content;
  }

  public String getCommentId() {
    return commentId;
  }

  public String getUserNickname() {
    return userNickname;
  }

  public String getUserAvatarUrl() {
    return userAvatarUrl;
  }

  public String getContent() {
    return content;
  }

  public static Builder Builder() {
    return new Builder();
  }

  public static class Builder {
    private String commentId;
    private String userNickname;
    private String userAvatarUrl;
    private String content;

    private Builder() {

    }

    public Builder commentId(String commentId) {
      this.commentId = commentId;
      return this;
    }

    public Builder userNickname(String userNickname) {
      this.userNickname = userNickname;
      return this;
    }

    public Builder userAvatarUrl(String userAvatarUrl) {
      this.userAvatarUrl = userAvatarUrl;
      return this;
    }

    public Builder content(String content) {
      this.content = content;
      return this;
    }

    public CommentItem build() {
      return new CommentItem(this);
    }
  }
}

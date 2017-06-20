package com.softonic.instamaterial.data.repository.comment;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.model.Comment;
import com.softonic.instamaterial.domain.model.CommentPublication;
import com.softonic.instamaterial.domain.repository.CommentRepository;
import java.util.List;

public class CommentDataRepository implements CommentRepository {
  private final CommentDataSource commentDataSource;

  public CommentDataRepository(CommentDataSource commentDataSource) {
    this.commentDataSource = commentDataSource;
  }

  @Override public ObservableTask<List<Comment>> getComments(String photoId) {
    return commentDataSource.getComments(photoId);
  }

  @Override public ObservableTask<Comment> publishComment(CommentPublication commentPublication) {
    return commentDataSource.publishComment(commentPublication);
  }

  @Override public ObservableTask<Comment> addCommentNotifier(String photoId) {
    return commentDataSource.addCommentNotifier(photoId);
  }

  @Override public ObservableTask<Boolean> removeCommentNotifier(String photoId) {
    return commentDataSource.removeCommentNotifier(photoId);
  }
}

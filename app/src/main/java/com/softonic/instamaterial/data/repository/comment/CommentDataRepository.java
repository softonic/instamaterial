package com.softonic.instamaterial.data.repository.comment;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.model.Comment;
import com.softonic.instamaterial.domain.model.UnpublishedComment;
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

  @Override public ObservableTask<Comment> publishComment(UnpublishedComment unpublishedComment) {
    return commentDataSource.publishComment(unpublishedComment);
  }
}

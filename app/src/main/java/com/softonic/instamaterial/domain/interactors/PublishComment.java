package com.softonic.instamaterial.domain.interactors;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.UseCase;
import com.softonic.instamaterial.domain.executor.UseCaseExecutor;
import com.softonic.instamaterial.domain.model.Comment;
import com.softonic.instamaterial.domain.model.UnpublishedComment;
import com.softonic.instamaterial.domain.repository.CommentRepository;

public class PublishComment extends UseCase<UnpublishedComment, Comment> {
  private final CommentRepository commentRepository;

  public PublishComment(UseCaseExecutor useCaseExecutor, CommentRepository commentRepository) {
    super(useCaseExecutor);
    this.commentRepository = commentRepository;
  }

  @Override
  public ObservableTask<Comment> createObservableTask(final UnpublishedComment unpublishedComment) {
    return commentRepository.publishComment(unpublishedComment);
  }
}

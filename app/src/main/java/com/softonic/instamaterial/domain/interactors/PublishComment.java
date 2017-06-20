package com.softonic.instamaterial.domain.interactors;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.UseCase;
import com.softonic.instamaterial.domain.executor.UseCaseExecutor;
import com.softonic.instamaterial.domain.model.Comment;
import com.softonic.instamaterial.domain.model.CommentPublication;
import com.softonic.instamaterial.domain.repository.CommentRepository;

public class PublishComment extends UseCase<CommentPublication, Comment> {
  private final CommentRepository commentRepository;

  public PublishComment(UseCaseExecutor useCaseExecutor, CommentRepository commentRepository) {
    super(useCaseExecutor);
    this.commentRepository = commentRepository;
  }

  @Override
  public ObservableTask<Comment> createObservableTask(final CommentPublication commentPublication) {
    return commentRepository.publishComment(commentPublication);
  }
}

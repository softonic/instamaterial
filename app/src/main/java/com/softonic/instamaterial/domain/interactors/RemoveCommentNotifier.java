package com.softonic.instamaterial.domain.interactors;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.UseCase;
import com.softonic.instamaterial.domain.executor.UseCaseExecutor;
import com.softonic.instamaterial.domain.repository.CommentRepository;

public class RemoveCommentNotifier extends UseCase<String, Boolean> {
  private final CommentRepository commentRepository;

  public RemoveCommentNotifier(
      UseCaseExecutor useCaseExecutor, CommentRepository commentRepository) {
    super(useCaseExecutor);
    this.commentRepository = commentRepository;
  }

  @Override public ObservableTask<Boolean> createObservableTask(String photoId) {
    return commentRepository.removeCommentNotifier(photoId);
  }
}

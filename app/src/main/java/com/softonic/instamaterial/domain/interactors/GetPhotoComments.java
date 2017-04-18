package com.softonic.instamaterial.domain.interactors;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.UseCase;
import com.softonic.instamaterial.domain.executor.UseCaseExecutor;
import com.softonic.instamaterial.domain.model.Comment;
import com.softonic.instamaterial.domain.repository.CommentRepository;
import java.util.List;

public class GetPhotoComments extends UseCase<String, List<Comment>> {
  private final CommentRepository commentRepository;

  public GetPhotoComments(
      UseCaseExecutor useCaseExecutor, CommentRepository commentRepository) {
    super(useCaseExecutor);
    this.commentRepository = commentRepository;
  }

  @Override public ObservableTask<List<Comment>> createObservableTask(final String photoId) {
    return commentRepository.getComments(photoId);
  }
}

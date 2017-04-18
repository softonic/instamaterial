package com.softonic.instamaterial.domain.interactors;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.UseCase;
import com.softonic.instamaterial.domain.executor.UseCaseExecutor;
import com.softonic.instamaterial.domain.model.Like;
import com.softonic.instamaterial.domain.repository.LikeRepository;

public class LikePhoto extends UseCase<Like, Boolean> {
  private final LikeRepository likeRepository;

  public LikePhoto(UseCaseExecutor useCaseExecutor, LikeRepository likeRepository) {
    super(useCaseExecutor);
    this.likeRepository = likeRepository;
  }

  @Override public ObservableTask<Boolean> createObservableTask(final Like like) {
    return likeRepository.toggleLike(like);
  }
}
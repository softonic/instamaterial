package com.softonic.instamaterial.domain.interactors;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.UseCase;
import com.softonic.instamaterial.domain.executor.UseCaseExecutor;
import com.softonic.instamaterial.domain.repository.LikeRepository;

public class RemoveLikeNotifier extends UseCase<String, Boolean> {
  private final LikeRepository likeRepository;

  public RemoveLikeNotifier(
      UseCaseExecutor useCaseExecutor, LikeRepository likeRepository) {
    super(useCaseExecutor);
    this.likeRepository = likeRepository;
  }

  @Override public ObservableTask<Boolean> createObservableTask(String photoId) {
    return likeRepository.removeLikeNotifier(photoId);
  }
}

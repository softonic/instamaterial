package com.softonic.instamaterial.domain.interactors;

import android.support.v4.util.Pair;
import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.UseCase;
import com.softonic.instamaterial.domain.executor.UseCaseExecutor;
import com.softonic.instamaterial.domain.model.Like;
import com.softonic.instamaterial.domain.repository.LikeRepository;

public class AddLikeNotifier extends UseCase<String, Pair<Like, Boolean>> {
  private final LikeRepository likeRepository;

  public AddLikeNotifier(UseCaseExecutor useCaseExecutor, LikeRepository likeRepository) {
    super(useCaseExecutor);
    this.likeRepository = likeRepository;
  }

  @Override public ObservableTask<Pair<Like, Boolean>> createObservableTask(String photoId) {
    return likeRepository.addLikeNotifier(photoId);
  }
}

package com.softonic.instamaterial.domain.interactors;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.UseCase;
import com.softonic.instamaterial.domain.executor.UseCaseExecutor;
import com.softonic.instamaterial.domain.model.Like;
import com.softonic.instamaterial.domain.repository.LikeRepository;
import java.util.List;

public class GetPhotoLikes extends UseCase<String, List<Like>> {
  private final LikeRepository likeRepository;

  public GetPhotoLikes(UseCaseExecutor useCaseExecutor, LikeRepository likeRepository) {
    super(useCaseExecutor);
    this.likeRepository = likeRepository;
  }

  @Override public ObservableTask<List<Like>> createObservableTask(final String photoId) {
    return likeRepository.getLikes(photoId);
  }
}

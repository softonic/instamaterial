package com.softonic.instamaterial.domain.interactors;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.UseCase;
import com.softonic.instamaterial.domain.executor.UseCaseExecutor;
import com.softonic.instamaterial.domain.model.Photo;
import com.softonic.instamaterial.domain.repository.PhotoRepository;

public class GetPhoto extends UseCase<String, Photo> {
  private final PhotoRepository photoRepository;

  public GetPhoto(UseCaseExecutor useCaseExecutor, PhotoRepository photoRepository) {
    super(useCaseExecutor);
    this.photoRepository = photoRepository;
  }

  @Override public ObservableTask<Photo> createObservableTask(final String photoId) {
    return photoRepository.getPhoto(photoId);
  }
}

package com.softonic.instamaterial.domain.interactors;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.UseCase;
import com.softonic.instamaterial.domain.executor.UseCaseExecutor;
import com.softonic.instamaterial.domain.model.Photo;
import com.softonic.instamaterial.domain.repository.PhotoRepository;

public class AddPhotoNotifier extends UseCase<Void, Photo> {
  private final PhotoRepository photoRepository;

  public AddPhotoNotifier(
      UseCaseExecutor useCaseExecutor, PhotoRepository photoRepository) {
    super(useCaseExecutor);
    this.photoRepository = photoRepository;
  }

  @Override public ObservableTask<Photo> createObservableTask(Void input) {
    return photoRepository.addPhotoNotifier();
  }
}

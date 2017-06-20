package com.softonic.instamaterial.domain.interactors;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.UseCase;
import com.softonic.instamaterial.domain.executor.UseCaseExecutor;
import com.softonic.instamaterial.domain.model.Photo;
import com.softonic.instamaterial.domain.repository.PhotoRepository;
import java.util.List;

public class GetPhotos extends UseCase<Void, List<Photo>> {
  private final PhotoRepository photoRepository;

  public GetPhotos(UseCaseExecutor useCaseExecutor, PhotoRepository photoRepository) {
    super(useCaseExecutor);
    this.photoRepository = photoRepository;
  }

  @Override public ObservableTask<List<Photo>> createObservableTask(Void input) {
    return photoRepository.getPhotos();
  }
}

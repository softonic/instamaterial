package com.softonic.instamaterial.domain.interactors;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.Subscriber;
import com.softonic.instamaterial.domain.common.UseCase;
import com.softonic.instamaterial.domain.executor.UseCaseExecutor;
import com.softonic.instamaterial.domain.model.Photo;
import com.softonic.instamaterial.domain.model.Publication;
import com.softonic.instamaterial.domain.repository.PhotoRepository;

public class PublishPhoto extends UseCase<Publication, Photo> {
  private final UploadPhoto uploadPhoto;
  private final PhotoRepository photoRepository;

  public PublishPhoto(UseCaseExecutor useCaseExecutor, UploadPhoto uploadPhoto,
      PhotoRepository photoRepository) {
    super(useCaseExecutor);
    this.uploadPhoto = uploadPhoto;
    this.photoRepository = photoRepository;
  }

  @Override public ObservableTask<Photo> createObservableTask(final Publication publication) {
    return new ObservableTask<Photo>() {
      @Override public void run(Subscriber<Photo> result) {
        String photoUri = uploadPhoto.createObservableTask(publication.getPhotoUri()).getResult();
        photoRepository.publishPhoto(publication.withPhotoUri(photoUri)).run(result);
      }
    };
  }
}

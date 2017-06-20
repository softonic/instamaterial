package com.softonic.instamaterial.data.repository.photo;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.model.Photo;
import com.softonic.instamaterial.domain.model.Publication;
import com.softonic.instamaterial.domain.repository.PhotoRepository;
import java.util.List;

public class PhotoDataRepository implements PhotoRepository {
  private final PhotoDataSource photoDataSource;

  public PhotoDataRepository(PhotoDataSource photoDataSource) {
    this.photoDataSource = photoDataSource;
  }

  @Override public ObservableTask<Photo> getPhoto(String photoId) {
    return photoDataSource.getPhoto(photoId);
  }

  @Override public ObservableTask<List<Photo>> getPhotos() {
    return photoDataSource.getPhotos();
  }

  @Override public ObservableTask<Photo> publishPhoto(Publication publication) {
    return photoDataSource.publishPhoto(publication);
  }

  @Override public ObservableTask<String> uploadPhoto(String photoUri) {
    return photoDataSource.uploadPhoto(photoUri);
  }

  @Override public ObservableTask<Photo> addPhotoNotifier() {
    return photoDataSource.addPhotoNotifier();
  }

  @Override public ObservableTask<Boolean> removePhotoNotifier() {
    return photoDataSource.removePhotoNotifier();
  }
}

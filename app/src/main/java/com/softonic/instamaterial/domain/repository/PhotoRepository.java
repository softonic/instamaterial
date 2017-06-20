package com.softonic.instamaterial.domain.repository;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.model.Photo;
import com.softonic.instamaterial.domain.model.Publication;
import java.util.List;

public interface PhotoRepository {
  ObservableTask<Photo> getPhoto(String photoId);

  ObservableTask<List<Photo>> getPhotos();

  ObservableTask<Photo> publishPhoto(Publication publication);

  ObservableTask<String> uploadPhoto(String photoUri);

  ObservableTask<Photo> addPhotoNotifier();

  ObservableTask<Boolean> removePhotoNotifier();
}

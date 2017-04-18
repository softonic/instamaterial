package com.softonic.instamaterial.data.repository.photo;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.model.Photo;
import com.softonic.instamaterial.domain.model.UnpublishedPhoto;
import java.util.List;

public interface PhotoDataSource {
  ObservableTask<Photo> getPhoto(String photoId);

  ObservableTask<List<Photo>> getPhotos();

  ObservableTask<Photo> publishPhoto(UnpublishedPhoto unpublishedPhoto);

  ObservableTask<String> uploadPhoto(String photoUri);
}

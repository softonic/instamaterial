package com.softonic.instamaterial.data.repository.like;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.model.Like;
import java.util.List;

public interface LikeDataSource {
  ObservableTask<List<Like>> getLikes(String photoId);

  ObservableTask<Boolean> toggleLike(Like like);
}

package com.softonic.instamaterial.domain.repository;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.model.Like;
import java.util.List;

public interface LikeRepository {
  ObservableTask<List<Like>> getLikes(String photoId);

  ObservableTask<Boolean> toggleLike(Like like);
}

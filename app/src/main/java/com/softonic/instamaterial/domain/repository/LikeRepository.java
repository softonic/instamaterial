package com.softonic.instamaterial.domain.repository;

import android.support.v4.util.Pair;
import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.model.Like;
import java.util.List;

public interface LikeRepository {
  ObservableTask<List<Like>> getLikes(String photoId);

  ObservableTask<Boolean> toggleLike(Like like);

  ObservableTask<Pair<Like, Boolean>> addLikeNotifier(String photoId);

  ObservableTask<Boolean> removeLikeNotifier(String photoId);
}

package com.softonic.instamaterial.data.repository.like;

import android.support.v4.util.Pair;
import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.model.Like;
import com.softonic.instamaterial.domain.repository.LikeRepository;
import java.util.List;

public class LikeDataRepository implements LikeRepository {
  private final LikeDataSource likeDataSource;

  public LikeDataRepository(LikeDataSource likeDataSource) {
    this.likeDataSource = likeDataSource;
  }

  @Override public ObservableTask<List<Like>> getLikes(String photoId) {
    return likeDataSource.getLikes(photoId);
  }

  @Override public ObservableTask<Boolean> toggleLike(Like like) {
    return likeDataSource.toggleLike(like);
  }

  @Override public ObservableTask<Pair<Like, Boolean>> addLikeNotifier(String photoId) {
    return likeDataSource.addLikeNotifier(photoId);
  }

  @Override public ObservableTask<Boolean> removeLikeNotifier(String photoId) {
    return likeDataSource.removeLikeNotifier(photoId);
  }
}

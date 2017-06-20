package com.softonic.instamaterial.data.repository.like;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.Subscriber;
import com.softonic.instamaterial.domain.model.Like;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class FakeLikeDataSource implements LikeDataSource {
  private static final String PHOTO_ID1 = "507f191e810c19729de860e1";
  private static final String PHOTO_ID2 = "507f191e810c19729de860e2";

  private static final String USER_ID1 = "8d57a927b98c4add82e61ea1";
  private static final String USER_ID2 = "8d57a927b98c4add82e61ea2";
  private static final String USER_ID3 = "8d57a927b98c4add82e61ea3";
  private static final String USER_ID4 = "8d57a927b98c4add82e61ea4";
  private static final String USER_ID5 = "8d57a927b98c4add82e61ea5";
  private static final String USER_ID6 = "8d57a927b98c4add82e61ea6";
  private static final String USER_ID7 = "8d57a927b98c4add82e61ea7";
  private static final String USER_ID8 = "8d57a927b98c4add82e61ea8";
  private static final String USER_ID9 = "8d57a927b98c4add82e61ea9";

  private Map<String, List<Like>> mapPhotoLikes = new HashMap<>();

  public FakeLikeDataSource() {
    mapPhotoLikes.put(PHOTO_ID1,
        new LinkedList<>(Arrays.asList(
            Like.Builder().photoId(PHOTO_ID1).userId(USER_ID2).build(),
            Like.Builder().photoId(PHOTO_ID1).userId(USER_ID4).build(),
            Like.Builder().photoId(PHOTO_ID1).userId(USER_ID6).build(),
            Like.Builder().photoId(PHOTO_ID1).userId(USER_ID8).build()
        )));
    mapPhotoLikes.put(PHOTO_ID2,
        new LinkedList<>(Arrays.asList(
            Like.Builder().photoId(PHOTO_ID2).userId(USER_ID1).build(),
            Like.Builder().photoId(PHOTO_ID2).userId(USER_ID3).build(),
            Like.Builder().photoId(PHOTO_ID2).userId(USER_ID5).build(),
            Like.Builder().photoId(PHOTO_ID2).userId(USER_ID7).build(),
            Like.Builder().photoId(PHOTO_ID2).userId(USER_ID9).build()
        )));
  }

  @Override public ObservableTask<List<Like>> getLikes(final String photoId) {
    return new ObservableTask<List<Like>>() {
      @Override public void run(Subscriber<List<Like>> result) {
        if (mapPhotoLikes.containsKey(photoId)) {
          result.onSuccess(mapPhotoLikes.get(photoId));
        } else {
          result.onError(new NoSuchElementException());
        }
      }
    };
  }

  @Override public ObservableTask<Boolean> toggleLike(@NonNull final Like like) {
    return new ObservableTask<Boolean>() {
      @Override public void run(Subscriber<Boolean> result) {
        boolean added = false;
        if (mapPhotoLikes.containsKey(like.getPhotoId())) {
          List<Like> photoLikes = mapPhotoLikes.get(like.getPhotoId());
          if (!photoLikes.contains(like)) {
            photoLikes.add(like);
            added = true;
          } else {
            photoLikes.remove(like);
          }
        } else {
          List<Like> photoLikes = new LinkedList<>();
          photoLikes.add(like);
          mapPhotoLikes.put(like.getPhotoId(), photoLikes);
          added = true;
        }
        result.onSuccess(added);
      }
    };
  }

  @Override public ObservableTask<Pair<Like, Boolean>> addLikeNotifier(String photoId) {
    return null;
  }

  @Override public ObservableTask<Boolean> removeLikeNotifier(String photoId) {
    return null;
  }
}

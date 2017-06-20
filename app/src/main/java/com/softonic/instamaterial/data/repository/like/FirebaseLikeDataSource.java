package com.softonic.instamaterial.data.repository.like;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.softonic.instamaterial.data.repository.commons.ChangeEventListener;
import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.Subscriber;
import com.softonic.instamaterial.domain.model.Like;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FirebaseLikeDataSource implements LikeDataSource {
  private final Map<String, ChangeEventListener<Long>> listenerMap = new HashMap<>();

  @Override public ObservableTask<List<Like>> getLikes(final String photoId) {
    return new ObservableTask<List<Like>>() {
      @Override public void run(final Subscriber<List<Like>> result) {
        final DatabaseReference likesRef = FirebaseDatabase.getInstance()
            .getReference("Photos")
            .child(photoId)
            .child("Likes");
        likesRef.addValueEventListener(new ValueEventListener() {
          @Override public void onDataChange(DataSnapshot dataSnapshot) {
            List<Like> likes = new LinkedList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
              likes.add(
                  new Like.Builder()
                      .photoId(photoId)
                      .userId(snapshot.getKey())
                      .build());
            }
            result.onSuccess(likes);
            likesRef.removeEventListener(this);
          }

          @Override public void onCancelled(DatabaseError databaseError) {
            result.onError(databaseError.toException());
            likesRef.removeEventListener(this);
          }
        });
      }
    };
  }

  @Override public ObservableTask<Boolean> toggleLike(final Like like) {
    return new ObservableTask<Boolean>() {
      @Override public void run(final Subscriber<Boolean> result) {
        final DatabaseReference likesRef = FirebaseDatabase.getInstance()
            .getReference("Photos")
            .child(like.getPhotoId())
            .child("Likes");
        likesRef.addValueEventListener(new ValueEventListener() {
          @Override public void onDataChange(DataSnapshot dataSnapshot) {
            likesRef.removeEventListener(this);
            if (dataSnapshot.hasChild(like.getUserId())) {
              likesRef
                  .child(like.getUserId())
                  .removeValue()
                  .addOnFailureListener(new OnFailureListener() {
                    @Override public void onFailure(@NonNull Exception e) {
                      result.onError(e);
                    }
                  })
                  .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override public void onComplete(@NonNull Task<Void> task) {
                      result.onSuccess(false);
                    }
                  });
            } else {
              likesRef
                  .child(like.getUserId())
                  .setValue(ServerValue.TIMESTAMP)
                  .addOnFailureListener(new OnFailureListener() {
                    @Override public void onFailure(@NonNull Exception e) {
                      result.onError(e);
                    }
                  })
                  .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override public void onComplete(@NonNull Task<Void> task) {
                      result.onSuccess(true);
                    }
                  });
            }
          }

          @Override public void onCancelled(DatabaseError databaseError) {
            result.onError(databaseError.toException());
            likesRef.removeEventListener(this);
          }
        });
      }
    };
  }

  @Override public ObservableTask<Pair<Like, Boolean>> addLikeNotifier(final String photoId) {
    return new ObservableTask<Pair<Like, Boolean>>() {
      @Override public void run(final Subscriber<Pair<Like, Boolean>> result) {
        DatabaseReference likesRef = FirebaseDatabase.getInstance()
            .getReference("Photos")
            .child(photoId)
            .child("Likes");

        if (listenerMap.containsKey(photoId)) {
          likesRef.removeEventListener(listenerMap.get(photoId));
          listenerMap.remove(photoId);
        }

        ChangeEventListener<Long> listener =
            new ChangeEventListener<Long>(likesRef, Long.class) {
              @Override protected void onChildAdded(String key, Long data) {
                Like like = Like.Builder()
                    .photoId(photoId)
                    .userId(key)
                    .build();
                result.onSuccess(new Pair<>(like, true));
              }

              @Override protected void onChildRemoved(String key, Long data) {
                Like like = Like.Builder()
                    .photoId(photoId)
                    .userId(key)
                    .build();
                result.onSuccess(new Pair<>(like, false));
              }
            };
        listenerMap.put(photoId, listener);
      }
    };
  }

  @Override public ObservableTask<Boolean> removeLikeNotifier(final String photoId) {
    return new ObservableTask<Boolean>() {
      @Override public void run(Subscriber<Boolean> result) {
        if (listenerMap.containsKey(photoId)) {
          DatabaseReference likesRef = FirebaseDatabase.getInstance()
              .getReference("Photos")
              .child(photoId)
              .child("Likes");
          likesRef.removeEventListener(listenerMap.get(photoId));
          listenerMap.remove(photoId);
        }
      }
    };
  }
}

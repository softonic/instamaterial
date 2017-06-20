package com.softonic.instamaterial.data.repository.comment;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softonic.instamaterial.data.repository.commons.AdditionEventListener;
import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.Subscriber;
import com.softonic.instamaterial.domain.model.Comment;
import com.softonic.instamaterial.domain.model.CommentPublication;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FirebaseCommentDataSource implements CommentDataSource {
  private final Map<String, AdditionEventListener<CommentData>> listenerMap = new HashMap<>();

  @Override public ObservableTask<List<Comment>> getComments(final String photoId) {
    return new ObservableTask<List<Comment>>() {
      @Override public void run(final Subscriber<List<Comment>> result) {
        final DatabaseReference commentsRef = FirebaseDatabase.getInstance()
            .getReference("Photos")
            .child(photoId)
            .child("Comments");
        commentsRef.addValueEventListener(new ValueEventListener() {
          @Override public void onDataChange(DataSnapshot dataSnapshot) {
            List<Comment> comments = new LinkedList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
              CommentData commentData = snapshot.getValue(CommentData.class);
              comments.add(createComment(photoId, snapshot.getKey(), commentData));
            }
            result.onSuccess(comments);
            commentsRef.removeEventListener(this);
          }

          @Override public void onCancelled(DatabaseError databaseError) {
            result.onError(databaseError.toException());
            commentsRef.removeEventListener(this);
          }
        });
      }
    };
  }

  @Override
  public ObservableTask<Comment> publishComment(final CommentPublication commentPublication) {
    return new ObservableTask<Comment>() {
      @Override public void run(final Subscriber<Comment> result) {
        DatabaseReference commentsRef = FirebaseDatabase.getInstance()
            .getReference("Photos")
            .child(commentPublication.getPhotoId())
            .child("Comments");
        final DatabaseReference commentRef = commentsRef.push();
        CommentData commentData = createCommentData(commentPublication);
        commentRef
            .setValue(commentData)
            .addOnFailureListener(new OnFailureListener() {
              @Override public void onFailure(@NonNull Exception e) {
                result.onError(e);
              }
            })
            .addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override public void onComplete(@NonNull Task<Void> task) {
                result.onSuccess(Comment.Builder()
                    .id(commentRef.getKey())
                    .photoId(commentPublication.getPhotoId())
                    .userId(commentPublication.getUserId())
                    .content(commentPublication.getContent())
                    .build());
              }
            });
      }
    };
  }

  @Override public ObservableTask<Comment> addCommentNotifier(final String photoId) {
    return new ObservableTask<Comment>() {
      @Override public void run(final Subscriber<Comment> result) {
        DatabaseReference photosRef = FirebaseDatabase.getInstance()
            .getReference("Photos")
            .child(photoId)
            .child("Comments");

        if (listenerMap.containsKey(photoId)) {
          photosRef.removeEventListener(listenerMap.get(photoId));
          listenerMap.remove(photoId);
        }

        AdditionEventListener<CommentData> listener =
            new AdditionEventListener<CommentData>(photosRef, CommentData.class) {
              @Override protected void onChildAdded(String commentId, CommentData commentData) {
                result.onSuccess(createComment(photoId, commentId, commentData));
              }
            };
        listenerMap.put(photoId, listener);
      }
    };
  }

  @Override public ObservableTask<Boolean> removeCommentNotifier(final String photoId) {
    return new ObservableTask<Boolean>() {
      @Override public void run(Subscriber<Boolean> result) {
        if (listenerMap.containsKey(photoId)) {
          DatabaseReference photosRef = FirebaseDatabase.getInstance()
              .getReference("Photos")
              .child(photoId)
              .child("Comments");
          photosRef.removeEventListener(listenerMap.get(photoId));
          listenerMap.remove(photoId);
        }
      }
    };
  }

  private CommentData createCommentData(CommentPublication commentPublication) {
    return new CommentData(
        commentPublication.getUserId(),
        commentPublication.getContent());
  }

  private Comment createComment(String photoId, String commentId, CommentData commentData) {
    return Comment.Builder()
        .id(commentId)
        .photoId(photoId)
        .userId(commentData.getUserId())
        .content(commentData.getContent())
        .build();
  }
}

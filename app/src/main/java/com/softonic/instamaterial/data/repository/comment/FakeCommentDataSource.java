package com.softonic.instamaterial.data.repository.comment;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.Subscriber;
import com.softonic.instamaterial.domain.model.Comment;
import com.softonic.instamaterial.domain.model.UnpublishedComment;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FakeCommentDataSource implements CommentDataSource {
  private static final String ID1 = "2e5f8127b98c4add82ep6t1";
  private static final String ID2 = "2e5f8127b98c4add82ep6t2";
  private static final String ID3 = "2e5f8127b98c4add82ep6t3";
  private static final String ID4 = "2e5f8127b98c4add82ep6t4";
  private static final String ID5 = "2e5f8127b98c4add82ep6t5";
  private static final String ID6 = "2e5f8127b98c4add82ep6t6";
  private static final String ID7 = "2e5f8127b98c4add82ep6t7";
  private static final String ID8 = "2e5f8127b98c4add82ep6t8";
  private static final String ID9 = "2e5f8127b98c4add82ep6t9";

  private static final String PHOTO1 = "507f191e810c19729de860e1";
  private static final String PHOTO2 = "507f191e810c19729de860e2";

  private static final String USER1 = "8d57a927b98c4add82e61ea1";
  private static final String USER2 = "8d57a927b98c4add82e61ea2";
  private static final String USER3 = "8d57a927b98c4add82e61ea3";
  private static final String USER4 = "8d57a927b98c4add82e61ea4";
  private static final String USER5 = "8d57a927b98c4add82e61ea5";
  private static final String USER6 = "8d57a927b98c4add82e61ea6";
  private static final String USER7 = "8d57a927b98c4add82e61ea7";
  private static final String USER8 = "8d57a927b98c4add82e61ea8";
  private static final String USER9 = "8d57a927b98c4add82e61ea9";

  private static final String COMMENT1 = "Hey! very nice pic ;-)";
  private static final String COMMENT2 = "A-W-E-S-O-M-E!";
  private static final String COMMENT3 = "where is that?? looks like a perfect spot!";
  private static final String COMMENT4 = "gr8 photo!!";
  private static final String COMMENT5 = "that's serious photography skills!!";
  private static final String COMMENT6 = "wow! when are you coming back btw?";
  private static final String COMMENT7 = "are you kidding me??!!";
  private static final String COMMENT8 = "Get Followers!!! Click in my profile!!";
  private static final String COMMENT9 = "What an amazing photo this is!";

  private Map<String, List<Comment>> mapPhotoComments = new HashMap<>();

  public FakeCommentDataSource() {
    mapPhotoComments.put(PHOTO1,
        new LinkedList<>(Arrays.asList(
            Comment.Builder().id(ID1).photoId(PHOTO1).userId(USER1).content(COMMENT1).build(),
            Comment.Builder().id(ID2).photoId(PHOTO1).userId(USER2).content(COMMENT2).build(),
            Comment.Builder().id(ID3).photoId(PHOTO1).userId(USER3).content(COMMENT3).build(),
            Comment.Builder().id(ID4).photoId(PHOTO1).userId(USER4).content(COMMENT4).build(),
            Comment.Builder().id(ID5).photoId(PHOTO1).userId(USER5).content(COMMENT5).build()
        )));
    mapPhotoComments.put(PHOTO2,
        new LinkedList<>(Arrays.asList(
            Comment.Builder().id(ID6).photoId(PHOTO2).userId(USER6).content(COMMENT6).build(),
            Comment.Builder().id(ID7).photoId(PHOTO2).userId(USER7).content(COMMENT7).build(),
            Comment.Builder().id(ID8).photoId(PHOTO2).userId(USER8).content(COMMENT8).build(),
            Comment.Builder().id(ID9).photoId(PHOTO2).userId(USER9).content(COMMENT9).build()
        )));
  }

  @Override public ObservableTask<List<Comment>> getComments(final String photoId) {
    if (mapPhotoComments.containsKey(photoId)) {
      return new ObservableTask<List<Comment>>() {
        @Override public void run(Subscriber<List<Comment>> result) {
          result.onSuccess(mapPhotoComments.get(photoId));
        }
      };
    }
    return null;
  }

  @Override
  public ObservableTask<Comment> publishComment(final UnpublishedComment unpublishedComment) {
    return new ObservableTask<Comment>() {
      @Override public void run(Subscriber<Comment> result) {
        final Comment comment = Comment.Builder()
            .id(String.valueOf(System.nanoTime()))
            .photoId(unpublishedComment.getPhotoId())
            .userId(unpublishedComment.getUserId())
            .content(unpublishedComment.getContent())
            .build();
        if (mapPhotoComments.containsKey(unpublishedComment.getPhotoId())) {
          List<Comment> photoComments = mapPhotoComments.get(comment.getPhotoId());
          if (!photoComments.contains(comment)) {
            photoComments.add(comment);
          }
        }
        result.onSuccess(comment);
      }
    };
  }
}

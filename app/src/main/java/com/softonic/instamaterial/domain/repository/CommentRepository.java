package com.softonic.instamaterial.domain.repository;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.model.Comment;
import com.softonic.instamaterial.domain.model.CommentPublication;
import java.util.List;

public interface CommentRepository {
  ObservableTask<List<Comment>> getComments(String photoId);

  ObservableTask<Comment> publishComment(CommentPublication commentPublication);

  ObservableTask<Comment> addCommentNotifier(String photoId);

  ObservableTask<Boolean> removeCommentNotifier(String photoId);
}

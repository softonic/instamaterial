package com.softonic.instamaterial.domain.repository;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.model.Comment;
import com.softonic.instamaterial.domain.model.UnpublishedComment;
import java.util.List;

public interface CommentRepository {
  ObservableTask<List<Comment>> getComments(String photoId);

  ObservableTask<Comment> publishComment(UnpublishedComment unpublishedComment);
}

package com.softonic.instamaterial.data.repository.comment;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.model.Comment;
import com.softonic.instamaterial.domain.model.UnpublishedComment;
import java.util.List;

public interface CommentDataSource {
  ObservableTask<List<Comment>> getComments(String photoId);

  ObservableTask<Comment> publishComment(UnpublishedComment unpublishedComment);
}

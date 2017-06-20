package com.softonic.instamaterial.ui.orchestrator;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.Subscriber;
import com.softonic.instamaterial.domain.common.UseCase;
import com.softonic.instamaterial.domain.executor.UseCaseExecutor;
import com.softonic.instamaterial.domain.interactors.GetUser;
import com.softonic.instamaterial.domain.model.Comment;
import com.softonic.instamaterial.domain.model.User;
import com.softonic.instamaterial.ui.model.CommentItem;

public class GetCommentItem extends UseCase<Comment, CommentItem> {
  private final GetUser getUser;

  public GetCommentItem(UseCaseExecutor useCaseExecutor, GetUser getUser) {
    super(useCaseExecutor);
    this.getUser = getUser;
  }

  @Override public ObservableTask<CommentItem> createObservableTask(final Comment comment) {
    return new ObservableTask<CommentItem>() {
      @Override public void run(final Subscriber<CommentItem> result) {
        getUser.createObservableTask(comment.getUserId()).run(new Subscriber<User>() {
          @Override public void onSuccess(User user) {
            result.onSuccess(
                CommentItem.Builder()
                    .commentId(comment.getId())
                    .content(comment.getContent())
                    .userNickname(user.getNickname())
                    .userAvatarUrl(user.getAvatarUrl())
                    .build());
          }

          @Override public void onError(Exception exception) {
            result.onError(exception);
          }
        });
      }
    };
  }
}

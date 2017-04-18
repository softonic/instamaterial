package com.softonic.instamaterial.ui.activity.comments;

import com.softonic.instamaterial.domain.common.UseCaseCallback;
import com.softonic.instamaterial.domain.interactors.GetAuthenticatedUserUid;
import com.softonic.instamaterial.domain.interactors.PublishComment;
import com.softonic.instamaterial.domain.model.Comment;
import com.softonic.instamaterial.domain.model.UnpublishedComment;
import com.softonic.instamaterial.ui.model.CommentItem;
import com.softonic.instamaterial.ui.orchestrator.GetCommentItem;
import com.softonic.instamaterial.ui.orchestrator.GetCommentItems;
import com.softonic.instamaterial.ui.presenter.Presenter;
import java.util.List;

public class CommentsPresenter extends Presenter<CommentsPresenter.View> {
  private final GetCommentItems getCommentItems;
  private final GetAuthenticatedUserUid getAuthenticatedUserUid;
  private final PublishComment publishComment;
  private final GetCommentItem getCommentItem;

  private String currentUserUid;

  public CommentsPresenter(GetCommentItems getCommentItems, GetAuthenticatedUserUid getAuthenticatedUserUid,
      PublishComment publishComment, GetCommentItem getCommentItem) {
    this.getCommentItems = getCommentItems;
    this.getAuthenticatedUserUid = getAuthenticatedUserUid;
    this.publishComment = publishComment;
    this.getCommentItem = getCommentItem;
  }

  @Override public void attach(View view) {
    super.attach(view);
    view.setupComments();
    getAuthenticatedUserUid.execute(new GetCurrentUserUidCallback());
  }

  public void requestComments(String photoId) {
    getCommentItems.execute(photoId, new GetCommentItemsCallback());
  }

  public void requestPublishComment(String photoId, String content) {
    UnpublishedComment unpublishedComment = UnpublishedComment.Builder()
        .userId(currentUserUid)
        .photoId(photoId)
        .content(content)
        .build();
    publishComment.execute(unpublishedComment, new PublishCommentCallback());
    view.showUploading();
  }

  public void requestCommentItem(Comment comment) {
    getCommentItem.execute(comment, new GetCommentItemCallback());
  }

  private class GetCommentItemsCallback implements UseCaseCallback<List<CommentItem>> {
    @Override public void onSuccess(List<CommentItem> commentItems) {
      view.onFinishedRequestingComments();
      if (commentItems.isEmpty()) {
        view.showEmptyComments();
      } else {
        view.updateComments(commentItems);
      }
    }

    @Override public void onError(Exception exception) {
      view.showErrorWhileRequestingComments();
    }
  }

  private class GetCurrentUserUidCallback implements UseCaseCallback<String> {

    @Override public void onSuccess(String result) {
      currentUserUid = result;
    }

    @Override public void onError(Exception exception) {
    }
  }

  private class PublishCommentCallback implements UseCaseCallback<Comment> {

    @Override public void onSuccess(Comment result) {
      getCommentItem.execute(result, new GetCommentItemCallback());
    }

    @Override public void onError(Exception exception) {
      view.showErrorWhileUploading();
    }
  }

  private class GetCommentItemCallback implements UseCaseCallback<CommentItem> {

    @Override public void onSuccess(CommentItem result) {
      view.onCommentAdded(result);
    }

    @Override public void onError(Exception exception) {
      view.showErrorWhileUploading();
    }
  }

  public interface View extends Presenter.View {
    void setupComments();

    void onFinishedRequestingComments();

    void updateComments(List<CommentItem> commentItems);

    void showEmptyComments();

    void showErrorWhileRequestingComments();

    void onCommentAdded(CommentItem commentItem);

    void showUploading();

    void showErrorWhileUploading();
  }
}

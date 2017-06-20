package com.softonic.instamaterial.ui.activity.comments;

import com.softonic.instamaterial.domain.common.EmptyUseCaseCallback;
import com.softonic.instamaterial.domain.common.UseCaseCallback;
import com.softonic.instamaterial.domain.interactors.AddCommentNotifier;
import com.softonic.instamaterial.domain.interactors.GetCurrentUserUid;
import com.softonic.instamaterial.domain.interactors.PublishComment;
import com.softonic.instamaterial.domain.interactors.RemoveCommentNotifier;
import com.softonic.instamaterial.domain.model.Comment;
import com.softonic.instamaterial.domain.model.CommentPublication;
import com.softonic.instamaterial.ui.model.CommentItem;
import com.softonic.instamaterial.ui.orchestrator.GetCommentItem;
import com.softonic.instamaterial.ui.orchestrator.GetCommentItems;
import com.softonic.instamaterial.ui.presenter.Presenter;
import java.util.List;

public class CommentsPresenter extends Presenter<CommentsPresenter.View> {
  private final GetCommentItems getCommentItems;
  private final GetCurrentUserUid getCurrentUserUid;
  private final PublishComment publishComment;
  private final GetCommentItem getCommentItem;
  private final AddCommentNotifier addCommentNotifier;
  private final RemoveCommentNotifier removeCommentNotifier;

  private String currentUserUid;

  public CommentsPresenter(GetCommentItems getCommentItems, GetCurrentUserUid getCurrentUserUid,
      PublishComment publishComment, GetCommentItem getCommentItem,
      AddCommentNotifier addCommentNotifier, RemoveCommentNotifier removeCommentNotifier) {
    this.getCommentItems = getCommentItems;
    this.getCurrentUserUid = getCurrentUserUid;
    this.publishComment = publishComment;
    this.getCommentItem = getCommentItem;
    this.addCommentNotifier = addCommentNotifier;
    this.removeCommentNotifier = removeCommentNotifier;
  }

  @Override public void attach(View view) {
    super.attach(view);
    view.setupComments();
    getCurrentUserUid.execute(new GetCurrentUserUidCallback());
  }

  public void requestComments(String photoId) {
    getCommentItems.execute(photoId, new GetCommentItemsCallback());
  }

  public void requestPublishComment(String photoId, String content) {
    CommentPublication commentPublication = CommentPublication.Builder()
        .userId(currentUserUid)
        .photoId(photoId)
        .content(content)
        .build();
    publishComment.execute(commentPublication, new PublishCommentCallback());
    view.showUploading();
  }

  public void requestCommentItem(Comment comment) {
    getCommentItem.execute(comment, new GetCommentItemCallback());
  }

  public void requestAddCommentNotifier(String photoId) {
    addCommentNotifier.execute(photoId, new AddCommentNotifierCallback());
  }

  public void requestRemoveCommentNotifier(String photoId) {
    removeCommentNotifier.execute(photoId, new EmptyUseCaseCallback<Boolean>());
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

  private class AddCommentNotifierCallback implements UseCaseCallback<Comment> {
    @Override public void onSuccess(Comment comment) {
      requestCommentItem(comment);
    }

    @Override public void onError(Exception exception) {
      view.showErrorWhileRequestingComments();
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

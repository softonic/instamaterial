package com.softonic.instamaterial.ui.activity.publish;

import com.softonic.instamaterial.domain.common.UseCaseCallback;
import com.softonic.instamaterial.domain.interactors.GetCurrentUserUid;
import com.softonic.instamaterial.domain.interactors.PublishPhoto;
import com.softonic.instamaterial.domain.model.Photo;
import com.softonic.instamaterial.domain.model.Publication;
import com.softonic.instamaterial.ui.presenter.Presenter;

public class PublishPresenter extends Presenter<PublishPresenter.View> {
  private final GetCurrentUserUid getCurrentUserUid;
  private final PublishPhoto publishPhoto;

  private String currentUserUid;

  public PublishPresenter(GetCurrentUserUid getCurrentUserUid, PublishPhoto publishPhoto) {
    this.getCurrentUserUid = getCurrentUserUid;
    this.publishPhoto = publishPhoto;
  }

  @Override public void attach(View view) {
    super.attach(view);
    getCurrentUserUid.execute(new GetCurrentUserUidCallback());
  }

  public void onRequestPublish(String photoUri, String photoDescription) {
    Publication publication = Publication.Builder()
        .userId(currentUserUid)
        .photoUri(photoUri)
        .description(photoDescription)
        .build();
    publishPhoto.execute(publication, new PublishPhotoCallback());
    view.showUploading();
  }

  private class GetCurrentUserUidCallback implements UseCaseCallback<String> {

    @Override public void onSuccess(String result) {
      currentUserUid = result;
    }

    @Override public void onError(Exception exception) {
    }
  }

  private class PublishPhotoCallback implements UseCaseCallback<Photo> {

    @Override public void onSuccess(Photo result) {
      view.onPhotoPublished(result.getId());
    }

    @Override public void onError(Exception exception) {
      view.showErrorWhileUploading();
    }
  }

  public interface View extends Presenter.View {
    void showUploading();

    void showErrorWhileUploading();

    void onPhotoPublished(String photoId);
  }
}

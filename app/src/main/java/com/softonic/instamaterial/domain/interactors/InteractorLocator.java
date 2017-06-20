package com.softonic.instamaterial.domain.interactors;

public interface InteractorLocator {
  GetPhoto getPhoto();

  GetPhotos getPhotos();

  GetUser getUser();

  UpdateUser updateUser();

  GetPhotoLikes getPhotoLikes();

  GetPhotoComments getPhotoComments();

  LikePhoto likePhoto();

  GetCurrentUserUid getLoggedUser();

  PublishPhoto publishPhoto();

  UploadPhoto uploadPhoto();

  PublishComment publishComment();

  AddPhotoNotifier notifyPhotoAdded();

  RemovePhotoNotifier removePhotoNotifier();

  AddCommentNotifier addCommentNotifier();

  RemoveCommentNotifier removeCommentNotifier();

  AddLikeNotifier addLikeNotifier();

  RemoveLikeNotifier removeLikeNotifier();
}

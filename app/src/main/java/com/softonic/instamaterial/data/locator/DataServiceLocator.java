package com.softonic.instamaterial.data.locator;

import com.softonic.instamaterial.data.repository.comment.CommentDataRepository;
import com.softonic.instamaterial.data.repository.comment.CommentDataSource;
import com.softonic.instamaterial.data.repository.comment.FirebaseCommentDataSource;
import com.softonic.instamaterial.data.repository.like.FirebaseLikeDataSource;
import com.softonic.instamaterial.data.repository.like.LikeDataRepository;
import com.softonic.instamaterial.data.repository.like.LikeDataSource;
import com.softonic.instamaterial.data.repository.loggedUser.FirebaseLoggedUserDataSource;
import com.softonic.instamaterial.data.repository.loggedUser.LoggedUserDataRepository;
import com.softonic.instamaterial.data.repository.loggedUser.LoggedUserDataSource;
import com.softonic.instamaterial.data.repository.photo.FirebasePhotoDataSource;
import com.softonic.instamaterial.data.repository.photo.PhotoDataRepository;
import com.softonic.instamaterial.data.repository.photo.PhotoDataSource;
import com.softonic.instamaterial.data.repository.user.FirebaseUserDataSource;
import com.softonic.instamaterial.data.repository.user.UserDataRepository;
import com.softonic.instamaterial.data.repository.user.UserDataSource;
import com.softonic.instamaterial.domain.repository.CommentRepository;
import com.softonic.instamaterial.domain.repository.LikeRepository;
import com.softonic.instamaterial.domain.repository.LoggedUserRepository;
import com.softonic.instamaterial.domain.repository.PhotoRepository;
import com.softonic.instamaterial.domain.repository.RepositoryLocator;
import com.softonic.instamaterial.domain.repository.UserRepository;

public class DataServiceLocator implements RepositoryLocator {
  private PhotoRepository photoRepository;
  private PhotoDataSource photoDataSource;

  private UserRepository userRepository;
  private UserDataSource userDataSource;

  private LikeRepository likeRepository;
  private LikeDataSource likeDataSource;

  private CommentRepository commentRepository;
  private CommentDataSource commentDataSource;

  private LoggedUserRepository loggedUserRepository;
  private LoggedUserDataSource loggedUserDataSource;

  public static DataServiceLocator getInstance() {
    return Instance.instance;
  }

  @Override public PhotoRepository photoRepository() {
    if (photoRepository == null) {
      photoRepository = new PhotoDataRepository(photoDataSource());
    }
    return photoRepository;
  }

  private PhotoDataSource photoDataSource() {
    if (photoDataSource == null) {
      photoDataSource = new FirebasePhotoDataSource();
    }
    return photoDataSource;
  }

  @Override public UserRepository userRepository() {
    if (userRepository == null) {
      userRepository = new UserDataRepository(userDataSource());
    }
    return userRepository;
  }

  private UserDataSource userDataSource() {
    if (userDataSource == null) {
      userDataSource = new FirebaseUserDataSource();
    }
    return userDataSource;
  }

  @Override public LikeRepository likeRepository() {
    if (likeRepository == null) {
      likeRepository = new LikeDataRepository(likeDataSource());
    }
    return likeRepository;
  }

  private LikeDataSource likeDataSource() {
    if (likeDataSource == null) {
      likeDataSource = new FirebaseLikeDataSource();
    }
    return likeDataSource;
  }

  @Override public CommentRepository commentRepository() {
    if (commentRepository == null) {
      commentRepository = new CommentDataRepository(commentDataSource());
    }
    return commentRepository;
  }

  private CommentDataSource commentDataSource() {
    if (commentDataSource == null) {
      commentDataSource = new FirebaseCommentDataSource();
    }
    return commentDataSource;
  }

  @Override public LoggedUserRepository loggedUserRepository() {
    if (loggedUserRepository == null) {
      loggedUserRepository = new LoggedUserDataRepository(loggedUserDataSource());
    }
    return loggedUserRepository;
  }

  private LoggedUserDataSource loggedUserDataSource() {
    if (loggedUserDataSource == null) {
      loggedUserDataSource = new FirebaseLoggedUserDataSource();
    }
    return loggedUserDataSource;
  }

  private static class Instance {
    private static final DataServiceLocator instance = new DataServiceLocator();
  }
}

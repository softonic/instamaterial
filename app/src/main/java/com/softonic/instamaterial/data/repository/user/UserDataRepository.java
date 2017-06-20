package com.softonic.instamaterial.data.repository.user;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.model.User;
import com.softonic.instamaterial.domain.repository.UserRepository;

public class UserDataRepository implements UserRepository {
  private final UserDataSource userDataSource;

  public UserDataRepository(UserDataSource userDataSource) {
    this.userDataSource = userDataSource;
  }

  @Override public ObservableTask<User> get(String userId) {
    return userDataSource.get(userId);
  }

  @Override public ObservableTask<Boolean> put(User user) {
    return userDataSource.put(user);
  }
}

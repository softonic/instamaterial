package com.softonic.instamaterial.data.repository.loggedUser;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.repository.AuthenticatedUserRepository;

public class AuthenticatedUserDataRepository implements AuthenticatedUserRepository {
  private final LoggedUserDataSource loggedUserDataSource;

  public AuthenticatedUserDataRepository(LoggedUserDataSource loggedUserDataSource) {
    this.loggedUserDataSource = loggedUserDataSource;
  }

  @Override public ObservableTask<String> getUserUid() {
    return loggedUserDataSource.get();
  }
}

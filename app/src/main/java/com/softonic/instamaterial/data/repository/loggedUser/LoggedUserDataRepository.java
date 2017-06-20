package com.softonic.instamaterial.data.repository.loggedUser;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.repository.LoggedUserRepository;

public class LoggedUserDataRepository implements LoggedUserRepository {
  private final LoggedUserDataSource loggedUserDataSource;

  public LoggedUserDataRepository(LoggedUserDataSource loggedUserDataSource) {
    this.loggedUserDataSource = loggedUserDataSource;
  }

  @Override public ObservableTask<String> get() {
    return loggedUserDataSource.get();
  }
}

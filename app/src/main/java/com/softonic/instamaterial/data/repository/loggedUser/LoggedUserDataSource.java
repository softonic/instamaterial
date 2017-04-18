package com.softonic.instamaterial.data.repository.loggedUser;

import com.softonic.instamaterial.domain.common.ObservableTask;

public interface LoggedUserDataSource {
  ObservableTask<String> get();
}

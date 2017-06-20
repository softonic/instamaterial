package com.softonic.instamaterial.domain.repository;

import com.softonic.instamaterial.domain.common.ObservableTask;

public interface LoggedUserRepository {
  ObservableTask<String> get();
}

package com.softonic.instamaterial.domain.repository;

import com.softonic.instamaterial.domain.common.ObservableTask;

public interface AuthenticatedUserRepository {
  ObservableTask<String> getUserUid();
}

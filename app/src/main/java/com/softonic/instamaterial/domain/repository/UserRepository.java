package com.softonic.instamaterial.domain.repository;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.model.User;

public interface UserRepository {
  ObservableTask<User> get(String userId);

  ObservableTask<Boolean> put(User user);
}

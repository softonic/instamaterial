package com.softonic.instamaterial.domain.repository;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.model.User;

public interface UserRepository {
  ObservableTask<User> getUser(String userId);

  ObservableTask<Boolean> putUser(User user);
}

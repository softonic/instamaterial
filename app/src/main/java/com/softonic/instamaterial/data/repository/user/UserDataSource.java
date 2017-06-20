package com.softonic.instamaterial.data.repository.user;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.model.User;

public interface UserDataSource {
  ObservableTask<User> get(String userId);

  ObservableTask<Boolean> put(User user);
}

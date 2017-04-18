package com.softonic.instamaterial.domain.interactors;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.UseCase;
import com.softonic.instamaterial.domain.executor.UseCaseExecutor;
import com.softonic.instamaterial.domain.model.User;
import com.softonic.instamaterial.domain.repository.UserRepository;

public class UpdateUser extends UseCase<User, Boolean> {
  private final UserRepository userRepository;

  public UpdateUser(UseCaseExecutor useCaseExecutor, UserRepository userRepository) {
    super(useCaseExecutor);
    this.userRepository = userRepository;
  }

  @Override public ObservableTask<Boolean> createObservableTask(User user) {
    return userRepository.putUser(user);
  }
}

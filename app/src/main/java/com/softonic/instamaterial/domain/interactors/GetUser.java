package com.softonic.instamaterial.domain.interactors;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.UseCase;
import com.softonic.instamaterial.domain.executor.UseCaseExecutor;
import com.softonic.instamaterial.domain.model.User;
import com.softonic.instamaterial.domain.repository.UserRepository;

public class GetUser extends UseCase<String, User> {
  private final UserRepository userRepository;

  public GetUser(UseCaseExecutor useCaseExecutor, UserRepository userRepository) {
    super(useCaseExecutor);
    this.userRepository = userRepository;
  }

  @Override public ObservableTask<User> createObservableTask(final String userId) {
    return userRepository.getUser(userId);
  }
}

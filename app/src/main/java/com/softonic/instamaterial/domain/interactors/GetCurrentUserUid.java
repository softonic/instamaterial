package com.softonic.instamaterial.domain.interactors;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.UseCase;
import com.softonic.instamaterial.domain.executor.UseCaseExecutor;
import com.softonic.instamaterial.domain.repository.LoggedUserRepository;

public class GetCurrentUserUid extends UseCase<Void, String> {
  private final LoggedUserRepository loggedUserRepository;

  public GetCurrentUserUid(UseCaseExecutor useCaseExecutor,
      LoggedUserRepository loggedUserRepository) {
    super(useCaseExecutor);
    this.loggedUserRepository = loggedUserRepository;
  }

  @Override public ObservableTask<String> createObservableTask(Void input) {
    return loggedUserRepository.get();
  }
}
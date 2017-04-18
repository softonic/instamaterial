package com.softonic.instamaterial.domain.interactors;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.UseCase;
import com.softonic.instamaterial.domain.executor.UseCaseExecutor;
import com.softonic.instamaterial.domain.repository.AuthenticatedUserRepository;

public class GetAuthenticatedUserUid extends UseCase<Void, String> {
  private final AuthenticatedUserRepository authenticatedUserRepository;

  public GetAuthenticatedUserUid(UseCaseExecutor useCaseExecutor,
      AuthenticatedUserRepository authenticatedUserRepository) {
    super(useCaseExecutor);
    this.authenticatedUserRepository = authenticatedUserRepository;
  }

  @Override public ObservableTask<String> createObservableTask(Void input) {
    return authenticatedUserRepository.getUserUid();
  }
}
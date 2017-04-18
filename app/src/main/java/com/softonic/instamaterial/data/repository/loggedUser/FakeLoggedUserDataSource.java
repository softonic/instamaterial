package com.softonic.instamaterial.data.repository.loggedUser;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.Subscriber;

public class FakeLoggedUserDataSource implements LoggedUserDataSource {
  private final String FAKE_USER_ID = "8d57a927b98c4add82e61ea1";

  @Override public ObservableTask<String> get() {
    return new ObservableTask<String>() {
      @Override public void run(Subscriber<String> result) {
        result.onSuccess(FAKE_USER_ID);
      }
    };
  }
}

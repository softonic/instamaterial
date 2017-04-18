package com.softonic.instamaterial.data.repository.user;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.Subscriber;
import com.softonic.instamaterial.domain.model.User;
import java.util.Random;

public class FakeUserDataSource implements UserDataSource {
  private static final String FAKE_NICKNAME_1 = "Jeanette Allen";
  private static final String FAKE_NICKNAME_2 = "Alex Willis";
  private static final String FAKE_NICKNAME_3 = "Beth Berry";
  private static final String FAKE_AVATAR_URL_1 = "https://randomuser.me/api/portraits/women/2.jpg";
  private static final String FAKE_AVATAR_URL_2 = "https://randomuser.me/api/portraits/men/37.jpg";
  private static final String FAKE_AVATAR_URL_3 = "https://randomuser.me/api/portraits/women/68.jpg";

  @Override public ObservableTask<User> get(final String userId) {
    return new ObservableTask<User>() {
      @Override public void run(Subscriber<User> result) {
        int random = new Random(userId.hashCode()).nextInt(3);
        switch (random) {
          case 0:
            result.onSuccess(User.Builder()
                .id(userId)
                .username(FAKE_NICKNAME_1)
                .photoUrl(FAKE_AVATAR_URL_1)
                .build());
            break;
          case 1:
            result.onSuccess(User.Builder()
                .id(userId)
                .username(FAKE_NICKNAME_2)
                .photoUrl(FAKE_AVATAR_URL_2)
                .build());
            break;
          case 2:
          default:
            result.onSuccess(User.Builder()
                .id(userId)
                .username(FAKE_NICKNAME_3)
                .photoUrl(FAKE_AVATAR_URL_3)
                .build());
            break;
        }
      }
    };
  }

  @Override public ObservableTask<Boolean> put(User user) {
    return new ObservableTask<Boolean>() {
      @Override public void run(Subscriber<Boolean> result) {
        result.onSuccess(true);
      }
    };
  }
}

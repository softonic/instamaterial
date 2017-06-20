package com.softonic.instamaterial.data.repository.user;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.Subscriber;
import com.softonic.instamaterial.domain.model.User;
import java.util.Random;

public class FakeUserDataSource implements UserDataSource {
  private static final String FAKE_NICKNAME_1 = "jorgedlsg";
  private static final String FAKE_NICKNAME_2 = "mmartos";
  private static final String FAKE_NICKNAME_3 = "nachovalen";
  private static final String FAKE_AVATAR_URL_1 =
      "https://scontent.cdninstagram.com/t51.2885-19/s150x150/11410528_504112499751185_1432909126_a.jpg";
  private static final String FAKE_AVATAR_URL_2 =
      "http://scontent.cdninstagram.com/t51.2885-19/11410685_504779919672166_1562473804_a.jpg";
  private static final String FAKE_AVATAR_URL_3 =
      "https://pbs.twimg.com/profile_images/781114860763484160/fp6-Qf2Y_400x400.jpg";

  @Override public ObservableTask<User> get(final String userId) {
    return new ObservableTask<User>() {
      @Override public void run(Subscriber<User> result) {
        int random = new Random(userId.hashCode()).nextInt(3);
        switch (random) {
          case 0:
            result.onSuccess(User.Builder()
                .id(userId)
                .nickname(FAKE_NICKNAME_1)
                .avatarUrl(FAKE_AVATAR_URL_1)
                .build());
            break;
          case 1:
            result.onSuccess(User.Builder()
                .id(userId)
                .nickname(FAKE_NICKNAME_2)
                .avatarUrl(FAKE_AVATAR_URL_2)
                .build());
            break;
          case 2:
          default:
            result.onSuccess(User.Builder()
                .id(userId)
                .nickname(FAKE_NICKNAME_3)
                .avatarUrl(FAKE_AVATAR_URL_3)
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

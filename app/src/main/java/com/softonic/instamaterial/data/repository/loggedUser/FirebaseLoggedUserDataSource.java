package com.softonic.instamaterial.data.repository.loggedUser;

import android.support.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.Subscriber;

public class FirebaseLoggedUserDataSource implements LoggedUserDataSource {
  @Override public ObservableTask<String> get() {
    return new ObservableTask<String>() {
      @Override public void run(final Subscriber<String> result) {
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
          @Override public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
              result.onSuccess(user.getUid());
            } else {
              result.onError(null);
            }
            firebaseAuth.removeAuthStateListener(this);
          }
        };
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
      }
    };
  }
}

package com.softonic.instamaterial.ui.orchestrator;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.softonic.instamaterial.R;
import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.Subscriber;
import com.softonic.instamaterial.domain.common.UseCase;
import com.softonic.instamaterial.domain.executor.UseCaseExecutor;

public class SignOut extends UseCase<Void, Boolean> implements
    GoogleApiClient.OnConnectionFailedListener {
  private final GoogleApiClient googleApiClient;

  public SignOut(UseCaseExecutor useCaseExecutor, FragmentActivity activity) {
    super(useCaseExecutor);

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(activity.getString(R.string.default_web_client_id))
        .requestEmail()
        .build();

    googleApiClient = new GoogleApiClient.Builder(activity)
        .enableAutoManage(activity, this)
        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        .build();
  }

  @Override public ObservableTask<Boolean> createObservableTask(Void input) {
    return new ObservableTask<Boolean>() {
      @Override public void run(final Subscriber<Boolean> result) {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
            new ResultCallback<Status>() {
              @Override
              public void onResult(Status status) {
                if (status.isSuccess()) {
                  FirebaseAuth.getInstance().signOut();
                  result.onSuccess(true);
                  googleApiClient.disconnect();
                } else {
                  result.onSuccess(false);
                }
              }
            });
      }
    };
  }

  @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

  }
}

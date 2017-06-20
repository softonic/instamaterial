package com.softonic.instamaterial.ui.orchestrator;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.softonic.instamaterial.R;
import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.Subscriber;
import com.softonic.instamaterial.domain.common.UseCase;
import com.softonic.instamaterial.domain.executor.UseCaseExecutor;
import com.softonic.instamaterial.domain.interactors.UpdateUser;
import com.softonic.instamaterial.domain.model.User;

public class SignIn extends UseCase<Integer, Boolean> implements
    GoogleApiClient.OnConnectionFailedListener {

  private final UpdateUser updateUser;
  private final FragmentActivity activity;
  private final GoogleApiClient googleApiClient;

  private Subscriber<Boolean> subscriber;

  public SignIn(UseCaseExecutor useCaseExecutor, UpdateUser updateUser, FragmentActivity activity) {
    super(useCaseExecutor);
    this.updateUser = updateUser;

    this.activity = activity;

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(activity.getString(R.string.default_web_client_id))
        .requestEmail()
        .build();

    googleApiClient = new GoogleApiClient.Builder(activity)
        .enableAutoManage(activity, this)
        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        .build();
  }

  @Override public ObservableTask<Boolean> createObservableTask(final Integer requestCode) {
    return new ObservableTask<Boolean>() {
      @Override public void run(Subscriber<Boolean> result) {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        subscriber = result;
        activity.startActivityForResult(intent, requestCode);
      }
    };
  }

  public void handleSignInResult(GoogleSignInResult result) {
    if (subscriber != null) {
      if (result.isSuccess()) {
        GoogleSignInAccount account = result.getSignInAccount();
        firebaseAuthWithGoogle(account);
      } else {
        subscriber.onSuccess(false);
      }
    }
  }

  @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
  }

  private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
    final FirebaseAuth auth = FirebaseAuth.getInstance();
    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
    auth.signInWithCredential(credential)
        .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
              User user = User.Builder()
                  .id(auth.getCurrentUser().getUid())
                  .nickname(account.getDisplayName())
                  .avatarUrl(account.getPhotoUrl().toString())
                  .build();
              updateUser.createObservableTask(user).run(subscriber);
            } else {
              subscriber.onSuccess(false);
            }
          }
        });
  }
}

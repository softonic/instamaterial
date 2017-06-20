package com.softonic.instamaterial.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.OnClick;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.softonic.instamaterial.R;
import com.softonic.instamaterial.ui.activity.BaseActivity;
import com.softonic.instamaterial.ui.locator.AppServiceLocator;

public class LoginActivity extends BaseActivity implements LoginPresenter.View {
  private static final int RC_SIGN_IN = 9001;

  private LoginPresenter loginPresenter;

  @BindView(R.id.contentRoot) LinearLayout llContentRoot;
  @BindView(R.id.flLoading) FrameLayout flLoading;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    initPresenter();
  }

  private void initPresenter() {
    LoginPresenterLocator presenterLocator =
        AppServiceLocator.getInstance().plusActivityServiceLocator();
    loginPresenter = presenterLocator.loginPresenter(this);
    loginPresenter.attach(this);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == RC_SIGN_IN) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      loginPresenter.handleSignInResult(result);
    }
  }

  @OnClick(R.id.btnSignIn)
  public void onSignInClick() {
    loginPresenter.requestLogin(RC_SIGN_IN);
  }

  @Override public void showLoading(boolean show) {
    flLoading.setVisibility(show ? View.VISIBLE : View.GONE);
  }

  @Override public void closeLoginRequest() {
    setResult(RESULT_OK);
    finish();
  }

  @Override public void showErrorWhileLoggingIn(String errorMessage) {
    Snackbar.make(llContentRoot, errorMessage, Snackbar.LENGTH_INDEFINITE)
        .setAction(R.string.action_retry, new View.OnClickListener() {
          @Override public void onClick(View v) {
            loginPresenter.requestLogin(RC_SIGN_IN);
          }
        })
        .show();
  }
}

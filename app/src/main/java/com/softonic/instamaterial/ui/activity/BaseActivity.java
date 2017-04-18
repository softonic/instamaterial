package com.softonic.instamaterial.ui.activity;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.softonic.instamaterial.R;

public class BaseActivity extends AppCompatActivity {

  @Nullable
  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @Nullable
  @BindView(R.id.ivLogo)
  ImageView ivLogo;

  @Override
  public void setContentView(int layoutResID) {
    super.setContentView(layoutResID);
    bindViews();
  }

  protected void bindViews() {
    ButterKnife.bind(this);
    setupToolbar();
  }

  protected void setupToolbar() {
    if (toolbar != null) {
      toolbar.setTitle("");
      setSupportActionBar(toolbar);
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null) {
        actionBar.setDisplayShowTitleEnabled(false);
      }
    }
  }

  @Nullable
  public Toolbar getToolbar() {
    return toolbar;
  }

  @Nullable
  public ImageView getIvLogo() {
    return ivLogo;
  }

  protected void hideKeyboard() {
    View view = getCurrentFocus();
    if (view != null) {
      InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }
}

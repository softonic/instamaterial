package com.softonic.instamaterial.ui.activity.publish;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import butterknife.BindView;
import com.softonic.instamaterial.R;
import com.softonic.instamaterial.ui.activity.BaseActivity;
import com.softonic.instamaterial.ui.activity.main.MainActivity;
import com.softonic.instamaterial.ui.locator.AppServiceLocator;
import com.softonic.instamaterial.ui.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class PublishActivity extends BaseActivity implements PublishPresenter.View {
  private static final String ARG_TAKEN_PHOTO_URI = "arg_taken_photo_uri";

  @BindView(R.id.ivPhoto)
  ImageView ivPhoto;
  @BindView(R.id.etDescription)
  EditText etDescription;
  @BindView(R.id.flUploading)
  FrameLayout flUploading;

  private Uri photoUri;
  private int photoSize;

  private PublishPresenter publishPresenter;

  private boolean uploading;

  public static void openWithPhotoUri(Activity openingActivity, Uri photoUri) {
    Intent intent = new Intent(openingActivity, PublishActivity.class);
    intent.putExtra(ARG_TAKEN_PHOTO_URI, photoUri);
    openingActivity.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_publish);
    initPresenter();

    Toolbar toolbar = getToolbar();
    if (toolbar != null) {
      toolbar.setNavigationIcon(R.drawable.ic_arrow_back_grey600_24dp);
    }
    photoSize = getResources().getDimensionPixelSize(R.dimen.publish_photo_thumbnail_size);

    if (savedInstanceState == null) {
      photoUri = getIntent().getParcelableExtra(ARG_TAKEN_PHOTO_URI);
    } else {
      photoUri = savedInstanceState.getParcelable(ARG_TAKEN_PHOTO_URI);
    }
    updateStatusBarColor();

    ivPhoto.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
      @Override
      public boolean onPreDraw() {
        ivPhoto.getViewTreeObserver().removeOnPreDrawListener(this);
        loadThumbnailPhoto();
        return true;
      }
    });
  }

  private void initPresenter() {
    PublishPresenterLocator presenterLocator =
        AppServiceLocator.getInstance().plusActivityServiceLocator();
    publishPresenter = presenterLocator.publishPresenter();
    publishPresenter.attach(this);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void updateStatusBarColor() {
    if (Utils.isAndroid5()) {
      getWindow().setStatusBarColor(0xff888888);
    }
  }

  private void loadThumbnailPhoto() {
    ivPhoto.setScaleX(0);
    ivPhoto.setScaleY(0);
    Picasso.with(this)
        .load(photoUri)
        .centerCrop()
        .resize(photoSize, photoSize)
        .into(ivPhoto, new Callback() {
          @Override
          public void onSuccess() {
            ivPhoto.animate()
                .scaleX(1.f).scaleY(1.f)
                .setInterpolator(new OvershootInterpolator())
                .setDuration(400)
                .setStartDelay(200)
                .start();
          }

          @Override
          public void onError() {
          }
        });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_publish, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_publish) {
      hideKeyboard();
      publishPresenter.onRequestPublish(photoUri.toString(), etDescription.getText().toString());
      return true;
    } else if (item.getItemId() == android.R.id.home) {
      finish();
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  private void bringMainActivityToTop(String photoId) {
    startActivity(MainActivity.getCallingIntent(this, photoId));
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelable(ARG_TAKEN_PHOTO_URI, photoUri);
  }

  @Override public void showUploading() {
    flUploading.setVisibility(View.VISIBLE);
    uploading = true;
  }

  @Override public void showErrorWhileUploading() {
    uploading = false;
    flUploading.setVisibility(View.GONE);
    Snackbar
        .make(findViewById(R.id.root), R.string.photos_cannot_publish_photo,
            Snackbar.LENGTH_INDEFINITE)
        .setAction(R.string.action_retry, new View.OnClickListener() {
          @Override public void onClick(View v) {
            publishPresenter.onRequestPublish(photoUri.toString(),
                etDescription.getText().toString());
          }
        })
        .show();
  }

  @Override public void onPhotoPublished(String photoId) {
    uploading = false;
    flUploading.setVisibility(View.GONE);
    bringMainActivityToTop(photoId);
  }

  @Override public void onBackPressed() {
    if (!uploading) {
      super.onBackPressed();
    }
  }
}

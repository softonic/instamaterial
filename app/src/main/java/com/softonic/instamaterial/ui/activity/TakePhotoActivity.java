package com.softonic.instamaterial.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.OnClick;
import com.commonsware.cwac.camera.CameraHost;
import com.commonsware.cwac.camera.CameraHostProvider;
import com.commonsware.cwac.camera.CameraView;
import com.commonsware.cwac.camera.PictureTransaction;
import com.commonsware.cwac.camera.SimpleCameraHost;
import com.softonic.instamaterial.R;
import com.softonic.instamaterial.ui.activity.publish.PublishActivity;
import com.softonic.instamaterial.ui.utils.Utils;
import com.softonic.instamaterial.ui.view.RevealBackgroundView;
import java.io.File;

public class TakePhotoActivity extends BaseActivity
    implements RevealBackgroundView.OnStateChangeListener,
    CameraHostProvider {
  private static final String ARG_REVEAL_START_LOCATION = "reveal_start_location";

  private static final Interpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
  private static final Interpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();

  @BindView(R.id.vRevealBackground)
  RevealBackgroundView vRevealBackground;
  @BindView(R.id.vPhotoRoot)
  View vTakePhotoRoot;
  @BindView(R.id.vShutter)
  View vShutter;
  @BindView(R.id.ivTakenPhoto)
  ImageView ivTakenPhoto;
  @BindView(R.id.vUpperPanel)
  View vUpperPanel;
  @BindView(R.id.vLowerPanel)
  View vLowerPanel;
  @BindView(R.id.cameraView)
  CameraView cameraView;
  @BindView(R.id.btnTakePhoto)
  Button btnTakePhoto;

  public static void startCameraFromLocation(int[] startingLocation, Activity startingActivity) {
    Intent intent = new Intent(startingActivity, TakePhotoActivity.class);
    intent.putExtra(ARG_REVEAL_START_LOCATION, startingLocation);
    startingActivity.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_take_photo);
    updateStatusBarColor();
    setupRevealBackground(savedInstanceState);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void updateStatusBarColor() {
    if (Utils.isAndroid5()) {
      getWindow().setStatusBarColor(0xff111111);
    }
  }

  private void setupRevealBackground(Bundle savedInstanceState) {
    vRevealBackground.setFillPaintColor(0xFF16181a);
    vRevealBackground.setOnStateChangeListener(this);
    if (savedInstanceState == null) {
      final int[] startingLocation = getIntent().getIntArrayExtra(ARG_REVEAL_START_LOCATION);
      vRevealBackground.getViewTreeObserver()
          .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
              vRevealBackground.getViewTreeObserver().removeOnPreDrawListener(this);
              vRevealBackground.startFromLocation(startingLocation);
              return true;
            }
          });
    } else {
      vRevealBackground.setToFinishedFrame();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    cameraView.onResume();
    btnTakePhoto.setEnabled(true);
  }

  @Override
  protected void onPause() {
    super.onPause();
    cameraView.onPause();
  }

  @OnClick(R.id.btnTakePhoto)
  public void onTakePhotoClick() {
    btnTakePhoto.setEnabled(false);
    cameraView.takePicture(true, true);
    animateShutter();
  }

  @OnClick(R.id.btnCancel)
  public void onCancelClick() {
    finish();
  }

  private void animateShutter() {
    vShutter.setVisibility(View.VISIBLE);
    vShutter.setAlpha(0.f);

    ObjectAnimator alphaInAnim = ObjectAnimator.ofFloat(vShutter, "alpha", 0f, 0.8f);
    alphaInAnim.setDuration(100);
    alphaInAnim.setStartDelay(100);
    alphaInAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

    ObjectAnimator alphaOutAnim = ObjectAnimator.ofFloat(vShutter, "alpha", 0.8f, 0f);
    alphaOutAnim.setDuration(200);
    alphaOutAnim.setInterpolator(DECELERATE_INTERPOLATOR);

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playSequentially(alphaInAnim, alphaOutAnim);
    animatorSet.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        vShutter.setVisibility(View.GONE);
      }
    });
    animatorSet.start();
  }

  @Override
  public void onStateChange(int state) {
    if (RevealBackgroundView.STATE_FINISHED == state) {
      vTakePhotoRoot.setVisibility(View.VISIBLE);
      startIntroAnimation();
    } else {
      vTakePhotoRoot.setVisibility(View.INVISIBLE);
    }
  }

  private void startIntroAnimation() {
    vUpperPanel.animate().translationY(0).setDuration(400).setInterpolator(DECELERATE_INTERPOLATOR);
    vLowerPanel.animate()
        .translationY(0)
        .setDuration(400)
        .setInterpolator(DECELERATE_INTERPOLATOR)
        .start();
  }

  @Override
  public CameraHost getCameraHost() {
    return new MyCameraHost(this);
  }

  class MyCameraHost extends SimpleCameraHost {

    private Camera.Size previewSize;

    public MyCameraHost(Context context) {
      super(context);
    }

    @Override
    public boolean useFullBleedPreview() {
      return true;
    }

    @Override
    public Camera.Size getPictureSize(PictureTransaction xact, Camera.Parameters parameters) {
      return previewSize;
    }

    @Override
    public Camera.Parameters adjustPreviewParameters(Camera.Parameters parameters) {
      Camera.Parameters parameters1 = super.adjustPreviewParameters(parameters);
      previewSize = parameters1.getPreviewSize();
      return parameters1;
    }

    @Override
    public void saveImage(PictureTransaction xact, final Bitmap bitmap) {
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          showTakenPicture(bitmap);
        }
      });
    }

    @Override
    public void saveImage(PictureTransaction xact, byte[] image) {
      super.saveImage(xact, image);
      publishTakenPicture(getPhotoPath());
    }

    @Override protected String getPhotoFilename() {
      return super.getPhotoFilename();
    }
  }

  private void showTakenPicture(Bitmap bitmap) {
    ivTakenPhoto.setImageBitmap(bitmap);
  }

  private void publishTakenPicture(File photoPath) {
    PublishActivity.openWithPhotoUri(this, Uri.fromFile(photoPath));
  }
}

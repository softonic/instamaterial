package com.softonic.instamaterial.ui.activity.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.OnClick;
import com.softonic.instamaterial.R;
import com.softonic.instamaterial.ui.activity.BaseActivity;
import com.softonic.instamaterial.ui.activity.TakePhotoActivity;
import com.softonic.instamaterial.ui.activity.comments.CommentsActivity;
import com.softonic.instamaterial.ui.adapter.FeedAdapter;
import com.softonic.instamaterial.ui.adapter.FeedItemAnimator;
import com.softonic.instamaterial.ui.locator.AppServiceLocator;
import com.softonic.instamaterial.ui.model.FeedItem;
import com.softonic.instamaterial.ui.utils.Utils;
import java.util.List;

public class MainActivity extends BaseActivity
    implements FeedAdapter.OnFeedItemClickListener, MainPresenter.View {
  public static final String ACTION_SHOW_LOADING_ITEM = "action_show_loading_item";

  private static final String EXTRA_PHOTO_ID = "photo_id";

  private static final int ANIM_DURATION_TOOLBAR = 300;
  private static final int ANIM_DURATION_FAB = 400;

  @BindView(R.id.clContent)
  CoordinatorLayout clContent;
  @BindView(R.id.rvFeed)
  RecyclerView rvFeed;
  @BindView(R.id.btnCreate)
  FloatingActionButton fabCreate;
  @BindView(R.id.pbLoading)
  ProgressBar pbLoading;
  @BindView(R.id.llNoElements)
  LinearLayout llNoElements;

  private FeedAdapter feedAdapter;

  private boolean pendingIntroAnimation;

  private MainPresenter mainPresenter;

  public static Intent getCallingIntent(Context context, String photoId) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    intent.setAction(MainActivity.ACTION_SHOW_LOADING_ITEM);
    intent.putExtra(EXTRA_PHOTO_ID, photoId);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initPresenter();

    getWindow().setBackgroundDrawable(null);

    if (savedInstanceState == null) {
      pendingIntroAnimation = true;
      mainPresenter.requestLoggedUser();
    }
  }

  private void initPresenter() {
    MainPresenterLocator presenterLocator =
        AppServiceLocator.getInstance().plusActivityServiceLocator();
    mainPresenter = presenterLocator.mainPresenter(this);
    mainPresenter.attach(this);
  }

  @Override
  public void setupFeed(String userId) {
    feedAdapter = new FeedAdapter(this, userId);
    feedAdapter.setOnFeedItemClickListener(this);

    rvFeed.setLayoutManager(new LinearLayoutManager(this));
    rvFeed.setItemAnimator(new FeedItemAnimator());
    rvFeed.setAdapter(feedAdapter);
  }

  @Override public void updateFeed(List<FeedItem> feedItems) {
    rvFeed.setVisibility(View.VISIBLE);
    fabCreate.setVisibility(View.VISIBLE);
    pbLoading.setVisibility(View.GONE);
    llNoElements.setVisibility(View.GONE);
    feedAdapter.addAll(feedItems, true);
  }

  @Override public void updatePhotoLike(String photoId, String userId, int likeTapFlags) {
    feedAdapter.updatePhotoLike(photoId, userId, likeTapFlags);
  }

  @Override
  public void addFeedItem(final FeedItem feedItem) {
    llNoElements.setVisibility(View.GONE);
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        rvFeed.smoothScrollToPosition(0);
        feedAdapter.addLoadingView(feedItem);
      }
    }, 500);
  }

  @Override public void showEmptyFeed() {
    pbLoading.setVisibility(View.GONE);
    fabCreate.setVisibility(View.VISIBLE);
    llNoElements.setVisibility(View.VISIBLE);
  }

  @Override public void loginUser() {
  }

  @Override public void showErrorWhileRequestingFeed() {
    pbLoading.setVisibility(View.GONE);
    fabCreate.setVisibility(View.GONE);
    Snackbar
        .make(findViewById(R.id.root), R.string.feed_cannot_request_elements,
            Snackbar.LENGTH_INDEFINITE)
        .setAction(R.string.action_retry, new View.OnClickListener() {
          @Override public void onClick(View v) {
            mainPresenter.requestFeedItems();
          }
        })
        .show();
  }

  @Override public void showErrorWhileLikingPhoto() {
    Snackbar
        .make(findViewById(R.id.root), R.string.feed_cannot_send_action,
            Snackbar.LENGTH_INDEFINITE)
        .show();
  }

  @Override public void showErrorWhileUpdatingFeed() {
    Snackbar
        .make(findViewById(R.id.root), R.string.feed_error_updating_feed,
            Snackbar.LENGTH_INDEFINITE)
        .show();
  }

  @Override public void showErrorNotLoggedUser() {
    pbLoading.setVisibility(View.GONE);
    fabCreate.setVisibility(View.GONE);
    llNoElements.setVisibility(View.GONE);
    Snackbar
        .make(findViewById(R.id.root), R.string.user_not_logged,
            Snackbar.LENGTH_INDEFINITE)
        .setAction(R.string.action_retry, new View.OnClickListener() {
          @Override public void onClick(View v) {
            loginUser();
          }
        })
        .show();
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    if (ACTION_SHOW_LOADING_ITEM.equals(intent.getAction())) {
      String photoId = intent.getStringExtra(EXTRA_PHOTO_ID);
      mainPresenter.requestFeedItem(photoId);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    if (pendingIntroAnimation) {
      pendingIntroAnimation = false;
      startIntroAnimation();
    }
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_sign_out) {
      mainPresenter.requestSignOut();
    }
    return super.onOptionsItemSelected(item);
  }

  private void startIntroAnimation() {
    fabCreate.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));

    int actionbarSize = Utils.dpToPx(56);
    getToolbar().setTranslationY(-actionbarSize);
    getIvLogo().setTranslationY(-actionbarSize);

    getToolbar().animate()
        .translationY(0)
        .setDuration(ANIM_DURATION_TOOLBAR)
        .setStartDelay(300);
    getIvLogo().animate()
        .translationY(0)
        .setDuration(ANIM_DURATION_TOOLBAR)
        .setStartDelay(400);
    fabCreate.animate()
        .translationY(0)
        .setInterpolator(new OvershootInterpolator(1.f))
        .setStartDelay(800)
        .setDuration(ANIM_DURATION_FAB)
        .start();
  }

  @Override
  public void onLikeClick(FeedItem feedItem, int likeSource) {
    mainPresenter.onRequestLike(feedItem.getPhotoId(), likeSource);
  }

  @Override
  public void onCommentsClick(View v, FeedItem feedItem) {
    final Intent intent =
        CommentsActivity.getCallingIntent(this, feedItem.getPhotoId(),
            mainPresenter.getCurrentUserUid());
    int[] startingLocation = new int[2];
    v.getLocationOnScreen(startingLocation);
    intent.putExtra(CommentsActivity.ARG_DRAWING_START_LOCATION, startingLocation[1]);
    startActivity(intent);
    overridePendingTransition(0, 0);
  }

  @OnClick(R.id.btnCreate)
  public void onTakePhotoClick() {
    int[] startingLocation = new int[2];
    fabCreate.getLocationOnScreen(startingLocation);
    startingLocation[0] += fabCreate.getWidth() / 2;
    TakePhotoActivity.startCameraFromLocation(startingLocation, this);
    overridePendingTransition(0, 0);
  }
}
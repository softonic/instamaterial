package com.softonic.instamaterial.ui.activity.comments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import butterknife.BindView;
import com.softonic.instamaterial.R;
import com.softonic.instamaterial.ui.activity.BaseActivity;
import com.softonic.instamaterial.ui.adapter.CommentsAdapter;
import com.softonic.instamaterial.ui.locator.AppServiceLocator;
import com.softonic.instamaterial.ui.model.CommentItem;
import com.softonic.instamaterial.ui.utils.Utils;
import com.softonic.instamaterial.ui.view.SendCommentButton;
import java.util.List;

public class CommentsActivity extends BaseActivity implements SendCommentButton.OnSendClickListener,
    CommentsPresenter.View {
  public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";
  public static final String PHOTO_ID = "photo_id";
  public static final String USER_ID = "user_id";

  @BindView(R.id.contentRoot)
  LinearLayout contentRoot;
  @BindView(R.id.rvComments)
  RecyclerView rvComments;
  @BindView(R.id.llNoComments)
  LinearLayout llNoComments;
  @BindView(R.id.llAddComment)
  LinearLayout llAddComment;
  @BindView(R.id.etComment)
  EditText etComment;
  @BindView(R.id.btnSendComment)
  SendCommentButton btnSendComment;
  @BindView(R.id.flUploading)
  FrameLayout flUploading;

  private LinearLayoutManager linearLayoutManager;
  private CommentsAdapter commentsAdapter;
  private CommentsPresenter commentsPresenter;
  private String photoId;
  private String userId;
  private boolean uploading;

  public static Intent getCallingIntent(Context context, String photoId, String userId) {
    Intent intent = new Intent(context, CommentsActivity.class);
    intent.putExtra(PHOTO_ID, photoId);
    intent.putExtra(USER_ID, userId);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_comments);
    initPresenter();
    photoId = getIntent().getStringExtra(PHOTO_ID);
    userId = getIntent().getStringExtra(USER_ID);

    if (savedInstanceState == null) {
      contentRoot.getViewTreeObserver()
          .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
              contentRoot.getViewTreeObserver().removeOnPreDrawListener(this);
              startIntroAnimation();
              return true;
            }
          });
      commentsPresenter.requestComments(photoId);
    }
    btnSendComment.setOnSendClickListener(this);
  }

  private void initPresenter() {
    CommentsPresenterLocator presenterLocator =
        AppServiceLocator.getInstance().plusActivityServiceLocator();
    commentsPresenter = presenterLocator.commentsPresenter();
    commentsPresenter.attach(this);
  }

  @Override public void setupComments() {
    linearLayoutManager = new LinearLayoutManager(this);
    linearLayoutManager.setStackFromEnd(true);
    rvComments.setLayoutManager(linearLayoutManager);

    commentsAdapter = new CommentsAdapter(this);
    rvComments.setAdapter(commentsAdapter);
    rvComments.setOverScrollMode(View.OVER_SCROLL_NEVER);
    rvComments.setOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
          commentsAdapter.setAnimationsLocked(true);
        }
      }
    });
  }

  @Override public void onFinishedRequestingComments() {
  }

  @Override public void updateComments(List<CommentItem> commentItems) {
    commentsAdapter.addAll(commentItems, true);
    rvComments.setVisibility(View.VISIBLE);
    llNoComments.setVisibility(View.GONE);
  }

  @Override public void showEmptyComments() {
    rvComments.setVisibility(View.GONE);
    llNoComments.setVisibility(View.VISIBLE);
  }

  @Override public void showErrorWhileRequestingComments() {
    Snackbar
        .make(contentRoot, R.string.comments_cannot_request_comments, Snackbar.LENGTH_INDEFINITE)
        .setAction(R.string.action_retry, new View.OnClickListener() {
          @Override public void onClick(View v) {
            commentsPresenter.requestComments(photoId);
          }
        })
        .show();
  }

  @Override public void onCommentAdded(CommentItem commentItem) {
    uploading = false;
    flUploading.setVisibility(View.GONE);
    llNoComments.setVisibility(View.GONE);
    rvComments.setVisibility(View.VISIBLE);
    if (commentsAdapter.add(commentItem)) {
      linearLayoutManager.smoothScrollToPosition(rvComments, null, commentsAdapter.getItemCount());
    }
  }

  @Override public void showUploading() {
    flUploading.setVisibility(View.VISIBLE);
    uploading = true;
  }

  @Override public void showErrorWhileUploading() {
    uploading = false;
    flUploading.setVisibility(View.GONE);
    Snackbar
        .make(findViewById(R.id.root), R.string.comments_cannot_publish_comment,
            Snackbar.LENGTH_INDEFINITE)
        .setAction(R.string.action_retry, new View.OnClickListener() {
          @Override public void onClick(View v) {
            commentsPresenter.requestPublishComment(photoId, etComment.getText().toString());
          }
        })
        .show();
  }

  private void startIntroAnimation() {
    contentRoot.setScaleY(0.1f);
    contentRoot.setPivotY(getIntent().getIntExtra(ARG_DRAWING_START_LOCATION, 0));
    llAddComment.setTranslationY(200);

    contentRoot.animate()
        .scaleY(1)
        .setDuration(200)
        .setInterpolator(new AccelerateInterpolator())
        .setListener(new AnimatorListenerAdapter() {
          @Override
          public void onAnimationEnd(Animator animation) {
            animateContent();
          }
        })
        .start();
  }

  private void animateContent() {
    llAddComment.animate().translationY(0)
        .setInterpolator(new DecelerateInterpolator())
        .setDuration(200)
        .start();
  }

  @Override
  public void onBackPressed() {
    if (!uploading) {
      contentRoot.animate()
          .translationY(Utils.getScreenHeight(this))
          .setDuration(200)
          .setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
              CommentsActivity.super.onBackPressed();
              overridePendingTransition(0, 0);
            }
          })
          .start();
    }
  }

  @Override
  public void onSendClickListener(View v) {
    if (validateComment()) {
      commentsPresenter.requestPublishComment(photoId, etComment.getText().toString());
      commentsAdapter.setAnimationsLocked(false);
      commentsAdapter.setDelayEnterAnimation(false);
      etComment.setText(null);
      btnSendComment.setCurrentState(SendCommentButton.STATE_DONE);
    }
  }

  private boolean validateComment() {
    if (TextUtils.isEmpty(etComment.getText())) {
      btnSendComment.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake_error));
      return false;
    }

    return true;
  }
}

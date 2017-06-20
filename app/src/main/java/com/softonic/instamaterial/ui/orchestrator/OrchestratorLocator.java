package com.softonic.instamaterial.ui.orchestrator;

import android.support.v4.app.FragmentActivity;

public interface OrchestratorLocator {
  GetFeedItem getFeedItem();

  GetFeedItems getFeedItems();

  GetCommentItems getCommentItems();

  GetCommentItem getCommentItem();

  SignIn signIn(FragmentActivity activity);

  SignOut signOut(FragmentActivity activity);
}

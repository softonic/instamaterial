package com.softonic.instamaterial.ui.executor;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import com.softonic.instamaterial.domain.executor.PostExecutor;

public class UIExecutor implements PostExecutor {
  private final Handler handler;

  public UIExecutor() {
    this.handler = new Handler(Looper.getMainLooper());
  }

  @Override public void execute(@NonNull Runnable command) {
    Message message = Message.obtain(handler, command);
    handler.sendMessage(message);
  }
}

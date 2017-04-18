package com.softonic.instamaterial.domain.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public abstract class ObservableTask<R> {
  public abstract void run(Subscriber<R> result);

  public R getResult() {
    final List<R> results = new ArrayList<>(1);
    final Semaphore semaphore = new Semaphore(0);
    run(new Subscriber<R>() {
      @Override public void onSuccess(R result) {
        results.add(0, result);
        semaphore.release();
      }

      @Override public void onError(Exception exception) {
        semaphore.release();
      }
    });
    semaphore.acquireUninterruptibly();
    return !results.isEmpty() ? results.get(0) : null;
  }
}

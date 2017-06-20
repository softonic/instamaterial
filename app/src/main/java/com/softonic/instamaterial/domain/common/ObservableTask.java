package com.softonic.instamaterial.domain.common;

import java.util.concurrent.Semaphore;

public abstract class ObservableTask<R> {
  public abstract void run(Subscriber<R> result);

  public R getResult() {
    final Object[] refResult = new Object[1];
    final Semaphore semaphore = new Semaphore(0);
    ObservableTask.this.run(new Subscriber<R>() {
      @Override public void onSuccess(R result) {
        refResult[0] = result;
        semaphore.release();
      }

      @Override public void onError(Exception exception) {
        semaphore.release();
      }
    });
    semaphore.acquireUninterruptibly();
    return (R) refResult[0];
  }
}

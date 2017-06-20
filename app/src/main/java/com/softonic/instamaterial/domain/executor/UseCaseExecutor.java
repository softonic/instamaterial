package com.softonic.instamaterial.domain.executor;

import com.softonic.instamaterial.domain.common.ObservableTask;
import com.softonic.instamaterial.domain.common.Subscriber;
import com.softonic.instamaterial.domain.common.UseCaseCallback;

public class UseCaseExecutor {
  private final ThreadExecutor threadExecutor;
  private final PostExecutor postExecutor;

  public UseCaseExecutor(ThreadExecutor threadExecutor, PostExecutor postExecutor) {
    this.threadExecutor = threadExecutor;
    this.postExecutor = postExecutor;
  }

  public <R> void execute(final ObservableTask<R> task, final UseCaseCallback<R> callback) {
    threadExecutor.execute(new Runnable() {
      @Override public void run() {
        try {
          task.run(createUseCaseResult(callback));
        } catch (final Exception exception) {
          postExecutor.execute(new Runnable() {
            @Override public void run() {
              callback.onError(exception);
            }
          });
        }
      }
    });
  }

  <R> Subscriber<R> createUseCaseResult(final UseCaseCallback<R> callback) {
    return new Subscriber<R>() {
      @Override public void onSuccess(final R result) {
        postExecutor.execute(new Runnable() {
          @Override public void run() {
            callback.onSuccess(result);
          }
        });
      }

      @Override public void onError(final Exception exception) {
        postExecutor.execute(new Runnable() {
          @Override public void run() {
            callback.onError(exception);
          }
        });
      }
    };
  }
}

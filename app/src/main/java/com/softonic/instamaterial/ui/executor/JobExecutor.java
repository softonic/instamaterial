package com.softonic.instamaterial.ui.executor;

import android.support.annotation.NonNull;
import com.softonic.instamaterial.domain.executor.ThreadExecutor;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class JobExecutor implements ThreadExecutor {
  private static final int INITIAL_POOL_SIZE = 1;
  private static final int MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors();
  private static final int KEEP_ALIVE_TIME = 10;
  private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

  private final ThreadPoolExecutor threadPoolExecutor;

  public JobExecutor() {
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
    ThreadFactory threadFactory = new JobThreadFactory();
    this.threadPoolExecutor = new ThreadPoolExecutor(INITIAL_POOL_SIZE, MAX_POOL_SIZE,
        KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, workQueue, threadFactory);
  }

  @Override public void execute(@NonNull Runnable command) {
    threadPoolExecutor.execute(command);
  }

  private static class JobThreadFactory implements ThreadFactory {
    private static final String THREAD_NAME = "android_";
    private int counter = 0;

    @Override
    public Thread newThread(@NonNull Runnable runnable) {
      return new Thread(runnable, THREAD_NAME + counter++);
    }
  }
}


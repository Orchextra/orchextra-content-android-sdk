package com.gigigo.orchextra.core.data.rxExecutor;

import android.support.annotation.NonNull;
import com.gigigo.orchextra.core.data.rxCache.imageCache.LowPriorityRunnable;
import com.gigigo.orchextra.core.domain.rxExecutor.ThreadExecutor;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;

@Singleton public class JobExecutor implements ThreadExecutor {

  // Sets the amount of time an idle thread waits before terminating
  private static final int KEEP_ALIVE_TIME = 10;
  // Sets the Time Unit to seconds
  private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
  private static int INITIAL_POOL_SIZE = 1;
  private static int MAX_POOL_SIZE = 1;

  private final ThreadPoolExecutor threadPoolExecutor;

  @Inject public JobExecutor() {
    initCores();

    Comparator<Runnable> comparator = (o1, o2) -> {

      int priority1 = 0;
      int priority2 = 0;

      if (o1 instanceof LowPriorityRunnable) priority1 = 1;
      if (o2 instanceof LowPriorityRunnable) priority2 = 1;

      return priority1 - priority2;
    };

    PriorityBlockingQueue<Runnable> workQueue =
        new PriorityBlockingQueue<>(INITIAL_POOL_SIZE, comparator);

    ThreadFactory threadFactory = new JobThreadFactory();
    this.threadPoolExecutor =
        new ThreadPoolExecutor(INITIAL_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
            KEEP_ALIVE_TIME_UNIT, workQueue, threadFactory);
  }

  private void initCores() {
    MAX_POOL_SIZE = 1;
    INITIAL_POOL_SIZE = 1;
  }

  @Override public void execute(@NonNull Runnable runnable) {
    this.threadPoolExecutor.execute(runnable);
  }

  private static class JobThreadFactory implements ThreadFactory {
    private static final String THREAD_NAME = "android_";
    private int counter = 0;

    @Override public Thread newThread(@NonNull Runnable runnable) {
      return new Thread(runnable, THREAD_NAME + counter++);
    }
  }
}

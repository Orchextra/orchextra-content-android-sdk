package com.gigigo.orchextra.core.data.rxExecutor;

import android.util.Log;
import com.gigigo.orchextra.core.data.rxCache.imageCache.ImageDownloader;
import com.gigigo.orchextra.core.data.rxCache.imageCache.LowPriorityRunnable;
import com.gigigo.orchextra.core.domain.rxExecutor.ThreadExecutor;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
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
  //Runtime.getRuntime().availableProcessors() - 1 <= 0 ? 1 : Runtime.getRuntime().availableProcessors() - 1;
  private static int INITIAL_POOL_SIZE = 1;
  private static int MAX_POOL_SIZE = 1;
  private final BlockingQueue<Runnable> workQueue;

  private final ThreadPoolExecutor threadPoolExecutor;

  private final ThreadFactory threadFactory;

  @Inject public JobExecutor() {
    initCores();
    this.workQueue = new PriorityBlockingQueue<>(INITIAL_POOL_SIZE, new Comparator<Runnable>() {
      @Override public int compare(Runnable o1, Runnable o2) {
        Log.v("Priority-o1", o1.getClass().getName() + " | " + (o1 instanceof LowPriorityRunnable));
        Log.v("Priority-o2", o2.getClass().getName() + " | " + (o2 instanceof LowPriorityRunnable));

        int priority1 = 0;
        int priority2 = 0;
        if (o1 instanceof LowPriorityRunnable) priority1 = 1;
        if (o2 instanceof LowPriorityRunnable) priority2 = 1;

        return priority1 - priority2;
      }
    });
    this.threadFactory = new JobThreadFactory();
    this.threadPoolExecutor =
        new ThreadPoolExecutor(INITIAL_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
            KEEP_ALIVE_TIME_UNIT, this.workQueue, this.threadFactory);
  }

  private void initCores() {
    //// Check if we're running on Android 5.0 or higher
    //if (Build.VERSION.SDK_INT >= 21) {
    //  // Call some material design APIs here
    //  INITIAL_POOL_SIZE = 2;
    //  MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors() - 1 <= 0 ? 1
    //      : Runtime.getRuntime().availableProcessors() - 1;
    //} else {
    //  // Implement this feature without material design
    //  INITIAL_POOL_SIZE = 1;
    //  MAX_POOL_SIZE = 1;
    //}

    INITIAL_POOL_SIZE = 2;

    try {
      if (Runtime.getRuntime().availableProcessors() - 1 < 2) {
        MAX_POOL_SIZE = 2;
      } else {
        MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors() - 1;
      }
    } catch (IllegalArgumentException exception) {
      MAX_POOL_SIZE = 2;
    }
  }

  @Override public void execute(Runnable runnable) {
    if (runnable == null) {
      throw new IllegalArgumentException("Runnable to execute cannot be null");
    }
    this.threadPoolExecutor.execute(runnable);
  }

  private static class JobThreadFactory implements ThreadFactory {
    private static final String THREAD_NAME = "android_";
    private int counter = 0;

    @Override public Thread newThread(Runnable runnable) {
      return new Thread(runnable, THREAD_NAME + counter++);
    }
  }
}

package com.gigigo.orchextra.core.sdk.di.modules;

import com.gigigo.interactorexecutor.base.invoker.InteractorInvoker;
import com.gigigo.interactorexecutor.invoker.InteractorInvokerImp;
import com.gigigo.interactorexecutor.invoker.InteractorOutputThreadFactory;
import com.gigigo.interactorexecutor.invoker.InteractorPriorityBlockingQueue;
import com.gigigo.interactorexecutor.invoker.LogExceptionHandler;
import com.gigigo.interactorexecutor.invoker.PriorizableThreadPoolExecutor;
import com.gigigo.orchextra.BuildConfig;
import com.gigigo.orchextra.core.data.utils.ConnectionUtilsImp;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;
import orchextra.javax.inject.Singleton;

@Module public class DomainModule {

  @Provides @Singleton ThreadFactory provideThreadFactory() {
    return new InteractorOutputThreadFactory();
  }

  @Provides @Singleton BlockingQueue<Runnable> provideBlockingQueue() {
    return new InteractorPriorityBlockingQueue(100);
  }

  @Provides @Singleton ExecutorService provideExecutor(ThreadFactory threadFactory,
      BlockingQueue<Runnable> blockingQueue) {
    return new PriorizableThreadPoolExecutor(BuildConfig.CONCURRENT_INTERACTORS,
        BuildConfig.CONCURRENT_INTERACTORS, 0L, TimeUnit.MILLISECONDS, blockingQueue,
        threadFactory);
  }

  @Provides @Singleton LogExceptionHandler provideLogExceptionHandler() {
    return new LogExceptionHandler();
  }

  @Provides @Singleton InteractorInvoker provideInteractorInvoker(ExecutorService executor,
      LogExceptionHandler logExceptionHandler) {
    return new InteractorInvokerImp(executor, logExceptionHandler);
  }

  @Singleton @Provides ConnectionUtils provideConnectionUtils(OcmContextProvider contextProvider) {
    return new ConnectionUtilsImp(contextProvider.getApplicationContext());
  }
}

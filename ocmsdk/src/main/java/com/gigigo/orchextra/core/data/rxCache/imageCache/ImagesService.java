package com.gigigo.orchextra.core.data.rxCache.imageCache;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.gigigo.orchextra.core.sdk.di.injector.Injector;
import com.gigigo.orchextra.ocm.OCManager;
import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;

/**
 * Created by francisco.hernandez on 7/6/17.
 */

@Singleton public class ImagesService extends Service {

  public static final int MIN_RAM_MEMORY = 256;

  @Inject OcmImageCache ocmImageCache;

  public ImagesService() {
    initDI();
  }

  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    if (checkDeviceHasEnoughRamMemory()) {
      ocmImageCache.start();
      return START_STICKY;
    }
    return START_STICKY_COMPATIBILITY;
  }

  private boolean checkDeviceHasEnoughRamMemory() {
    try {
      final Runtime runtime = Runtime.getRuntime();
      final long usedMemInMB = (runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
      final long maxHeapSizeInMB = runtime.maxMemory() / 1048576L;
      final long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;

      return availHeapSizeInMB > MIN_RAM_MEMORY;
    } catch (Exception e) {
      return true;
    }
  }

  @Override public void onDestroy() {
    ocmImageCache.stop();
    super.onDestroy();
  }

  private void initDI() {
    Injector injector = OCManager.getInjector();
    if (injector != null) {
      injector.injectImagesService(this);
    }
  }
}

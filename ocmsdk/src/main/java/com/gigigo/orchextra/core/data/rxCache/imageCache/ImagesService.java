package com.gigigo.orchextra.core.data.rxCache.imageCache;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import com.gigigo.orchextra.core.sdk.di.injector.Injector;
import com.gigigo.orchextra.ocm.OCManager;
import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;

/**
 * Created by francisco.hernandez on 7/6/17.
 */

@Singleton public class ImagesService extends Service {

  @Inject
  OcmImageCache ocmImageCache;

  public ImagesService() {
    initDI();
  }

  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    ocmImageCache.start();
    return super.onStartCommand(intent, flags, startId);
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

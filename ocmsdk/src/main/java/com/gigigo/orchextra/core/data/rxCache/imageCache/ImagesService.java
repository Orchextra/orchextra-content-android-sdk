package com.gigigo.orchextra.core.data.rxCache.imageCache;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.gigigo.orchextra.core.sdk.di.injector.Injector;
import com.gigigo.orchextra.ocm.OCManager;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class ImagesService extends Service {

    @Inject
    OcmImageCache ocmImageCache;

    public ImagesService() {
        initDI();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ocmImageCache != null) {
            ocmImageCache.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (ocmImageCache != null) {
            ocmImageCache.stop();
        }
        super.onDestroy();
    }

    private void initDI() {
        Injector injector = OCManager.getInjector();
        if (injector != null) {
            injector.injectImagesService(this);
        }
    }
}

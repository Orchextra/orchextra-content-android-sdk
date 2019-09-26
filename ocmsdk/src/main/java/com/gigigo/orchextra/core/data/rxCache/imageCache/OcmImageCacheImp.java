package com.gigigo.orchextra.core.data.rxCache.imageCache;

import android.content.Context;

import com.gigigo.orchextra.core.domain.rxExecutor.ThreadExecutor;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;


@Singleton
public class OcmImageCacheImp implements OcmImageCache {

    private static final String TAG = OcmImageCache.class.getSimpleName();

    private final Context mContext;
    private final ThreadExecutor threadExecutor;
    private final ConnectionUtils connectionUtils;
    private final ImageQueue imageQueue;
    private boolean running = false;

    @Inject
    public OcmImageCacheImp(Context mContext, ThreadExecutor executor,
                            ConnectionUtils connectionUtils) {
        this.mContext = mContext;
        this.threadExecutor = executor;
        this.connectionUtils = connectionUtils;
        this.imageQueue = new ImageQueueImp(mContext);
    }

    @Override
    public void start() {
        if (running) return;

        running = true;
        downloadFirst();
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public void add(ImageData imageData) {
        imageQueue.add(imageData);
    }

    private void downloadFirst() {
        if (connectionUtils.isConnectedMobile()) {
            running = false;
            return;
        }
        if (running && imageQueue.hasImages() && DeviceUtils.checkDeviceHasEnoughRamMemory()) {
            executeAsynchronously(new ImageDownloader(imageQueue.getImage(), new Callback() {
                @Override
                public void onSuccess(ImageData imageData) {
                    downloadFirst();
                }

                @Override
                public void onError(ImageData imageData, Exception e) {
                    imageData.consumeRetry();
                    if (imageData.getRetriesLeft() > 0) {
                        imageQueue.add(imageData);
                        Timber.d("RETRIES LEFT (" + imageData.getRetriesLeft() + ") <- " + imageData.getPath());
                    }
                    try {
                        Thread.sleep(1000 * 10);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    downloadFirst();
                }
            }, mContext));
        } else {
            Timber.d("FINISHED");
            running = false;
        }
    }

    private void executeAsynchronously(Runnable runnable) {
        this.threadExecutor.execute(runnable);
    }

    interface Callback {
        void onSuccess(ImageData imageData);

        void onError(ImageData imageData, Exception e);
    }
}

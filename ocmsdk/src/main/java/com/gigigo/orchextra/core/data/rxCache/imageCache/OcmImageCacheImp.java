package com.gigigo.orchextra.core.data.rxCache.imageCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.gigigo.ggglogger.GGGLogImpl;
import com.gigigo.ggglogger.LogLevel;
import com.gigigo.orchextra.core.domain.rxExecutor.ThreadExecutor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;

/**
 * Created by francisco.hernandez on 6/6/17.
 */

@Singleton public class OcmImageCacheImp implements OcmImageCache {

  private static final String TAG = OcmImageCache.class.getSimpleName();

  private final Context mContext;
  private final ThreadExecutor threadExecutor;
  private final ImageQueue imageQueue;
  private boolean started = false;

  @Inject public OcmImageCacheImp(Context mContext, ThreadExecutor executor) {
    this.mContext = mContext;
    this.threadExecutor = executor;
    this.imageQueue = new ImageQueueImp(mContext);
  }

  @Override public void start() {
    started = true;
    downloadFirst();
  }

  @Override public void add(ImageData imageData) {
    imageQueue.add(imageData);
  }

  private void downloadFirst() {
    while (started && imageQueue.hasImages()) {
      executeMainThread(new ImageDownloader(imageQueue.getImage(), mContext));
    }
  }

  private void executeMainThread(Runnable runnable) {
    Handler uiHandler = new Handler(Looper.getMainLooper());
    uiHandler.post(runnable);
  }

  private void executeAsynchronously(Runnable runnable) {
    this.threadExecutor.execute(runnable);
  }

  private class ImageDownloader implements Runnable {
    private final ImageData imageData;
    private final Context mContext;

    public ImageDownloader(ImageData imageData, Context mContext) {
      this.imageData = imageData;
      this.mContext = mContext;
    }

    @Override public void run() {
      downloadImage(imageData);
    }

    private void downloadImage(final ImageData imageData) {
      Log.v("START OcmImageCache -> ", imageData.getPath());
      GGGLogImpl.log("GET -> " + imageData.getPath(), LogLevel.INFO, TAG);
      Glide.with(mContext)
          .load(imageData.getPath())
          .asBitmap()
          .priority(Priority.LOW)
          .diskCacheStrategy(DiskCacheStrategy.SOURCE)
          .skipMemoryCache(true)
          .into(new SimpleTarget<Bitmap>() {
            @Override public void onResourceReady(Bitmap resource,
                GlideAnimation<? super Bitmap> glideAnimation) {
              GGGLogImpl.log("GET <- " + imageData.getPath(), LogLevel.INFO, TAG);
              executeAsynchronously(new ImageSaver(imageData, resource, new ImageSaver.Callback() {
                @Override public void onSuccess() {

                }

                @Override public void onError(ImageData imageData, Exception e) {
                  e.printStackTrace();
                  add(imageData);
                }
              }, mContext));

            }

            @Override public void onLoadFailed(Exception e, Drawable errorDrawable) {
              super.onLoadFailed(e, errorDrawable);
              e.printStackTrace();
            }
          });
    }
  }
}

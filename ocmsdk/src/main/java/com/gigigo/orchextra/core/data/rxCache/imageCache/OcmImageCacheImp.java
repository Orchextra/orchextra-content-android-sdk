package com.gigigo.orchextra.core.data.rxCache.imageCache;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.gigigo.orchextra.core.domain.rxExecutor.ThreadExecutor;
import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;

/**
 * Created by francisco.hernandez on 6/6/17.
 */

@Singleton public class OcmImageCacheImp implements OcmImageCache {

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

  @Override public void add(String url) {
    imageQueue.add(url);
  }

  private void downloadFirst() {
    while (started && imageQueue.hasImages()) {
      executeAsynchronously(new ImageDownloader(imageQueue.getUrl(), mContext));
    }
  }

  private void executeAsynchronously(Runnable runnable) {
    Handler uiHandler = new Handler(Looper.getMainLooper());
    uiHandler.post(runnable);
    //this.threadExecutor.execute(runnable);
  }

  private static class ImageDownloader implements Runnable {
    private final String url;
    private final Context mContext;

    public ImageDownloader(String url, Context mContext) {
      this.url = url;
      this.mContext = mContext;
    }

    @Override public void run() {
      downloadImage(url);
    }

    private void downloadImage(final String url) {
      Log.v("START OcmImageCache -> ", url);
      //Glide.with(mContext)
      //    .load(url)
      //    .priority(Priority.LOW)
      //    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
      //    .skipMemoryCache(true)
      //    .preload();
      Glide.with(mContext)
          .load(url)
          .priority(Priority.LOW)
          .diskCacheStrategy(DiskCacheStrategy.SOURCE)
          .skipMemoryCache(true)
          .into(new SimpleTarget<GlideDrawable>() {
            @Override public void onResourceReady(GlideDrawable resource,
                GlideAnimation<? super GlideDrawable> glideAnimation) {
              Log.v("END  OcmImageCache -> ", url);
            }

            @Override public void onLoadFailed(Exception e, Drawable errorDrawable) {
              super.onLoadFailed(e, errorDrawable);
              e.printStackTrace();
            }
          });
    }
  }
}

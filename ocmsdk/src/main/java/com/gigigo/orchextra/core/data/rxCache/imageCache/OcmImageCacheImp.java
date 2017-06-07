package com.gigigo.orchextra.core.data.rxCache.imageCache;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
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

  private final Context mContext;
  private final ImageQueue imageQueue;
  private boolean started = false;

  @Inject public OcmImageCacheImp(Context mContext) {
    this.mContext = mContext;
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
    if (started && imageQueue.hasImages()) {
      downloadImage(imageQueue.getUrl());
    }
  }

  private synchronized void downloadImage(final String url) {
    Log.v("START OcmImageCache -> ", url);
    Glide.with(mContext).load(url).into(new SimpleTarget<GlideDrawable>() {
      @Override public void onResourceReady(GlideDrawable resource,
          GlideAnimation<? super GlideDrawable> glideAnimation) {
        Log.v("END  OcmImageCache -> ", url);
        downloadFirst();
      }

      @Override public void onLoadFailed(Exception e, Drawable errorDrawable) {
        super.onLoadFailed(e, errorDrawable);
        e.printStackTrace();
      }
    });
  }
}

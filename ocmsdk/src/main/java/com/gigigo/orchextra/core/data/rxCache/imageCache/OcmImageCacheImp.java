package com.gigigo.orchextra.core.data.rxCache.imageCache;

import android.content.Context;
import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;

/**
 * Created by francisco.hernandez on 6/6/17.
 */

@Singleton
public class OcmImageCacheImp implements OcmImageCache {

  public final Context mContext;

  @Inject
  public OcmImageCacheImp(Context mContext) {
    this.mContext = mContext;
  }

  @Override public void downloadImage(String url, int priority) {

  }
}

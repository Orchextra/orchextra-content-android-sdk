package com.gigigo.orchextra.core.data.rxCache.imageCache.loader;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;

public class MyGlideModule implements GlideModule {
  private static final int CACHE_SIZE = 1024 * 50; // 50MB

  @Override public void applyOptions(Context context, GlideBuilder builder) {
    // Apply options to the builder here.
    builder.setDiskCache(
        new DiskLruCacheFactory(context.getCacheDir() + "/thumbnails", CACHE_SIZE));
  }

  @Override public void registerComponents(Context context, Glide glide) {
    // register ModelLoaders here.
  }
}
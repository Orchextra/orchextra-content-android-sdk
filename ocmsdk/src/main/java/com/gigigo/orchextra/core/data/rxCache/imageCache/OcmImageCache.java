package com.gigigo.orchextra.core.data.rxCache.imageCache;


public interface OcmImageCache {

  void start();

  void stop();

  void add(ImageData imageData);
}

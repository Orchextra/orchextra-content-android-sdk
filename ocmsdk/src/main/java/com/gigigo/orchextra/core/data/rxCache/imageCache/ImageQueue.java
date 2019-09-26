package com.gigigo.orchextra.core.data.rxCache.imageCache;


public interface ImageQueue {

  void add(String url, int priority);

  void add(ImageData imageData);

  ImageData getImage();

  boolean hasImages();

  void reset();
}

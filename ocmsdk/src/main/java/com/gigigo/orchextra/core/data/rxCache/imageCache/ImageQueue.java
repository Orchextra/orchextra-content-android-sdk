package com.gigigo.orchextra.core.data.rxCache.imageCache;

/**
 * Created by francisco.hernandez on 6/6/17.
 */

public interface ImageQueue {

  void add(String url, int priority);

  void add(ImageData imageData);

  ImageData getImage();

  boolean hasImages();

  int size();

  void reset();
}

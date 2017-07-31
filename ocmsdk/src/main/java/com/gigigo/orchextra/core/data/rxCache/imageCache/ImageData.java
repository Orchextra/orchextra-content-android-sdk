package com.gigigo.orchextra.core.data.rxCache.imageCache;

/**
 * Created by francisco.hernandez on 6/6/17.
 */

public class ImageData {

  private final String path;
  private int priority;
  private int retriesLeft = 30;

  public ImageData(String path, int priority) {
    this.path = path;
    this.priority = priority;
  }

  public String getPath() {
    return path;
  }

  public int getPriority() {
    return priority;
  }

  public int getRetriesLeft() {
    return retriesLeft;
  }

  public synchronized void consumeRetry() {
    priority++;
    retriesLeft--;
  }
}

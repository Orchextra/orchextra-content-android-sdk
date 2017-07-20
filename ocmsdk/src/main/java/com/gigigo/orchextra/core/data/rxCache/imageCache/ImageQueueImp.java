package com.gigigo.orchextra.core.data.rxCache.imageCache;

import android.content.Context;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import orchextra.javax.inject.Inject;

/**
 * Created by francisco.hernandez on 6/6/17.
 */

public class ImageQueueImp implements ImageQueue {
  public final Context mContext;
  public List<ImageData> queue = new ArrayList<>(); //asv si tal un hashmap

  @Inject public ImageQueueImp(Context mContext) {
    this.mContext = mContext;
  }

  @Override public void add(String url, int priority) {
    ImageData imageData = new ImageData(url, priority);
    add(imageData);
  }

  @Override public synchronized void add(ImageData imageData) {
    queue.add(imageData);
    System.out.println("ocmImageCache size: " + queue.size());
    //Collections.sort(queue, mComparator);
  }

  private final Comparator mComparator =
      (Comparator<ImageData>) (o1, o2) -> o1.getPriority() - o2.getPriority();

  @Override public ImageData getImage() {
    if (hasImages()) {
      ImageData imageData = queue.get(0);
      queue.remove(0);
      return imageData;
    }
    return null;
  }

  @Override public boolean hasImages() {
    return queue != null && queue.size() > 0;
  }

  @Override public int size() {

    if (queue != null) {
      return queue.size();
    } else

    {
      return 0;
    }
  }

  @Override public void reset() {
    queue = null;
    queue = new ArrayList<>();
  }
}

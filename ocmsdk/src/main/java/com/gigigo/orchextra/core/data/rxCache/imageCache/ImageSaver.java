package com.gigigo.orchextra.core.data.rxCache.imageCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.gigigo.ggglogger.GGGLogImpl;
import com.gigigo.ggglogger.LogLevel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by francisco.hernandez on 8/6/17.
 */

public class ImageSaver implements Runnable {
  private static final String TAG = ImageSaver.class.getSimpleName();

  private final ImageData imageData;
  private final Callback callback;
  private final Context mContext;
  private WeakReference<Bitmap> bitmapWeakReference;

  public ImageSaver(ImageData imageData, Bitmap bitmap, Callback callback, Context mContext) {
    this.imageData = imageData;
    this.callback = callback;
    this.mContext = mContext;
    bitmapWeakReference = new WeakReference<>(bitmap);
  }

  @Override public void run() {
    save();
  }

  public void save() {
    GGGLogImpl.log("SAVING -> " + imageData.getPath(), LogLevel.INFO, TAG);
    if (bitmapWeakReference != null && bitmapWeakReference.get() != null) {
      try {
        putBitmapInDiskCache(imageData.getPath(), bitmapWeakReference.get(), mContext);
        GGGLogImpl.log("SAVING <- " + imageData.getPath(), LogLevel.INFO, TAG);
        callback.onSuccess();
      } catch (Exception e) {
        e.printStackTrace();
        GGGLogImpl.log("ERROR <- " + imageData.getPath(), LogLevel.ERROR, TAG);
        callback.onError(imageData, e);
      }
    }
  }

  /**
   * Write bitmap associated with a url to disk cache
   */
  private void putBitmapInDiskCache(String url, Bitmap bitmap, final Context mContext)
      throws Exception {
    // Create a path pointing to the system-recommended cache dir for the app, with sub-dir named
    // thumbnails
    File cacheDir = new File(mContext.getCacheDir(), "images");
    // Create a path in that dir for a file, named by the default hash of the url
    File cacheFile = new File(cacheDir, "" + url.hashCode());
    if (!cacheDir.exists()) cacheDir.mkdir();

    // Create a file at the file path, and open it for writing obtaining the output stream
    cacheFile.createNewFile();
    FileOutputStream fos = new FileOutputStream(cacheFile);
    // Write the bitmap to the output stream (and thus the file) in PNG format (lossless compression)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
    // Flush and close the output stream
    fos.flush();
    fos.close();

    if (!bitmap.isRecycled())
      bitmap.recycle();
    bitmap = null;
  }

  private static String md5(String s) {
    try {
      // Create MD5 Hash
      MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
      digest.update(s.getBytes());
      byte messageDigest[] = digest.digest();

      // Create Hex String
      StringBuffer hexString = new StringBuffer();
      for (int i = 0; i < messageDigest.length; i++)
        hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return "";
  }

  interface Callback {
    void onSuccess();

    void onError(ImageData imageData, Exception e);
  }
}

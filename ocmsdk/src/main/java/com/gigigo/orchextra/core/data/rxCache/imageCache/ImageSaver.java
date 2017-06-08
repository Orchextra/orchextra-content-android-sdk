package com.gigigo.orchextra.core.data.rxCache.imageCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by francisco.hernandez on 8/6/17.
 */

public class ImageSaver {
  private static final String TAG = ImageSaver.class.getSimpleName();

  public static void save(final ImageData imageData, final Callback callback,
      final Context mContext) {
    GGGLogImpl.log("GET -> " + imageData.getPath(), LogLevel.INFO, TAG);
    Glide.with(mContext)
        .load(imageData.getPath())
        .priority(Priority.LOW)
        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
        .skipMemoryCache(true)
        .into(new SimpleTarget<GlideDrawable>() {
          @Override public void onResourceReady(GlideDrawable resource,
              GlideAnimation<? super GlideDrawable> glideAnimation) {
            try {
              writeInDisk(md5(imageData.getPath()), resource, mContext);
              callback.onSuccess();
              GGGLogImpl.log("GET <- " + imageData.getPath(), LogLevel.INFO, TAG);
            } catch (Exception e) {
              callback.onError(imageData, e);
              GGGLogImpl.log("ERROR <- " + imageData.getPath(), LogLevel.ERROR, TAG);
              e.printStackTrace();
            }
          }

          @Override public void onLoadFailed(Exception e, Drawable errorDrawable) {
            super.onLoadFailed(e, errorDrawable);
            callback.onError(imageData, e);
            GGGLogImpl.log("ERROR <- " + imageData.getPath(), LogLevel.ERROR, TAG);
            e.printStackTrace();
          }
        });
  }

  private static void writeInDisk(final String filename, final GlideDrawable drawable,
      final Context mContext) throws Exception {
    Bitmap bm = ((BitmapDrawable) (Drawable) drawable).getBitmap();
    File file = new File(mContext.getCacheDir(), "images/" + filename);
    FileOutputStream outStream = new FileOutputStream(file);
    bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
    outStream.flush();
    outStream.close();
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

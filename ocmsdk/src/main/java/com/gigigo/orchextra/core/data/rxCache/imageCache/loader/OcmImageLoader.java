package com.gigigo.orchextra.core.data.rxCache.imageCache.loader;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v4.app.FragmentActivity;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.gigigo.ggglogger.GGGLogImpl;
import com.gigigo.ggglogger.LogLevel;
import com.gigigo.orchextra.ocmsdk.R;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by francisco.hernandez on 12/6/17.
 */

public class OcmImageLoader {
  private static String TAG = OcmImageLoader.class.getSimpleName();
  public static boolean DEBUG = true;

  public static DrawableRequestBuilder<String> load(Context mContext, String url) {
    File cacheFile = getCacheFile(mContext, md5(url));
    if (cacheFile.exists()) {
      GGGLogImpl.log("(DISK)  " + url, LogLevel.INFO, TAG);
      return Glide.with(mContext)

          .load(cacheFile.getPath()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
          .thumbnail(Glide.with(mContext).load(R.drawable.thumbnail).centerCrop())
          //.transform(new CacheTransformation(mContext, false))
          ;
    } else {
      GGGLogImpl.log("(CLOUD) " + url, LogLevel.INFO, TAG);
      return Glide.with(mContext)
          .load(url).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
          .thumbnail(Glide.with(mContext).load(R.drawable.thumbnail).centerCrop())
          //.transform(new CacheTransformation(mContext, true))
          ;
    }
  }

  public static DrawableRequestBuilder<String> load(Activity mActivity, String url) {
    File cacheFile = getCacheFile(mActivity, md5(url));
    if (cacheFile.exists()) {
      GGGLogImpl.log("(DISK)  " + url, LogLevel.INFO, TAG);
      return Glide.with(mActivity)
          .load(cacheFile.getPath()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
          .thumbnail(Glide.with(mActivity).load(R.drawable.thumbnail).centerCrop())
          //.transform(new CacheTransformation(mActivity.getApplicationContext(), false))
          ;
    } else {
      GGGLogImpl.log("(CLOUD) " + url, LogLevel.INFO, TAG);
      return Glide.with(mActivity)
          .load(url).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
          .thumbnail(Glide.with(mActivity).load(R.drawable.thumbnail).centerCrop())
          //.transform(new CacheTransformation(mActivity.getApplicationContext(), true))
          ;
    }
  }

  public static DrawableRequestBuilder<String> load(Fragment mFragment, String url) {
    File cacheFile = getCacheFile(mFragment.getActivity().getApplicationContext(), md5(url));
    if (cacheFile.exists()) {
      GGGLogImpl.log("(DISK)  " + url, LogLevel.INFO, TAG);
      return Glide.with(mFragment)
          .load(cacheFile.getPath()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
          .thumbnail(Glide.with(mFragment).load(R.drawable.thumbnail).centerCrop())
          //.transform(
          //    new CacheTransformation(mFragment.getActivity().getApplicationContext(), true))
          ;
    } else {
      GGGLogImpl.log("(CLOUD) " + url, LogLevel.INFO, TAG);
      return Glide.with(mFragment)
          .load(url).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
          .thumbnail(Glide.with(mFragment).load(R.drawable.thumbnail).centerCrop())
          //.transform(
          //    new CacheTransformation(mFragment.getActivity().getApplicationContext(), true))
          ;
    }
  }

  public static DrawableRequestBuilder<String> load(android.support.v4.app.Fragment mFragment,
      String url) {
    File cacheFile = getCacheFile(mFragment.getActivity().getApplicationContext(), md5(url));
    if (cacheFile.exists()) {
      GGGLogImpl.log("(DISK)  " + url, LogLevel.INFO, TAG);
      return Glide.with(mFragment)
          .load(cacheFile.getPath()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
          .thumbnail(Glide.with(mFragment).load(R.drawable.thumbnail).centerCrop())
          //.transform(new CacheTransformation(mFragment.getContext(), false))
          ;
    } else {
      GGGLogImpl.log("(CLOUD) " + url, LogLevel.INFO, TAG);
      return Glide.with(mFragment)
          .load(url).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
          .thumbnail(Glide.with(mFragment).load(R.drawable.thumbnail).centerCrop())
          //.transform(new CacheTransformation(mFragment.getContext(), true))
          ;
    }
  }

  public static DrawableRequestBuilder<String> load(FragmentActivity mFragmentActivity, String url) {
    File cacheFile = getCacheFile(mFragmentActivity, md5(url));
    if (cacheFile.exists()) {
      GGGLogImpl.log("(DISK)  " + url, LogLevel.INFO, TAG);
      return Glide.with(mFragmentActivity).load(cacheFile.getPath())
          .thumbnail(Glide.with(mFragmentActivity).load(R.drawable.thumbnail).centerCrop());
    } else {
      GGGLogImpl.log("(CLOUD) " + url, LogLevel.INFO, TAG);
      return Glide.with(mFragmentActivity).load(url)
          .thumbnail(Glide.with(mFragmentActivity).load(R.drawable.thumbnail).centerCrop());
    }
  }

  public static String md5(String s) {
    if (s.contains("?")) {
      int index = s.indexOf("?");
      s = s.substring(0, index);
    }
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
    return s;
  }

  public static File getCacheDir(Context mContext) {
    // Create a path pointing to the system-recommended cache dir for the app, with sub-dir named
    // thumbnails
    File cacheDir = new File(mContext.getCacheDir(), "images");
    return cacheDir;
  }

  public static File getCacheFile(Context mContext, String filename) {
    File cacheFile = new File(getCacheDir(mContext), filename);
    return cacheFile;
  }

  private static class CacheTransformation extends BitmapTransformation {
    private final boolean fromCloud;
    private final Context mContext;

    public CacheTransformation(Context context, boolean fromCloud) {
      super(context);
      this.fromCloud = fromCloud;
      this.mContext = context;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {

      Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(),
          R.drawable.ox_notification_alpha_small_icon);

      int w = toTransform.getWidth();
      int h = toTransform.getHeight();
      double ratio = (double) w / (double) h;
      Bitmap result = Bitmap.createBitmap(w, h, toTransform.getConfig());

      Canvas canvas = new Canvas(result);
      canvas.drawBitmap(toTransform, 0, 0, null);

      if (!fromCloud) canvas.drawBitmap(icon, (int) (150 * ratio), (int) (100 * ratio), null);

      toTransform.recycle();
      return result;
    }

    @Override public String getId() {
      // Return some id that uniquely identifies your transformation.
      return getClass().getSimpleName();
    }
  }
}

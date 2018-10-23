package com.gigigo.orchextra.core.sdk.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;
import java.lang.ref.WeakReference;

public class ImageGenerator {

  private static final String ORIGINAL_WIDTH = "originalwidth";
  private static final String ORIGINAL_HEIGHT = "originalheight";

  public static String generateImageUrl(String url, int widthPixels, int heightPixels) {
    if (url == null) {
      return "";
    }

    Uri uriUrl = Uri.parse(url)
        .buildUpon()
        .appendQueryParameter("w", String.valueOf(widthPixels))
        .appendQueryParameter("h", String.valueOf(heightPixels))
        .build();
    return uriUrl.toString();
  }

  public static String generateImageUrl(String url, int widthPixels) {
    Uri uriUrl =
        Uri.parse(url).buildUpon().appendQueryParameter("w", String.valueOf(widthPixels)).build();
    return uriUrl.toString();
  }

  public static void generateThumbImage(String imageThumb, ImageView imageView) {
    if (!TextUtils.isEmpty(imageThumb)) {
      byte[] decodedImageString = Base64.decode(imageThumb, Base64.DEFAULT);
      Bitmap decodedImageByte =
          BitmapFactory.decodeByteArray(decodedImageString, 0, decodedImageString.length);

      WeakReference<Bitmap> weakReference = new WeakReference<>(decodedImageByte);

      imageView.setImageBitmap(weakReference.get());
    }
  }

  public static float getRatioImage(String url) {

    try {
      if (url == null) {
        return -1;
      }

      Uri parse = Uri.parse(url);

      String queryParameterWidth = parse.getQueryParameter(ORIGINAL_WIDTH);
      float originalWidth = Float.valueOf(queryParameterWidth);

      String queryParameterheight = parse.getQueryParameter(ORIGINAL_HEIGHT);
      float originalHeight = Float.valueOf(queryParameterheight);

      return originalHeight != 0 ? originalWidth / originalHeight : -1;
    } catch (Exception e) {
      return -1;
    }
  }
}

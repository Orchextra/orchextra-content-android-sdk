package com.gigigo.orchextra.core.sdk.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;
import java.lang.ref.WeakReference;

public class ImageGenerator {

  public static String generateImageUrl(String url, int widthPixels, int heightPixels) {
    Uri uriUrl = Uri.parse(url).buildUpon()
        .appendQueryParameter("w", String.valueOf(widthPixels))
        .appendQueryParameter("h", String.valueOf(heightPixels))
        .build();
    return uriUrl.toString();
  }

  public static String generateImageUrl(String url, int widthPixels) {
    Uri uriUrl = Uri.parse(url).buildUpon()
        .appendQueryParameter("w", String.valueOf(widthPixels))
        .build();
    return uriUrl.toString();
  }

  public static void generateThumbImage(String imageThumb, ImageView imageView) {
    if (!TextUtils.isEmpty(imageThumb)) {
      byte[] decodedImageString = Base64.decode(imageThumb, Base64.DEFAULT);
      Bitmap decodedImageByte = BitmapFactory.decodeByteArray(decodedImageString, 0, decodedImageString.length);

      WeakReference<Bitmap> weakReference = new WeakReference<>(decodedImageByte);

      imageView.setImageBitmap(weakReference.get());
    }
  }
}

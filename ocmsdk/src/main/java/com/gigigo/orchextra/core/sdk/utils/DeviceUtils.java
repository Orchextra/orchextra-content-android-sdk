package com.gigigo.orchextra.core.sdk.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.support.customtabs.CustomTabsIntent;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import com.gigigo.ggglib.device.AndroidSdkVersion;
import com.gigigo.orchextra.ocmsdk.R;

public class DeviceUtils {

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
  public static int calculateRealHeightDevice(Context context) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

    DisplayMetrics metrics = new DisplayMetrics();
    Display display = wm.getDefaultDisplay();

    if (AndroidSdkVersion.hasJellyBean17()) {
      display.getRealMetrics(metrics);
      return metrics.heightPixels;
    } else {
      Point size = new Point();
      display.getSize(size);
      return size.y;
    }
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
  public static int calculateRealWidthDevice(Context context) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

    DisplayMetrics metrics = new DisplayMetrics();
    Display display = wm.getDefaultDisplay();

    if (AndroidSdkVersion.hasJellyBean17()) {
      display.getRealMetrics(metrics);
      return metrics.widthPixels;
    } else {
      Point size = new Point();
      display.getSize(size);
      return size.x;
    }
  }

  public static void openChromeTabs(Activity activity, String url) {
    if (activity != null && url!=null) {
      CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
      builder.setToolbarColor(
          activity.getResources().getColor(R.color.oc_background_detail_toolbar));
      //builder.setCloseButtonIcon(BitmapFactory.decodeResource(
      //    getResources(), android.R.drawable.ic_menu_b));
      CustomTabsIntent customTabsIntent = builder.build();
      customTabsIntent.launchUrl(activity, Uri.parse(url));
    }
  }
}

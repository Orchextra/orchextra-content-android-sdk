package com.gigigo.orchextra.core.sdk.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DeviceUtils {

  public static int calculateRealHeightDevice(Context context) {
    DisplayMetrics metrics = new DisplayMetrics();
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    wm.getDefaultDisplay().getRealMetrics(metrics);
    return metrics.heightPixels;
  }

  public static int calculateRealWidthDevice(Context context) {
    DisplayMetrics metrics = new DisplayMetrics();
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    wm.getDefaultDisplay().getRealMetrics(metrics);
    return metrics.widthPixels;
  }
}

package com.gigigo.showcase;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {

  private Utils() {
  }

  public static boolean isOnline(Context context) {
    ConnectivityManager cm =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = null;
    if (cm != null) {
      netInfo = cm.getActiveNetworkInfo();
    }
    return netInfo != null && netInfo.isConnectedOrConnecting();
  }
}

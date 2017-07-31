package com.gigigo.orchextra.core.receiver;

/**
 * Created by francisco.hernandez on 12/6/17.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.gigigo.orchextra.core.data.rxCache.imageCache.ImagesService;
import com.gigigo.orchextra.core.data.rxCache.imageCache.OcmImageCache;
import com.gigigo.orchextra.core.sdk.di.injector.Injector;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocmsdk.R;
import orchextra.javax.inject.Inject;

/**
 * Receives wifi changes and creates a notification when wifi connects to a network,
 * displaying the SSID and MAC address.
 *
 * Put the following in your manifest
 *
 * <receiver android:name=".WifiReceiver" android:exported="false" >
 * <intent-filter>
 * <action android:name="android.net.wifi.WIFI_STATE_CHANGED" />
 * </intent-filter>
 * </receiver>
 * <service android:name=".WifiReceiver$WifiActiveService" android:exported="false" />
 *
 * To activate logging use: adb shell setprop log.tag.WifiReceiver VERBOSE
 */
public class WifiReceiver extends BroadcastReceiver {

  private final static String TAG = WifiReceiver.class.getSimpleName();
  public static Intent intentService = null;

  @Override public void onReceive(final Context context, final Intent intent) {
    int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
    intentService = new Intent(context, ImagesService.class);
    if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())
        && WifiManager.WIFI_STATE_ENABLED == wifiState) {
      if (Log.isLoggable(TAG, Log.VERBOSE)) {
        Log.v(TAG, "Wifi is now enabled");
      }
      context.startService(intentService);
    } else {
      if (intentService != null) {
        context.stopService(intentService);
        intentService = null;
      }
    }
  }

}

package com.gigigo.orchextra.core.data.api.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import com.gigigo.orchextra.core.domain.utils.ConnectionStates;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;

public class ConnectionUtilsImp implements ConnectionUtils {

  private Context context;

  public ConnectionUtilsImp(Context context) {
    this.context = context;
  }

  @Override public boolean hasConnection() {
    ConnectionStates connectionStates = getActiveConnectionStatus();
    return connectionStates == ConnectionStates.WIFI || connectionStates == ConnectionStates.MOBILE;
  }

  private ConnectionStates getActiveConnectionStatus() {
    ConnectionStates connectionState;

    try {

      ConnectivityManager connManager =
          (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
      if (connManager != null) {

        NetworkInfo activeNetworkInfo = connManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
          if (activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected()) {

            switch (activeNetworkInfo.getType()) {
              case ConnectivityManager.TYPE_WIFI:
              case ConnectivityManager.TYPE_WIMAX:

                WifiManager wifiManager =
                    (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
                if (wifiManager != null) {

                  WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                  if (wifiInfo != null) {
                    SupplicantState supState = wifiInfo.getSupplicantState();
                    if (supState.equals(SupplicantState.COMPLETED)) {
                      connectionState = ConnectionStates.WIFI;
                    } else {
                      connectionState = ConnectionStates.DISCONNECT;
                    }
                  } else {
                    connectionState = ConnectionStates.ERROR;
                  }
                } else {
                  connectionState = ConnectionStates.ERROR;
                }
                break;
              case ConnectivityManager.TYPE_MOBILE:
              case ConnectivityManager.TYPE_MOBILE_DUN:

                if (activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
                  connectionState = ConnectionStates.MOBILE;
                } else {
                  connectionState = ConnectionStates.DISCONNECT;
                }
                break;
              default:
                connectionState = ConnectionStates.DISCONNECT;
                break;
            }
          } else {
            connectionState = ConnectionStates.DISCONNECT;
          }
        } else {
          connectionState = ConnectionStates.ERROR;
        }
      } else {
        connectionState = ConnectionStates.ERROR;
      }
    } catch (Exception e) {
      connectionState = ConnectionStates.ERROR;
    }

    return connectionState;
  }

  @Override public boolean isConnectedWifi() {
    NetworkInfo info = getNetworkInfo(context);
    return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
  }

  @Override public boolean isConnectedMobile() {
    NetworkInfo info = getNetworkInfo(context);
    return (info != null
        && info.isConnected()
        && info.getType() == ConnectivityManager.TYPE_MOBILE);
  }

  @Override public boolean isConnectedFast() {
    NetworkInfo info = getNetworkInfo(context);
    return (info != null && info.isConnected() && isConnectionFast(info.getType(),
        info.getSubtype()));
  }

  private NetworkInfo getNetworkInfo(Context context) {
    ConnectivityManager cm =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    return cm.getActiveNetworkInfo();
  }

  private boolean isConnectionFast(int type, int subType) {
    if (type == ConnectivityManager.TYPE_WIFI) {
      return true;
    } else if (type == ConnectivityManager.TYPE_MOBILE) {
      switch (subType) {
        case TelephonyManager.NETWORK_TYPE_1xRTT:
          return false; // ~ 50-100 kbps
        case TelephonyManager.NETWORK_TYPE_CDMA:
          return false; // ~ 14-64 kbps
        case TelephonyManager.NETWORK_TYPE_EDGE:
          return false; // ~ 50-100 kbps
        case TelephonyManager.NETWORK_TYPE_EVDO_0:
          return true; // ~ 400-1000 kbps
        case TelephonyManager.NETWORK_TYPE_EVDO_A:
          return true; // ~ 600-1400 kbps
        case TelephonyManager.NETWORK_TYPE_GPRS:
          return false; // ~ 100 kbps
        case TelephonyManager.NETWORK_TYPE_HSDPA:
          return true; // ~ 2-14 Mbps
        case TelephonyManager.NETWORK_TYPE_HSPA:
          return true; // ~ 700-1700 kbps
        case TelephonyManager.NETWORK_TYPE_HSUPA:
          return true; // ~ 1-23 Mbps
        case TelephonyManager.NETWORK_TYPE_UMTS:
          return true; // ~ 400-7000 kbps
            /*
             * Above API level 7, make sure to set android:targetSdkVersion
             * to appropriate level to use these
             */
        case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
          return true; // ~ 1-2 Mbps
        case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
          return true; // ~ 5 Mbps
        case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
          return true; // ~ 10-20 Mbps
        case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
          return false; // ~25 kbps
        case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
          return true; // ~ 10+ Mbps
        // Unknown
        case TelephonyManager.NETWORK_TYPE_UNKNOWN:
        default:
          return false;
      }
    } else {
      return false;
    }
  }
}

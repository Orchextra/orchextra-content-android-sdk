package com.gigigo.orchextra.core.data.api.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.gigigo.orchextra.core.domain.utils.ConnectionStates;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;

/**
 * Created by rui.alonso on 13/11/15.
 */
public class ConnectionUtilsImp implements ConnectionUtils {

  private Context context;

  public ConnectionUtilsImp(Context context) {
    this.context = context;
  }

  @Override
  public boolean hasConnection() {
    ConnectionStates connectionStates = getActiveConnectionStatus();
    return connectionStates == ConnectionStates.WIFI || connectionStates == ConnectionStates.MOBILE;
  }

  private ConnectionStates getActiveConnectionStatus() {
    ConnectionStates connectionState;

    try {

      ConnectivityManager connManager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
      if (connManager != null) {

        NetworkInfo activeNetworkInfo = connManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
          if (activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected()) {

            switch (activeNetworkInfo.getType()) {
              case ConnectivityManager.TYPE_WIFI:
              case ConnectivityManager.TYPE_WIMAX:

                WifiManager wifiManager = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
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
}

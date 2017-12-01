package com.gigigo.orchextra.wrapper;

import android.app.Application;
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver;

/**
 * Created by alex on 01/12/2017.
 */

public interface OxManager {

  void startImageRecognition();
  void startScanner();
  void init(Application app,
      String oxKey,
      String oxSecret,
      Class notificationActivityClass,
      String senderId,
      ImageRecognition vuforia,
      OrchextraCompletionCallback orchextraCompletionCallback);

  void getToken();
  void bindUser(CrmUser crmUser);
  void unBindUser();
  void bindDevice(String device);
  void unBindDevice();
  void setCustomSchemeReceiver(OnCustomSchemeReceiver onCustomSchemeReceiver);
  void start();
  void stop();
  void updateSDKCredentials(String apiKey, String apiSecret, boolean forceCallback);
}

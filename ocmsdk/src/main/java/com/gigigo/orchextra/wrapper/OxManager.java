package com.gigigo.orchextra.wrapper;

import android.app.Application;
import android.support.annotation.NonNull;
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver;

/**
 * Created by alex on 01/12/2017.
 */

public interface OxManager {

  void startImageRecognition();

  void startScanner();

  void init(Application app, Config config);

  void getToken();

  void bindUser(CrmUser crmUser);

  void unBindUser();

  void bindDevice(String device);

  void unBindDevice();

  void setOnCustomSchemeReceiver(OnCustomSchemeReceiver onCustomSchemeReceiver);

  void callOnCustomSchemeReceiver(String customScheme);

  void start();

  void stop();

  void updateSDKCredentials(String apiKey, String apiSecret, boolean forceCallback);

  void scanCode(ScanCodeListener scanCodeListener);

  final class Config {
    String apiKey;
    String apiSecret;
    Class notificationActivityClass;
    String senderId;
    ImageRecognition vuforia;
    OrchextraCompletionCallback orchextraCompletionCallback;

    public Config(Builder builder) {
      apiKey = builder.apiKey;
      apiSecret = builder.apiSecret;
      notificationActivityClass = builder.notificationActivityClass;
      senderId = builder.senderId;
      vuforia = builder.vuforia;
      orchextraCompletionCallback = builder.orchextraCompletionCallback;
    }

    public String getApiKey() {
      return apiKey;
    }

    public String getApiSecret() {
      return apiSecret;
    }

    public Class getNotificationActivityClass() {
      return notificationActivityClass;
    }

    public String getSenderId() {
      return senderId;
    }

    public ImageRecognition getVuforia() {
      return vuforia;
    }

    public OrchextraCompletionCallback getOrchextraCompletionCallback() {
      return orchextraCompletionCallback;
    }

    public static final class Builder {
      String apiKey;
      String apiSecret;
      Class notificationActivityClass;
      String senderId;
      ImageRecognition vuforia;
      OrchextraCompletionCallback orchextraCompletionCallback;

      public Builder() {

      }

      public Builder setApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
      }

      public Builder setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
        return this;
      }

      public Builder setNotificationActivityClass(Class notificationActivityClass) {
        this.notificationActivityClass = notificationActivityClass;
        return this;
      }

      public Builder setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
      }

      public Builder setVuforia(ImageRecognition vuforia) {
        this.vuforia = vuforia;
        return this;
      }

      public Builder setOrchextraCompletionCallback(
          OrchextraCompletionCallback orchextraCompletionCallback) {
        this.orchextraCompletionCallback = orchextraCompletionCallback;
        return this;
      }

      public Config build() {
        return new Config(this);
      }
    }
  }

  interface ScanCodeListener {
    void onCodeScan(@NonNull String code);
  }
}

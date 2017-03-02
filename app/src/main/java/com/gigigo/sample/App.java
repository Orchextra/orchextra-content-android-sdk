package com.gigigo.sample;

import android.app.Application;
import com.gigigo.orchextra.CustomSchemeReceiver;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.OcmBuilder;
import com.gigigo.orchextra.ocm.OcmEvent;
import com.gigigo.orchextra.ocm.OcmStyleUiBuilder;
import com.gigigo.orchextra.ocm.callbacks.OcmCredentialCallback;
import com.gigigo.orchextra.ocm.callbacks.OnEventCallback;
import com.gigigo.orchextra.ocm.callbacks.OnRequiredLoginCallback;

public class App extends Application {

  public static String API_KEY = "aba98891a3886e5909312ff489201163dff0fe93";
  public static String API_SECRET = "3e83f8a0623a8028ba8f6d0d63846e22cab55f63";

  private CustomSchemeReceiver onCustomSchemeReceiver = new CustomSchemeReceiver() {
    @Override public void onReceive(String s) {

    }
  };
  private OnRequiredLoginCallback onDoRequiredLoginCallback = new OnRequiredLoginCallback() {
    @Override public void doRequiredLogin() {

    }
  };
  private OnEventCallback onEventCallback = new OnEventCallback() {
    @Override public void doEvent(OcmEvent event, Object data) {

    }
  };

  @Override public void onCreate() {
    super.onCreate();

    OcmBuilder ocmBuilder = new OcmBuilder(this).setNotificationActivityClass(MainActivity.class)
        .setOrchextraCredentials("FAKE_KEY", "FAKE_SECRET")
        .setContentLanguage("IT")
        .setOnDoRequiredLoginCallback(onDoRequiredLoginCallback)
        .setOnEventCallback(onEventCallback)
        .setOnCustomSchemeReceiver(onCustomSchemeReceiver);

    Ocm.initialize(ocmBuilder);

    OcmStyleUiBuilder ocmStyleUiBuilder = new OcmStyleUiBuilder();

    Ocm.setStyleUi(ocmStyleUiBuilder);

    //Ocm.start();

    Ocm.startWithCredentials(API_KEY, API_SECRET, new OcmCredentialCallback() {
      @Override public void onCredentialReceiver(String accessToken) {

      }
    });
  }
}

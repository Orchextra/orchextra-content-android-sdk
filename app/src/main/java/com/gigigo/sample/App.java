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
import com.gigigo.orchextra.ocm.callbacks.OnRetrieveUiMenuListener;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import java.util.List;

public class App extends Application {

  public static String API_KEY = "8286702045adf5a3ad816f70ecb80e4c91fbb8de";
  public static String API_SECRET = "eab37080130215ced60eb9d5ff729049749ec205";

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
        .setContentLanguage("ES")
        .setOnDoRequiredLoginCallback(onDoRequiredLoginCallback)
        .setOnEventCallback(onEventCallback)
        .setOnCustomSchemeReceiver(onCustomSchemeReceiver);

    Ocm.initialize(ocmBuilder);

    OcmStyleUiBuilder ocmStyleUiBuilder = new OcmStyleUiBuilder();

    Ocm.setStyleUi(ocmStyleUiBuilder);

    Ocm.setBusinessUnit("it");
  }
}

package com.gigigo.sample;

import android.app.Application;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.OcmBuilder;
import com.gigigo.orchextra.ocm.OcmEvent;
import com.gigigo.orchextra.ocm.OcmStyleUiBuilder;
import com.gigigo.orchextra.ocm.callbacks.OnEventCallback;
import com.gigigo.orchextra.ocm.callbacks.OnRequiredLoginCallback;

public class App extends Application {

  //public static String API_KEY = "a2966ba69f4ead1a4f1550bfda450e9fd07e6762";   //Asv project
  //public static String API_SECRET = "f79713d7e9b0fcd69fedfb94f471106cb85d8ca4";

  public static String API_KEY = "8286702045adf5a3ad816f70ecb80e4c91fbb8de";    //Woah project
  public static String API_SECRET = "eab37080130215ced60eb9d5ff729049749ec205";

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
        .setContentLanguage("EN")
        .setOnDoRequiredLoginCallback(onDoRequiredLoginCallback)
        .setOnEventCallback(onEventCallback);

    Ocm.initialize(ocmBuilder);

    OcmStyleUiBuilder ocmStyleUiBuilder = new OcmStyleUiBuilder();

    Ocm.setStyleUi(ocmStyleUiBuilder);

    Ocm.setBusinessUnit("it");
  }
}

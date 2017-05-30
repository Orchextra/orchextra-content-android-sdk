package com.gigigo.sample;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.OcmBuilder;
import com.gigigo.orchextra.ocm.OcmEvent;
import com.gigigo.orchextra.ocm.OcmStyleUiBuilder;
import com.gigigo.orchextra.ocm.callbacks.OnEventCallback;
import com.gigigo.orchextra.ocm.callbacks.OnRequiredLoginCallback;


//MultiDexApplication
public class App extends MultiDexApplication {

  //public static String API_KEY = "a2966ba69f4ead1a4f1550bfda450e9fd07e6762";   //Asv project
  //public static String API_SECRET = "f79713d7e9b0fcd69fedfb94f471106cb85d8ca4";

  //public static String API_KEY = "8286702045adf5a3ad816f70ecb80e4c91fbb8de";    //Woah project
  //public static String API_SECRET = "eab37080130215ced60eb9d5ff729049749ec205";

  //public static String API_KEY = "adfc8ba4340828a054bf061f692707a197af96cb";    //[UAT][CSE] - WOAH SITC project
  //public static String API_SECRET = "677cf75a17aeec144ee402c281ad3a732d736a8a";

  //REPSOL RELEASE
  public static String API_KEY = "7bb9fa0f9b7a02846383fd6284d3c74b8155644c";    //[UAT][CSE] - DEMO CONTENT
  public static String API_SECRET = "3295dc8de90300e2977e6cec5b28b614fc644934";

  private OnRequiredLoginCallback onDoRequiredLoginCallback = new OnRequiredLoginCallback() {
    @Override public void doRequiredLogin() {

    }
  };
  private OnEventCallback onEventCallback = new OnEventCallback() {
    @Override public void doEvent(OcmEvent event, Object data) {
    }

    @Override public void doEvent(OcmEvent event) {
    }
  };

  @Override public void onCreate() {
    super.onCreate();
    // if (LeakCanary.isInAnalyzerProcess(this)) {
    //   //This process is dedicated to LeakCanary for heap analysis.
    //   //You should not init your app in this process.
    //  return;
    //}
    //LeakCanary.install(this);
    //// Normal app init code...

    MultiDex.install(this);

    OcmBuilder ocmBuilder = new OcmBuilder(this).setNotificationActivityClass(MainActivity.class)
        .setOrchextraCredentials("FAKE_KEY", "FAKE_SECRET")
        .setContentLanguage("EN")
        .setOnDoRequiredLoginCallback(onDoRequiredLoginCallback)
        .setOnEventCallback(onEventCallback);

    Ocm.initialize(ocmBuilder);

    OcmStyleUiBuilder ocmStyleUiBuilder = new OcmStyleUiBuilder();

    Ocm.setStyleUi(ocmStyleUiBuilder);

    Ocm.setBusinessUnit("pl");
  }
}

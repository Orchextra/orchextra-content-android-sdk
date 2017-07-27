package com.gigigo.sample;

import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.OcmBuilder;
import com.gigigo.orchextra.ocm.OcmEvent;
import com.gigigo.orchextra.ocm.OcmStyleUiBuilder;
import com.gigigo.orchextra.ocm.callbacks.OnEventCallback;
import com.gigigo.orchextra.ocm.callbacks.OnRequiredLoginCallback;
import com.squareup.leakcanary.LeakCanary;

//MultiDexApplication
public class App extends MultiDexApplication {

  //public static String API_KEY = "a2966ba69f4ead1a4f1550bfda450e9fd07e6762";   //Asv project
  //public static String API_SECRET = "f79713d7e9b0fcd69fedfb94f471106cb85d8ca4";

  //WOAH PRELAUNCH
  //public static String API_KEY = "8286702045adf5a3ad816f70ecb80e4c91fbb8de";
  //public static String API_SECRET = "eab37080130215ced60eb9d5ff729049749ec205";

  //public static String API_KEY = "adfc8ba4340828a054bf061f692707a197af96cb";    //[UAT][CSE] - WOAH SITC project --- Staging
  //public static String API_SECRET = "677cf75a17aeec144ee402c281ad3a732d736a8a";

  //WOAH MARKETS
  //public static String API_KEY = "ef08c4dccb7649b9956296a863db002a68240be2";    //Woah project
  //public static String API_SECRET = "6bc18c500546f253699f61c11a62827679178400";

  //REPSOL RELEASE
  //public static String API_KEY = "7bb9fa0f9b7a02846383fd6284d3c74b8155644c";
  //public static String API_SECRET = "3295dc8de90300e2977e6cec5b28b614fc644934";

  //REPSOL DEBUG
  public static String API_KEY = "53cf8785f1f02b5a69adbddee58bb33b6094eb5b";    //Woah project
  public static String API_SECRET = "e1d718aefde8aab04621a7acf1771dfbf5884fef";

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
    enableStrictMode();
    super.onCreate();
     if (LeakCanary.isInAnalyzerProcess(this)) {
       //This process is dedicated to LeakCanary for heap analysis.
       //You should not init your app in this process.
      return;
    }
    LeakCanary.install(this);
    //// Normal app init code...

    MultiDex.install(this);

    OcmBuilder ocmBuilder = new OcmBuilder(this).setNotificationActivityClass(MainActivity.class)
        .setOrchextraCredentials("FAKE_KEY", "FAKE_SECRET")
        .setContentLanguage("EN")
        .setOnEventCallback(onEventCallback);

    Ocm.initialize(ocmBuilder);

    OcmStyleUiBuilder ocmStyleUiBuilder = new OcmStyleUiBuilder().setTitleToolbarEnabled(true)
        .setThumbnailEnabled(false)
        .setEnabledStatusBar(true);

    Ocm.setStyleUi(ocmStyleUiBuilder);

    Ocm.setBusinessUnit("it");
  }

  private void enableStrictMode() {
    StrictMode.setThreadPolicy(
        new StrictMode.ThreadPolicy.Builder().detectAll()   // or .detectAll() for all detectable problems
            .penaltyLog().build());
    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().build());
  }
}

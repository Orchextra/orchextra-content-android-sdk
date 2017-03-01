package com.gigigo.sample;

import android.app.Application;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.OcmBuilder;

public class App extends Application {

  @Override public void onCreate() {
    super.onCreate();

    OcmBuilder ocmBuilder = new OcmBuilder(this).setNotificationActivityClass(MainActivity.class).setOrchextraCredentials("","");

    Ocm.initialize(ocmBuilder);
  }
}

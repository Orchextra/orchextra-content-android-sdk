package com.gigigo.orchextra.ocm.sample;

import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import com.facebook.stetho.Stetho;
import com.gigigo.orchextra.ocm.sample.ocm.OcmWrapper;
import com.gigigo.orchextra.ocm.sample.ocm.OcmWrapperImp;
import com.squareup.leakcanary.LeakCanary;
import timber.log.Timber;

public class App extends MultiDexApplication {

  private static final String TAG = "App";

  @Override public void onCreate() {
    //enableStrictMode();
    super.onCreate();
    if (LeakCanary.isInAnalyzerProcess(this)) {
      return;
    }

    Timber.plant(new Timber.DebugTree());
    LeakCanary.install(this);
    MultiDex.install(this);

    Stetho.initializeWithDefaults(this);

    initOcm();
  }

  private void initOcm() {
    OcmWrapperImp.getInstance(this).startWithCredentials(BuildConfig.API_KEY, BuildConfig.API_SECRET,
        BuildConfig.BUSSINES_UNIT, "34e1438469ab50e311a171463c8e4f62", new OcmWrapper.OnStartWithCredentialsCallback() {
          @Override public void onCredentialReceiver(String accessToken) {
            Timber.d("onCredentialReceiver()");
          }

          @Override public void onCredentailError() {
            Timber.e("onCredentailError");
          }
        });
  }

  private void enableStrictMode() {
    StrictMode.setThreadPolicy(
        new StrictMode.ThreadPolicy.Builder().detectAll()   // or .detectAll() for all detectable problems
            .penaltyLog().build());
    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().build());
  }
}
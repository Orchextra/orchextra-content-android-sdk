package com.gigigo.orchextra.ocm.sample;

import android.os.StrictMode;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
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
        BuildConfig.BUSSINES_UNIT, "50163590b4402cceefb2c78a7aba7093",
        new OcmWrapper.OnStartWithCredentialsCallback() {
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
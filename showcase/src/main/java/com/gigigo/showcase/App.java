package com.gigigo.showcase;

import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import com.gigigo.showcase.ocm.ContentManager;

public class App extends MultiDexApplication {

  private ContentManager contentManager;

  @Override public void onCreate() {
    enableStrictMode();
    super.onCreate();

    MultiDex.install(this);

    contentManager = new ContentManager(this);
  }

  public ContentManager getContentManager() {
    return contentManager;
  }

  private void enableStrictMode() {
    StrictMode.setThreadPolicy(
        new StrictMode.ThreadPolicy.Builder().detectAll()   // or .detectAll() for all detectable problems
            .penaltyLog().build());
    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().build());
  }
}
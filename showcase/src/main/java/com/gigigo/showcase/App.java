package com.gigigo.showcase;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import com.gigigo.showcase.ocm.ContentManager;

public class App extends MultiDexApplication {

  private ContentManager contentManager;

  @Override public void onCreate() {
    super.onCreate();

    MultiDex.install(this);

    contentManager = new ContentManager(this);
  }

  public ContentManager getContentManager() {
    return contentManager;
  }
}
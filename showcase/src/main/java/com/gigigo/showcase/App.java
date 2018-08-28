package com.gigigo.showcase;

import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import com.gigigo.showcase.domain.DataManager;
import com.gigigo.showcase.ocm.ContentManager;

public class App extends MultiDexApplication {

  private ContentManager contentManager;
  private DataManager dataManager;

  @Override public void onCreate() {
    super.onCreate();

    MultiDex.install(this);

    contentManager = new ContentManager(this);
    dataManager = new DataManager(PreferenceManager.getDefaultSharedPreferences(this));
  }

  public ContentManager getContentManager() {
    return contentManager;
  }

  public DataManager getDataManager() {
    return dataManager;
  }
}
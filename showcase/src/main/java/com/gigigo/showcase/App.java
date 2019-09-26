package com.gigigo.showcase;

import android.preference.PreferenceManager;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import com.gigigo.showcase.domain.DataManager;
import com.gigigo.showcase.ocm.ContentManager;
import timber.log.Timber;

public class App extends MultiDexApplication {

  private ContentManager contentManager;
  private DataManager dataManager;

  @Override public void onCreate() {
    super.onCreate();

    MultiDex.install(this);
    Timber.plant(new Timber.DebugTree());

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
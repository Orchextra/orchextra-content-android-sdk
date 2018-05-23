package com.gigigo.orchextra.core.sdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class OcmPreferencesImp implements OcmPreferences {

  private static final String OCM_PREFERENCES = "OCM_PREFERENCES";
  private final Context context;

  private static final String OCM_VERSION_CODE = "OCM_VERSION_CODE";

  public OcmPreferencesImp(Context context) {
    this.context = context;
  }

  private SharedPreferences getPreferences() {
    return context.getSharedPreferences(OCM_PREFERENCES, Context.MODE_PRIVATE);
  }

  @Override public void saveVersion(String version) {
    SharedPreferences preferences = getPreferences();
    SharedPreferences.Editor editor = preferences.edit();
    editor.putString(OCM_VERSION_CODE, version);
    editor.apply();
  }

  @Override public String getVersion() {
    SharedPreferences preferences = getPreferences();
    return preferences.getString(OCM_VERSION_CODE, null);
  }
}

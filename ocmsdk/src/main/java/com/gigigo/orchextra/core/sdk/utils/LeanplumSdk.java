package com.gigigo.orchextra.core.sdk.utils;

import android.app.Application;
import android.util.Log;
import com.leanplum.Leanplum;
import com.leanplum.LeanplumActivityHelper;
import com.leanplum.Var;
import com.leanplum.annotations.Parser;
import com.leanplum.callbacks.VariablesChangedCallback;

public class LeanplumSdk {

  private Var<String> typeItem = Var.define("typeItem", "image.png!");
  private boolean isUpdatedData = false;

  public LeanplumSdk(Application app) {
    Leanplum.setApplicationContext(app);
    Parser.parseVariablesForClasses(this.getClass());

    //  For session lifecyle tracking.
    LeanplumActivityHelper.enableLifecycleCallbacks(app);

    //Leanplum.setAppIdForDevelopmentMode("app_obDbH7vraMYn5f6KLQMixhXKP6r7Sc3Z5md2j9enjwA", "dev_z8tM4sr2k9CPoAd21HupyA6BG1EpzmfiCZ0jOeUAOEA");
    Leanplum.setAppIdForProductionMode("app_obDbH7vraMYn5f6KLQMixhXKP6r7Sc3Z5md2j9enjwA",
        "prod_RBgeBaF74qEoFsTczh4ckG45weFO7ZzzUnINxDJjwTs");

    //Leanplum.enableVerboseLoggingInDevelopmentMode();

    Leanplum.addVariablesChangedHandler(new VariablesChangedCallback() {
      @Override public void variablesChanged() {
        isUpdatedData = true;
        Log.i("Leanplum Test", String.valueOf(typeItem.value()));
      }
    });

    Leanplum.start(app);
  }

  public Var<String> getTypeItem() {
    return typeItem;
  }

  public boolean isUpdatedDataFromServer() {
    return isUpdatedData;
  }

  public void track(String event) {
    Leanplum.track(event);
  }

  public void track(String event, String action) {
    Leanplum.track(event, action);
  }
}

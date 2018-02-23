package com.gigigo.orchextra.ocm.sample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import com.gigigo.ggglogger.GGGLogImpl;
import com.gigigo.orchextra.core.controller.model.home.ImageTransformReadArticle;
import com.gigigo.orchextra.core.sdk.model.detail.DetailActivity;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.OcmBuilder;
import com.gigigo.orchextra.ocm.OcmEvent;
import com.gigigo.orchextra.ocm.OcmStyleUiBuilder;
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver;
import com.gigigo.orchextra.ocm.callbacks.OnEventCallback;
import com.squareup.leakcanary.LeakCanary;
import java.util.ArrayList;
import java.util.Arrays;

public class App extends MultiDexApplication {

  private Context context;
  private static final String OCM_DEEPLINK_START_PATH = "ocm";
  private static final String OCM_DEEPLINK_ELEMENT_PATH = "element";
  private static final String SLUG_DEEPLINK_START_PATH = "slug=";
  private static final String URLSCHEME = "urlscheme";

  private OnEventCallback onEventCallback = new OnEventCallback() {
    @Override public void doEvent(OcmEvent event, Object data) {
    }

    @Override public void doEvent(OcmEvent event) {
    }
  };

  private OnCustomSchemeReceiver onCustomSchemeReceiver = new OnCustomSchemeReceiver() {
    @Override public void onReceive(String scheme) {
      try {
        Intent i = new Intent();
        i.setAction("android.intent.action.VIEW");

        Uri deeplink = Uri.parse(scheme);
        i.setData(deeplink);

        getDeepLinking(i);
      } catch (Exception ignored) {
      }
    }
  };

  private void getDeepLinking(Intent intent) {
    Uri data = intent.getData();

    if (data != null) {
      processDeepLinks(data);
    } else if (intent.getExtras() != null) {
      String path = intent.getExtras().getString(URLSCHEME);
      if (path != null) {
        Uri decode = Uri.parse(path);
        processDeepLinks(decode);
      }
    }
  }

  private void processDeepLinks(Uri data) {
    if (data != null) {
      String host = data.getHost();
      String path = data.getPath();
      String query = data.getQuery();

      System.out.println("DeepLink Host: " + host);
      System.out.println("DeepLink Path: " + path);
      System.out.println("DeepLink Query: " + query);

      if (query != null && !query.isEmpty()) {
        int indexOfSlug = query.indexOf(SLUG_DEEPLINK_START_PATH);
        if (indexOfSlug > -1) {
          query = query.substring(indexOfSlug + SLUG_DEEPLINK_START_PATH.length());
        }
        processDeepLinks(host, query);
      } else {
        processDeepLinks(host, path);
      }
    }
  }

  private void processDeepLinks(String host, String path) {
    if (path != null && !path.isEmpty()) {

      if (path.startsWith("/")) {
        path = path.substring(1, path.length());
      }

      if (path.toUpperCase().startsWith(OCM_DEEPLINK_START_PATH.toUpperCase())
          || path.toUpperCase().startsWith(OCM_DEEPLINK_ELEMENT_PATH.toUpperCase())) {

        path = path.substring(path.indexOf("/") + 1, path.length());

        Ocm.processDeepLinks(path);
      }
      else {
        System.out.println("navigateToSection " + path);
      }
    }
  }

  @Override public void onCreate() {
    context = this;

    enableStrictMode();
    super.onCreate();
    if (LeakCanary.isInAnalyzerProcess(this)) {
      //This process is dedicated to LeakCanary for heap analysis.
      //You should not init your app in this process.
      return;
    }
    LeakCanary.install(this);
    MultiDex.install(this);

    OcmBuilder ocmBuilder = new OcmBuilder(this)
        .setNotificationActivityClass(new ArrayList<>(
        Arrays.asList(MainActivity.class.toString(), DetailActivity.class.toString())))
        .setShowReadArticles(true)
        //.setVuforiaImpl(new ImageRecognitionVuforiaImpl())
        .setTransformReadArticleMode(ImageTransformReadArticle.BITMAP_TRANSFORM)
        // .setVuforiaImpl(new ImageRecognitionVuforiaImpl())
        .setMaxReadArticles(100)
        .setOrchextraCredentials(BuildConfig.API_KEY, BuildConfig.API_SECRET)
        .setContentLanguage("EN")
        .setOnEventCallback(onEventCallback);

    Ocm.initialize(ocmBuilder);

    OcmStyleUiBuilder ocmStyleUiBuilder =
        new OcmStyleUiBuilder().setTitleFont("fonts/Gotham-Ultra.ttf")
            .setNormalFont("fonts/Gotham-Book.ttf")
            .setMediumFont("fonts/Gotham-Medium.ttf")
            .setTitleToolbarEnabled(false)
            //.disableThumbnailImages()
            .setEnabledStatusBar(false);

    Ocm.setStyleUi(ocmStyleUiBuilder);

    Ocm.setBusinessUnit(BuildConfig.BUSSINES_UNIT);

    Ocm.setOnCustomSchemeReceiver(onCustomSchemeReceiver);
  }

  private void enableStrictMode() {
    StrictMode.setThreadPolicy(
        new StrictMode.ThreadPolicy.Builder().detectAll()   // or .detectAll() for all detectable problems
            .penaltyLog().build());
    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().build());
  }
}

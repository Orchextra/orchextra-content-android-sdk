package com.gigigo.orchextra.core.sdk.actions;

import android.content.Intent;
import android.net.Uri;
import com.gigigo.orchextra.Orchextra;
import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube.YoutubeContentDataActivity;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.ocm.OCManager;

public class ActionHandler {

  private final OcmContextProvider ocmContextProvider;

  public ActionHandler(OcmContextProvider ocmContextProvider) {
    this.ocmContextProvider = ocmContextProvider;
  }

  public void processDeepLink(String uri) {
    OCManager.returnOcCustomSchemeCallback(uri);
  }

  public void launchExternalYoutube(String url) {
    YoutubeContentDataActivity.open(ocmContextProvider.getCurrentActivity(), url);
  }

  public void launchOxVuforia() {
    Orchextra.startImageRecognition();
  }

  public void lauchOxScan() {
    Orchextra.startScannerActivity();
  }

  public void launchExternalBrowser(String url) {
    Intent i = new Intent(Intent.ACTION_VIEW);
    i.setData(Uri.parse(url));
    ocmContextProvider.getCurrentActivity().startActivity(i);
  }

  public void launchCustomTabs(String url) {
    DeviceUtils.openChromeTabs(ocmContextProvider.getCurrentActivity(), url);
  }
}

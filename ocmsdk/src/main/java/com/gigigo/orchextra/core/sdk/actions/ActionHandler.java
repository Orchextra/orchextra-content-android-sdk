package com.gigigo.orchextra.core.sdk.actions;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.gigigo.orchextra.Orchextra;
import com.gigigo.orchextra.core.data.api.utils.ConnectionUtilsImp;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube.YoutubeContentDataActivity;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocmsdk.R;

public class ActionHandler {

  private final OcmContextProvider ocmContextProvider;
  private final ConnectionUtils connectionUtils;

  public ActionHandler(OcmContextProvider ocmContextProvider) {
    this.ocmContextProvider = ocmContextProvider;
    this.connectionUtils = new ConnectionUtilsImp(ocmContextProvider.getApplicationContext());
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
    if (connectionUtils.hasConnection()) {
      DeviceUtils.openChromeTabs(ocmContextProvider.getCurrentActivity(), url);
    } else {
      View rootView = ((ViewGroup) ocmContextProvider.getCurrentActivity()
          .findViewById(android.R.id.content)).getChildAt(0);
      Snackbar.make(rootView, R.string.oc_error_content_not_available_without_internet, Toast.LENGTH_SHORT).show();
    }
  }
}

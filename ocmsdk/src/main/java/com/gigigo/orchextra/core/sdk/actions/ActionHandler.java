package com.gigigo.orchextra.core.sdk.actions;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.gigigo.orchextra.Orchextra;
import com.gigigo.orchextra.core.data.api.utils.ConnectionUtilsImp;
import com.gigigo.orchextra.core.domain.entities.elementcache.FederatedAuthorization;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube.YoutubeContentDataActivity;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocmsdk.R;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

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

  public void launchExternalBrowser(final String url, FederatedAuthorization federatedAuth) {
    Activity currentActivity = ocmContextProvider.getCurrentActivity();
    if (currentActivity != null) {
      Intent intent = new Intent(Intent.ACTION_VIEW);

      if (federatedAuth != null) {
        if (federatedAuth != null
            && federatedAuth.isActive()
            && Ocm.getQueryStringGenerator() != null) {

          Ocm.getQueryStringGenerator().createQueryString(federatedAuth, queryString -> {
            if (queryString != null && !queryString.isEmpty()) {
              String urlWithQueryParams = addQueryParamsToUrl(queryString, url);
              System.out.println(
                  "Main ContentWebViewGridLayout federatedAuth url: " + urlWithQueryParams);
              if (urlWithQueryParams != null) {
                intent.setData(Uri.parse(urlWithQueryParams));
                currentActivity.startActivity(intent);
              } else {
                intent.setData(Uri.parse(url));
                currentActivity.startActivity(intent);
              }
            }
          });
        } else {
          intent.setData(Uri.parse(url));
          currentActivity.startActivity(intent);
        }
      } else {
        intent.setData(Uri.parse(url));
        currentActivity.startActivity(intent);
      }
      //currentActivity.startActivity(intent);
    }
    else
    {
      //todo falta que si no hay currentactivity lo lanze en webview
    }
  }

  //region external browser FA
  private static final String URL_START_QUERY_DELIMITER = "?";
  private static final String URL_CONCAT_QUERY_DELIMITER = "&";
  private static final String URL_QUERY_VALUE_DELIMITER = "=";

  private String getQueryDelimiter(String url) {
    try {
      return new URL(url).getQuery() != null ? URL_CONCAT_QUERY_DELIMITER
          : URL_START_QUERY_DELIMITER;
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    return null;
  }

  private String addQueryParamsToUrl(List<Pair<String, String>> queryParams, String url) {
    if (getQueryDelimiter(url) != null) {
      url = url + getQueryDelimiter(url);

      Iterator<Pair<String, String>> iterator = queryParams.iterator();
      while (iterator.hasNext()) {
        Pair<String, String> pair = iterator.next();
        url =
            url + pair.first + URL_QUERY_VALUE_DELIMITER + pair.second + URL_CONCAT_QUERY_DELIMITER;
      }

      return url.substring(0, url.length() - 2);
    } else {
      return null;
    }
  }
  //endregion

  public void launchCustomTabs(String url, FederatedAuthorization federatedAuthorization) {
    if (connectionUtils.hasConnection()) {
      DeviceUtils.openChromeTabs(ocmContextProvider.getCurrentActivity(), url, federatedAuthorization);
    } else {
      View rootView = ((ViewGroup) ocmContextProvider.getCurrentActivity()
          .findViewById(android.R.id.content)).getChildAt(0);
      Snackbar.make(rootView, R.string.oc_error_content_not_available_without_internet,
          Toast.LENGTH_SHORT).show();
    }
  }
}

package com.gigigo.orchextra.core.sdk.actions;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.gigigo.orchextra.core.data.api.utils.ConnectionUtilsImp;
import com.gigigo.orchextra.core.domain.entities.elementcache.FederatedAuthorization;
import com.gigigo.orchextra.core.domain.rxInteractor.GetVideo;
import com.gigigo.orchextra.core.domain.rxInteractor.PriorityScheduler;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleVimeoVideoView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube.YoutubeContentDataActivity;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.federatedAuth.FAUtils;
import com.gigigo.orchextra.ocmsdk.BuildConfig;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.orchextra.wrapper.OxManager;
import com.gigigo.orchextra.wrapper.OxManagerImpl;
import gigigo.com.vimeolibs.VideoObserver;
import gigigo.com.vimeolibs.VimeoBuilder;
import gigigo.com.vimeolibs.VimeoCallback;
import gigigo.com.vimeolibs.VimeoExoPlayerActivity;
import gigigo.com.vimeolibs.VimeoInfo;
import gigigo.com.vimeolibs.VimeoManager;

public class ActionHandler {

  OxManager orchextra;
  private final OcmContextProvider ocmContextProvider;
  private final ConnectionUtils connectionUtils;
  private GetVideo getVideo;

  public ActionHandler(OcmContextProvider ocmContextProvider, GetVideo getVideo) {
    this.ocmContextProvider = ocmContextProvider;
    this.connectionUtils = new ConnectionUtilsImp(ocmContextProvider.getApplicationContext());
    this.orchextra = new OxManagerImpl();
    this.getVideo = getVideo;
  }

  public void processDeepLink(String uri) {
    OCManager.returnOnCustomSchemeCallback(uri);
  }

  public void launchYoutubePlayer(String videoId) {
    YoutubeContentDataActivity.open(ocmContextProvider.getApplicationContext(), videoId);
  }

  public void launchVimeoPlayer(String videoId) {
    if (videoId != null && !videoId.equals("")) {
      //show loading
      VimeoExoPlayerActivity.open(ocmContextProvider.getCurrentActivity(), null);

      //more 4 dagger
      ConnectionUtilsImp conn = new ConnectionUtilsImp(ocmContextProvider.getCurrentActivity());
      //get vimeo data from sdk vimeo

      getVideo.execute(new VideoObserver(new VimeoCallback() {

        @Override public void onSuccess(VimeoInfo vimeoInfo) {
          VimeoExoPlayerActivity.open(ocmContextProvider.getCurrentActivity(), vimeoInfo);
        }

        @Override public void onError(Exception e) {
          System.out.println("Error VimeoCallback" + e.toString());
        }
      }), GetVideo.Params.Companion.forVideo(ocmContextProvider.getCurrentActivity(), false, videoId, connectionUtils.isConnectedWifi(), connectionUtils.isConnectedMobile()), PriorityScheduler.Priority.HIGH);
    }
  }

  public void launchOxVuforia() {
    orchextra.startImageRecognition();
  }

  public void lauchOxScan() {
    orchextra.startScanner();
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
              String urlWithQueryParams = FAUtils.addQueryParamsToUrl(queryString, url);
              System.out.println(
                  "Main ContentWebViewGridLayout federatedAuth url: " + urlWithQueryParams);
              if (urlWithQueryParams != null) {
                intent.setData(Uri.parse(urlWithQueryParams));
                currentActivity.startActivity(intent);
              } else {
                intent.setData(Uri.parse(url));
                currentActivity.startActivity(intent);
              }
            } else {
              intent.setData(Uri.parse(url));
              currentActivity.startActivity(intent);
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
    } else {
      //todo falta que si no hay currentactivity lo lanze en webview
    }
  }

  public void launchCustomTabs(String url, FederatedAuthorization federatedAuthorization) {
    if (connectionUtils.hasConnection()) {
      DeviceUtils.openChromeTabs(ocmContextProvider.getCurrentActivity(), url,
          federatedAuthorization);
    } else {
      View rootView = ((ViewGroup) ocmContextProvider.getCurrentActivity()
          .findViewById(android.R.id.content)).getChildAt(0);
      Snackbar.make(rootView, R.string.oc_error_content_not_available_without_internet,
          Toast.LENGTH_SHORT).show();
    }
  }
}

package com.gigigo.orchextra.core.sdk.actions;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import com.google.android.material.snackbar.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.orchextra.core.data.api.utils.ConnectionUtilsImp;
import com.gigigo.orchextra.core.domain.entities.elementcache.FederatedAuthorization;
import com.gigigo.orchextra.core.domain.rxInteractor.GetVideo;
import com.gigigo.orchextra.core.domain.rxInteractor.PriorityScheduler;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube.YoutubeContentDataActivity;
import com.gigigo.orchextra.core.sdk.ui.OcmWebViewActivity;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.federatedAuth.FAUtils;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.orchextra.wrapper.OxManager;
import com.gigigo.orchextra.wrapper.OxManagerImpl;
import gigigo.com.vimeolibs.VideoObserver;
import gigigo.com.vimeolibs.VimeoCallback;
import gigigo.com.vimeolibs.VimeoExoPlayerActivity;
import gigigo.com.vimeolibs.VimeoInfo;

public class ActionHandler {

  private static final String TAG = "ActionHandler";
  private final OxManager oxManager;
  private final OcmContextProvider ocmContextProvider;
  private final ConnectionUtils connectionUtils;
  private GetVideo getVideo;

  public ActionHandler(OcmContextProvider ocmContextProvider, GetVideo getVideo) {
    this.ocmContextProvider = ocmContextProvider;
    this.connectionUtils = new ConnectionUtilsImp(ocmContextProvider.getApplicationContext());
    this.oxManager = new OxManagerImpl();
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

      getVideo.execute(new VideoObserver(new VimeoCallback() {
            @Override public void onSuccess(VimeoInfo vimeoInfo) {
              VimeoExoPlayerActivity.openVideo(ocmContextProvider.getCurrentActivity(), vimeoInfo);
            }

            @Override public void onError(Throwable e) {
              Log.e(TAG, "getVideo()", e);
            }
          }), GetVideo.Params.Companion.forVideo(ocmContextProvider.getCurrentActivity(), false,
          videoId, connectionUtils.isConnectedWifi(), connectionUtils.isConnectedMobile()),
          PriorityScheduler.Priority.HIGH);
    }
  }

  public void getVimeoInfo(String videoId, VideoObserver videoObserver) {
    if (videoId != null && !videoId.equals("")) {
      getVideo.execute(new VideoObserver(new VimeoCallback() {
            @Override public void onSuccess(VimeoInfo vimeoInfo) {
              videoObserver.onNext(vimeoInfo);
            }

            @Override public void onError(Throwable e) {
              videoObserver.onError(e);
            }
          }), GetVideo.Params.Companion.forVideo(ocmContextProvider.getCurrentActivity(), false,
          videoId, connectionUtils.isConnectedWifi(), connectionUtils.isConnectedMobile()),
          PriorityScheduler.Priority.HIGH);
    }
  }

  public void launchOxVuforia() {
    oxManager.startImageRecognition();
  }

  public void lauchOxScan() {
    oxManager.startScanner();
  }

  public void scanCode(OxManager.ScanCodeListener scanCodeListener) {
    oxManager.scanCode(scanCodeListener);
  }

  public void launchExternalBrowser(final String url, FederatedAuthorization federatedAuth) {
    Activity currentActivity = ocmContextProvider.getCurrentActivity();
    if (currentActivity != null) {
      Intent intent = new Intent(Intent.ACTION_VIEW);

      try {
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
      } catch (Exception e) {
        OcmWebViewActivity.open(currentActivity, url, "");
      }
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

      OCManager.getCustomTranslation(R.string.oc_error_content_not_available_without_internet,
          text -> {

            if (text != null) {
              Snackbar.make(rootView, text, Snackbar.LENGTH_SHORT).show();
            } else {
              Snackbar.make(rootView, R.string.oc_error_content_not_available_without_internet,
                  Snackbar.LENGTH_SHORT).show();
            }
            return null;
          });
    }
  }
}

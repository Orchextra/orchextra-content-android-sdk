package com.gigigo.orchextra.core.sdk;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import com.gigigo.orchextra.core.data.rxException.NetworkConnectionException;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheRender;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheType;
import com.gigigo.orchextra.core.domain.entities.elementcache.FederatedAuthorization;
import com.gigigo.orchextra.core.domain.entities.elementcache.VideoFormat;
import com.gigigo.orchextra.core.domain.entities.elements.ElementSegmentation;
import com.gigigo.orchextra.core.domain.entities.menus.RequiredAuthoritation;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.core.sdk.actions.ActionHandler;
import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import com.gigigo.orchextra.core.sdk.model.detail.DetailActivity;
import com.gigigo.orchextra.core.sdk.ui.OcmWebViewActivity;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.core.sdk.utils.swipeback.UrlUtilsKt;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.OcmEvent;
import com.gigigo.orchextra.ocm.callbacks.CustomUrlCallback;
import com.gigigo.orchextra.wrapper.OxManager;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OcmSchemeHandler {

  private static final String TAG = "OcmSchemeHandler";
  private final OcmContextProvider contextProvider;
  private final OcmController ocmController;
  private final ActionHandler actionHandler;
  private final Authoritation authoritation;
  private String elementURL;
  private String processElementURL;
  private Map<String, String> customParams = new HashMap<>();

  public OcmSchemeHandler(OcmContextProvider contextProvider, OcmController ocmController,
      ActionHandler actionHandler, Authoritation authoritation) {
    this.contextProvider = contextProvider;
    this.ocmController = ocmController;
    this.actionHandler = actionHandler;
    this.authoritation = authoritation;
  }

  public void processRedirectElementUrl(final String elementUrl) {
    ocmController.getDetails(elementUrl, new OcmController.GetDetailControllerCallback() {
      @Override public void onGetDetailLoaded(ElementCache elementCache) {
        if (elementCache != null) {
          if (elementCache.getCustomProperties() != null) {
            OCManager.notifyCustomBehaviourContinue(elementCache.getCustomProperties(), null,
                canContinue -> {
                  if (canContinue) {
                    executeAction(elementCache, elementUrl, null, null);
                  }
                  return null;
                });
          } else {
            executeAction(elementCache, elementUrl, null, null);
          }
        }
      }

      @Override public void onGetDetailFails(Exception e) {
        e.printStackTrace();
      }

      @Override public void onGetDetailNoAvailable(Exception e) {
        e.printStackTrace();
      }
    });
  }

  public void processElementUrl(final String elementUrl) {
    String elementUri = elementUrl;
    if (elementURL != null) {
      elementUri = elementURL;
      elementURL = null;
    }

    String finalElementUri = elementUri;
    ocmController.getDetails(elementUri, new OcmController.GetDetailControllerCallback() {
      @Override public void onGetDetailLoaded(ElementCache elementCache) {
        if (elementCache != null) {
          if (elementRequiredUserToBeLogged(elementCache)) {
            // Save url of the element that require login
            elementURL = elementUrl;
            //OCManager.notifyRequiredLoginToContinue(elementURL);
          } else {
            executeAction(elementCache, finalElementUri, null, null);
          }
        }
      }

      @Override public void onGetDetailFails(Exception e) {
        e.printStackTrace();
      }

      @Override public void onGetDetailNoAvailable(Exception e) {
        e.printStackTrace();
      }
    });
  }

  public void processElementUrl(String elementUrl, ImageView imageViewToExpandInDetail,
      ProcessElementCallback processElementCallback) {

    WeakReference<ImageView> imageViewWeakReference =
        new WeakReference<>(imageViewToExpandInDetail);

    String elementUri = elementUrl;
    if (processElementURL != null) {
      elementUri = processElementURL;
      processElementURL = null;
    }

    String finalElementUri = elementUri;
    ocmController.getDetails(elementUri, new OcmController.GetDetailControllerCallback() {

      @Override public void onGetDetailLoaded(ElementCache elementCache) {
        if (elementCache != null) {
          processElementCallback.onProcessElementSuccess(elementCache);

          String urlImageToExpand = null;
          if (elementCache != null && elementCache.getPreview() != null) {
            urlImageToExpand = elementCache.getPreview().getImageUrl();
          }

          if (elementRequiredUserToBeLogged(elementCache)) {
            // Save url of the element that require login
            processElementURL = elementUrl;
            //OCManager.notifyRequiredLoginToContinue(processElementURL);
          } else {
            executeAction(elementCache, finalElementUri, urlImageToExpand, imageViewWeakReference);
          }
        }
      }

      @Override public void onGetDetailFails(Exception e) {
        processElementCallback.onProcessElementFail(e);
      }

      @Override public void onGetDetailNoAvailable(Exception e) {
        processElementCallback.onProcessElementFail(new NetworkConnectionException());
      }
    });
  }

  /*
  public void processElementUrl(String elementUrl, String urlImageToExpand, int widthScreen,
      int heightScreen, ImageView imageViewToExpandInDetail) {

    WeakReference<ImageView> imageViewWeakReference =
        new WeakReference<>(imageViewToExpandInDetail);

    String elementUri = elementUrl;
    if (processElementURL != null) {
      elementUri = processElementURL;
      processElementURL = null;
    }

    String finalElementUri = elementUri;
    ocmController.getDetails(elementUri, new OcmController.GetDetailControllerCallback() {
      @Override public void onGetDetailLoaded(ElementCache elementCache) {
        if (elementCache != null) {
          if (elementRequiredUserToBeLogged(elementCache)) {
            // Save url of the element that require login
            processElementURL = elementUrl;
            OCManager.notifyRequiredLoginToContinue(processElementURL);
          } else {
            executeAction(elementCache, finalElementUri, urlImageToExpand, widthScreen,
                heightScreen, imageViewWeakReference);
          }
        }
      }

      @Override public void onGetDetailFails(Exception e) {
        e.printStackTrace();
      }

      @Override public void onGetDetailNoAvailable(Exception e) {
        e.printStackTrace();
      }
    });
  }
  */

  private boolean elementRequiredUserToBeLogged(ElementCache elementCache) {
    ElementSegmentation segmentation = elementCache.getSegmentation();

    boolean loggedRequired = false;
    if (segmentation != null) {
      loggedRequired = RequiredAuthoritation.LOGGED.equals(segmentation.getRequiredAuth());
    }

    return loggedRequired && !authoritation.isAuthorizatedUser();
  }

  public void executeAction(ElementCache cachedElement, String elementUrl, String urlImageToExpand,
      WeakReference<ImageView> imageViewToExpandInDetail) {

    boolean hasPreview = cachedElement.getPreview() != null;

    if (hasPreview) {
      processDetailActivity(elementUrl, urlImageToExpand, imageViewToExpandInDetail);
      return;
    }

    ElementCacheType type = cachedElement.getType();
    ElementCacheRender render = cachedElement.getRender();

    switch (type) {
      case VUFORIA:
        OCManager.notifyEvent(OcmEvent.OPEN_IR, cachedElement);
        if (render != null) {
          processImageRecognitionAction();
        }
        break;
      case SCAN:
        OCManager.notifyEvent(OcmEvent.OPEN_BARCODE, cachedElement);
        if (render != null) {
          processScanAction();
        }
        break;
      case WEBVIEW:
        OCManager.notifyEvent(OcmEvent.VISIT_URL, cachedElement);
        if (render != null) {
          render.setUrl(processUrl(render.getUrl()));
          OcmWebViewActivity.open(contextProvider.getCurrentActivity(), render, "");
        }
        break;

      case BROWSER:
        OCManager.notifyEvent(OcmEvent.VISIT_URL, cachedElement);
        if (render != null) {
          processCustomTabs(processUrl(render.getUrl()), render.getFederatedAuth());
        }
        break;
      case EXTERNAL_BROWSER:
        OCManager.notifyEvent(OcmEvent.VISIT_URL, cachedElement);
        if (render != null) {
          processExternalBrowser(processUrl(render.getUrl()), render.getFederatedAuth());
        }
        break;
      case DEEP_LINK:
        OCManager.notifyEvent(OcmEvent.VISIT_URL, cachedElement);
        if (render != null) {
          processDeepLink(render.getSchemeUri());
        }
        break;
      case VIDEO:
        if (render != null) {
          processVideo(render.getFormat(), render.getSource(), cachedElement);
        }
        break;
      default:
        Log.w(TAG, "Unknow type: " + type);
        processDetailActivity(elementUrl, urlImageToExpand, imageViewToExpandInDetail);
        break;
    }
  }

  private void processDetailActivity(String elementUrl, String urlImageToExpand,
      WeakReference<ImageView> imageViewToExpandInDetail) {
    int widthScreen =
        DeviceUtils.calculateRealWidthDeviceInImmersiveMode(contextProvider.getCurrentActivity());
    int heightScreen =
        DeviceUtils.calculateHeightDeviceInImmersiveMode(contextProvider.getCurrentActivity());

    ImageView imageView = null;
    if (imageViewToExpandInDetail != null) {
      imageView = imageViewToExpandInDetail.get();
    }
    openDetailActivity(elementUrl, urlImageToExpand, widthScreen, heightScreen, imageView);
  }

  private void processVideo(VideoFormat format, String source, ElementCache cachedElement) {
    if (TextUtils.isEmpty(source) || format == VideoFormat.NONE) {
      return;
    } else if (format == VideoFormat.YOUTUBE) {
      OCManager.notifyEvent(OcmEvent.PLAY_YOUTUBE, cachedElement);
      actionHandler.launchYoutubePlayer(source);
    } else if (format == VideoFormat.VIMEO) {
      OCManager.notifyEvent(OcmEvent.PLAY_VIMEO, cachedElement);
      actionHandler.launchVimeoPlayer(source);
    }
  }

  private void processCustomTabs(String url, FederatedAuthorization federatedAuthorization) {
    DeviceUtils.openChromeTabs(contextProvider.getCurrentActivity(), url, federatedAuthorization);
  }

  private void processImageRecognitionAction() {
    actionHandler.launchOxVuforia();
  }

  private void processScanAction() {
    actionHandler.lauchOxScan();
  }

  private void processScanCode(OxManager.ScanCodeListener scanCodeListener) {
    actionHandler.scanCode(scanCodeListener);
  }

  private void processExternalBrowser(String url, FederatedAuthorization federatedAuth) {
    actionHandler.launchExternalBrowser(url, federatedAuth);
  }

  private void processDeepLink(String uri) {
    Log.d(TAG, "processDeepLink: " + uri);

    if (uri.contains("openScanner")) {
      processScanCode(code -> {
        customParams.put("#code#", code);

        String action = uri.replaceAll("^[a-z]*://openScanner/", "");
        Log.d(TAG, "Code: " + code + " Action: " + action);
        processElementUrl(action);
      });
    } else {
      actionHandler.processDeepLink(uri);
    }
  }

  private void openDetailActivity(String elementUrl, String urlImageToExpand, int widthScreen,
      int heightScreen, ImageView imageViewToExpandInDetail) {
    DetailActivity.open(contextProvider.getCurrentActivity(), elementUrl, urlImageToExpand,
        widthScreen, heightScreen, imageViewToExpandInDetail);
  }

  @NonNull private String processUrl(@NonNull String url) {

    List<String> params = UrlUtilsKt.getUrlParameters(url);

    if (params.isEmpty()) {
      return url;
    } else {
      String newUrl = url;

      for (String param : params) {
        if (customParams.containsKey(param)) {
          newUrl = newUrl.replace(param, customParams.get(param));
        }
      }

      CustomUrlCallback customUrlCallback = OCManager.getCustomUrlCallback();
      if (customUrlCallback != null) {
        Map<String, String> map = customUrlCallback.actionNeedsValues(params);
        for (String param : params) {
          if (map.containsKey(param)) {
            newUrl = newUrl.replace(param, map.get(param));
          }
        }
      } else {
        Log.e(TAG, "customUrlCallback is null");
      }
      return newUrl;
    }
  }

  public interface ProcessElementCallback {
    void onProcessElementSuccess(ElementCache elementCache);

    void onProcessElementFail(Exception exception);
  }
}
package com.gigigo.orchextra.core.sdk;

import android.widget.ImageView;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheRender;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheType;
import com.gigigo.orchextra.core.sdk.actions.ActionHandler;
import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import com.gigigo.orchextra.core.sdk.model.detail.DetailActivity;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import java.lang.ref.WeakReference;

public class OcmSchemeHandler {

  private final OcmContextProvider contextProvider;
  private final OcmController ocmController;
  private final ActionHandler actionHandler;

  public OcmSchemeHandler(OcmContextProvider contextProvider, OcmController ocmController,
      ActionHandler actionHandler) {
    this.contextProvider = contextProvider;
    this.ocmController = ocmController;
    this.actionHandler = actionHandler;
  }

  public void processElementUrl(final String elementUrl) {
    ocmController.getDetails(false, elementUrl, new OcmController.GetDetailControllerCallback() {
      @Override public void onGetDetailLoaded(ElementCache elementCache) {
        if (elementCache != null) {
          executeAction(elementCache, elementUrl, null, 0, 0, null);
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

  public void processElementUrl(String elementUrl, String urlImageToExpand, int widthScreen,
      int heightScreen, ImageView imageViewToExpandInDetail) {

    WeakReference<ImageView> imageViewWeakReference =
        new WeakReference<>(imageViewToExpandInDetail);

    ocmController.getDetails(false, elementUrl, new OcmController.GetDetailControllerCallback() {
      @Override public void onGetDetailLoaded(ElementCache elementCache) {
        if (elementCache != null) {
          executeAction(elementCache, elementUrl, urlImageToExpand, widthScreen, heightScreen,
              imageViewWeakReference);
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

  private void executeAction(ElementCache cachedElement, String elementUrl, String urlImageToExpand,
      int widthScreen, int heightScreen, WeakReference<ImageView> imageViewToExpandInDetail) {

    ImageView imageView = imageViewToExpandInDetail.get();

    ElementCacheType type = cachedElement.getType();
    ElementCacheRender render = cachedElement.getRender();

    switch (type) {
      case VUFORIA:
        if (render != null) {
          processImageRecognitionAction();
        }
        break;
      case SCAN:
        if (render != null) {
          processScanAction();
        }
        break;
      case BROWSER:
        if (render != null) {
          processCustomTabs(render.getUrl());
        }
        break;
      case EXTERNAL_BROWSER:
        if (render != null) {
          processExternalBrowser(render.getUrl());
        }
        break;
      case DEEP_LINK:
        if (render != null) {
          processDeepLink(render.getUri());
        }
        break;
      default:
        openDetailActivity(elementUrl, urlImageToExpand, widthScreen, heightScreen,
            imageView);
        break;
    }
  }

  private void processCustomTabs(String url) {
    DeviceUtils.openChromeTabs(contextProvider.getCurrentActivity(), url);
  }

  private void processImageRecognitionAction() {
    actionHandler.launchOxVuforia();
  }

  private void processScanAction() {
    actionHandler.lauchOxScan();
  }

  private void processExternalBrowser(String url) {
    actionHandler.launchExternalBrowser(url);
  }

  private void processDeepLink(String uri) {
    actionHandler.processDeepLink(uri);
  }

  private void openDetailActivity(String elementUrl, String urlImageToExpand, int widthScreen,
      int heightScreen, ImageView imageViewToExpandInDetail) {
    DetailActivity.open(contextProvider.getCurrentActivity(), elementUrl, urlImageToExpand,
        widthScreen, heightScreen, imageViewToExpandInDetail);
  }
}

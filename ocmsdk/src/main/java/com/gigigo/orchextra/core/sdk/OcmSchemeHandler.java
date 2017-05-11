package com.gigigo.orchextra.core.sdk;

import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheRender;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheType;
import com.gigigo.orchextra.core.sdk.actions.ActionHandler;
import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import com.gigigo.orchextra.core.sdk.model.detail.DetailActivity;

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

  public void processScheme(String path) {

    ElementCache cachedElement = ocmController.getCachedElement(path);

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
        openDetailActivity(path);
        break;
    }
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

  private void openDetailActivity(String path) {
    DetailActivity.open(contextProvider.getCurrentActivity(), path, null, 0, 0, null);
  }
}

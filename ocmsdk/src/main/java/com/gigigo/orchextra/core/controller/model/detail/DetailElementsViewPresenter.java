package com.gigigo.orchextra.core.controller.model.detail;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.gigigo.interactorexecutor.base.Presenter;
import com.gigigo.interactorexecutor.base.viewinjector.GenericViewInjector;
import com.gigigo.orchextra.core.controller.OcmViewGenerator;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheBehaviour;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCachePreview;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheRender;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheType;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheShare;

public class DetailElementsViewPresenter extends Presenter<DetailElementsView> {

  private final OcmController ocmController;
  private final OcmViewGenerator ocmViewGenerator;

  private String elementUrl;

  public DetailElementsViewPresenter(GenericViewInjector viewInjector, OcmController ocmController,
      OcmViewGenerator ocmViewGenerator) {
    super(viewInjector);

    this.ocmController = ocmController;
    this.ocmViewGenerator = ocmViewGenerator;
  }

  @Override public void onViewAttached() {
    getView().initUi();
  }

  public void loadSection(String elementUrl) {
    this.elementUrl = elementUrl;

    getView().showProgressView(true);

    ElementCache cachedElement = ocmController.getCachedElement(elementUrl);

    if (cachedElement != null) {
      renderView(cachedElement);
    } else {
      getView().showEmptyView();
    }

    getView().showProgressView(false);
  }

  private void renderView(ElementCache cachedElement) {
    ElementCacheShare shareElement = cachedElement.getShare();

    UiBaseContentData previewContentData =
        generatePreview(cachedElement.getPreview(), shareElement);

    UiBaseContentData detailContentData =
        generateDetailView(cachedElement.getType(), cachedElement.getRender());

    if (previewContentData != null && detailContentData != null) {
      getView().renderDetailViewWithPreview(previewContentData, detailContentData,
          shareElement != null);
    } else if (previewContentData != null) {
      getView().renderPreview(previewContentData, shareElement != null);
    } else if (detailContentData != null) {
      getView().renderDetailView(detailContentData, shareElement != null);
    }
  }

  private UiBaseContentData generatePreview(ElementCachePreview preview, ElementCacheShare share) {
    if (preview != null && preview.getBehaviour() != ElementCacheBehaviour.NONE) {
      return ocmViewGenerator.generatePreview(preview, share);
    }
    return null;
  }

  private UiBaseContentData generateDetailView(ElementCacheType type, ElementCacheRender render) {
    if (type != ElementCacheType.NONE) {
      return ocmViewGenerator.generateDetailView(type, render);
    }
    return null;
  }

  public void shareElement() {
    ElementCache cachedElement = ocmController.getCachedElement(elementUrl);

    ElementCacheShare shareElement = cachedElement.getShare();

    String shareText = retrieveShareText(shareElement);

    if (!TextUtils.isEmpty(shareText)) {
      getView().shareElement(shareText);
    }
  }

  @Nullable private String retrieveShareText(ElementCacheShare shareElement) {
    String shareText = shareElement.getText();
    String shareUrl = shareElement.getUrl();

    String share;
    if (!TextUtils.isEmpty(shareText) && !TextUtils.isEmpty(shareUrl)) {
      share = shareText + " " + shareUrl;
    } else {
      share = (!TextUtils.isEmpty(shareText)) ? shareText : shareUrl;
    }
    return share;
  }
}

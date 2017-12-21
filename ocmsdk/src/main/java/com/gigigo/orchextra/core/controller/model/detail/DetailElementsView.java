package com.gigigo.orchextra.core.controller.model.detail;

import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;

public interface DetailElementsView {

  void initUi();

  void renderDetailViewWithPreview(UiBaseContentData previewContentData,
      UiBaseContentData detailContentData, ElementCache elementCache);

  void renderDetailView(UiBaseContentData detailContentData, ElementCache elementCache);

  void renderPreview(UiBaseContentData previewContentData, ElementCache elementCache);

  void showProgressView(boolean visible);

  void showEmptyView(boolean isEmpty);

  void shareElement(String shareText);

  void finishView();
}

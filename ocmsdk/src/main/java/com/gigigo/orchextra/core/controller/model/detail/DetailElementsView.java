package com.gigigo.orchextra.core.controller.model.detail;

import com.gigigo.orchextra.core.controller.dto.DetailViewInfo;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;

public interface DetailElementsView {

  void initUi();

  void renderDetailViewWithPreview(UiBaseContentData previewContentData,
      UiBaseContentData detailContentData, DetailViewInfo detailViewInfo);

  void renderDetailView(UiBaseContentData detailContentData, DetailViewInfo detailViewInfo);

  void renderPreview(UiBaseContentData previewContentData, DetailViewInfo detailViewInfo);

  void showProgressView(boolean visible);

  void showEmptyView(boolean isEmpty);

  void shareElement(String shareText);

  void finishView();
}

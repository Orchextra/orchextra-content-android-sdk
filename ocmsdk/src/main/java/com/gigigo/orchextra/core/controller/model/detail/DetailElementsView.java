package com.gigigo.orchextra.core.controller.model.detail;

import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.threaddecoratedview.views.qualifiers.NotDecorated;
import com.gigigo.threaddecoratedview.views.qualifiers.ThreadDecoratedView;

@ThreadDecoratedView
public interface DetailElementsView {

  @NotDecorated
  void initUi();

  void renderDetailViewWithPreview(UiBaseContentData previewContentData,
      UiBaseContentData detailContentData, boolean canShare);

  void renderDetailView(UiBaseContentData detailContentData, boolean canShare);

  void renderPreview(UiBaseContentData previewContentData, boolean canShare);

  void showProgressView(boolean visible);

  void showEmptyView(boolean isEmpty);

  void shareElement(String shareText);
}

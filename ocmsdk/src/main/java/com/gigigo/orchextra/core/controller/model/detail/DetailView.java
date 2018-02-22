package com.gigigo.orchextra.core.controller.model.detail;

import com.gigigo.orchextra.ocm.views.UiDetailBaseContentData;

public interface DetailView {

  void initUi();

  void setView(UiDetailBaseContentData contentDetailView);

  void showError();

  void finishView(boolean showingPreview);

  void setAnimationImageView();

  void redirectToAction(); //TODO: remove this redirect, its related to old segmentation
}

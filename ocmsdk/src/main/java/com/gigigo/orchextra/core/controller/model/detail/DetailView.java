package com.gigigo.orchextra.core.controller.model.detail;

import com.gigigo.orchextra.ocm.views.UiDetailBaseContentData;
import com.gigigo.threaddecoratedview.views.qualifiers.NotDecorated;
import com.gigigo.threaddecoratedview.views.qualifiers.ThreadDecoratedView;

@ThreadDecoratedView
public interface DetailView {

  @NotDecorated
  void initUi();

  void setView(UiDetailBaseContentData contentDetailView);

  void showError();

  void finishView(boolean showingPreview);

  void setAnimationImageView();
}

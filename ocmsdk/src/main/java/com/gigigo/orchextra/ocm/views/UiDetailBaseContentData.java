package com.gigigo.orchextra.ocm.views;

import com.gigigo.orchextra.core.controller.views.UiBaseContentData;

public abstract class UiDetailBaseContentData extends UiBaseContentData {

  public abstract void setOnFinishListener(OnFinishViewListener onFinishListener);

  //public abstract void setTopScroll();

  public interface OnFinishViewListener {
    void onFinish();
  }
}

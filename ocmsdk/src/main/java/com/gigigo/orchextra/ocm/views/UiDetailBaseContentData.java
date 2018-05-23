package com.gigigo.orchextra.ocm.views;

import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.ocm.callbacks.OnFinishViewListener;

public abstract class UiDetailBaseContentData extends UiBaseContentData {

  public abstract void setOnFinishListener(OnFinishViewListener onFinishListener);
}

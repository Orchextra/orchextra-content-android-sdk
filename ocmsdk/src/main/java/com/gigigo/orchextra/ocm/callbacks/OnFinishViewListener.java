package com.gigigo.orchextra.ocm.callbacks;

public abstract class OnFinishViewListener {

  boolean showingPreview = true;

  public void setAppbarExpanded(boolean showingPreview) {
    this.showingPreview = showingPreview;
  }

  public boolean isAppbarExpanded() {
    return showingPreview;
  }

  public abstract void onFinish();

}

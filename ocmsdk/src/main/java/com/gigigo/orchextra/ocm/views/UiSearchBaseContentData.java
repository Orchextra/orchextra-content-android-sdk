package com.gigigo.orchextra.ocm.views;

import android.view.View;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;

public abstract class UiSearchBaseContentData extends UiBaseContentData {

  public abstract void doSearch(String textToSearch);

  public abstract void setEmptyView(View emptyView);

  public abstract void setProgressView(View progressLayout);
}

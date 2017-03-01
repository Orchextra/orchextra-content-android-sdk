package com.gigigo.orchextra.core.sdk.model.detail.viewtypes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;

public class BrowserContentData extends UiBaseContentData {

  private static final String EXTRA_URL = "EXTRA_URL";

  private String url;


  public static BrowserContentData newInstance(String url) {
    BrowserContentData browserElements = new BrowserContentData();

    Bundle bundle = new Bundle();
    bundle.putString(EXTRA_URL, url);
    browserElements.setArguments(bundle);

    return browserElements;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    return inflater.inflate(R.layout.view_blank_elements_item, container, false);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    url = getArguments().getString(EXTRA_URL);
  }

  public String getUrl() {
    return url;
  }
}

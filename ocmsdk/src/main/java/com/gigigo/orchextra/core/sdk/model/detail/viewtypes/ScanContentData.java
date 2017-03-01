package com.gigigo.orchextra.core.sdk.model.detail.viewtypes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;

public class ScanContentData extends UiBaseContentData {

  public static ScanContentData newInstance() {
    return new ScanContentData();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.view_blank_elements_item, container, false);
  }
}

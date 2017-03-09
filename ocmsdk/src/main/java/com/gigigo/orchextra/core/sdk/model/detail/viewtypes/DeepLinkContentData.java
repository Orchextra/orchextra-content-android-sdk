package com.gigigo.orchextra.core.sdk.model.detail.viewtypes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.ocmsdk.R;

public class DeepLinkContentData extends UiBaseContentData {

  private static final String EXTRA_URI = "EXTRA_URI";

  public static DeepLinkContentData newInstance(String uri) {
    DeepLinkContentData fragment = new DeepLinkContentData();

    Bundle bundle = new Bundle();
    bundle.putString(EXTRA_URI, uri);
    fragment.setArguments(bundle);

    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.view_blank_elements_item, container, false);
  }

  public String getUri() {
    return getArguments().getString(EXTRA_URI);
  }
}

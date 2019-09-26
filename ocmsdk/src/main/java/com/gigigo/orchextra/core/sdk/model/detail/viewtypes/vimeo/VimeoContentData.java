package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.vimeo;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.ocmsdk.R;

public class VimeoContentData extends UiBaseContentData {

  private static final String EXTRA_VIDEO_ID = "EXTRA_URL";

  public static VimeoContentData newInstance(String videoId) {
    VimeoContentData fragment = new VimeoContentData();

    Bundle bundle = new Bundle();
    bundle.putString(EXTRA_VIDEO_ID, videoId);
    fragment.setArguments(bundle);

    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.view_blank_elements_item, container, false);
  }

  public String getVideoId() {
    return getArguments().getString(EXTRA_VIDEO_ID, null);
  }
}

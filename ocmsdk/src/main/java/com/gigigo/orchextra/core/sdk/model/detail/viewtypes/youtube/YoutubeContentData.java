package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.ocmsdk.R;

public class YoutubeContentData extends UiBaseContentData {

  private static final String EXTRA_VIDEO_ID = "EXTRA_URL";

  public static YoutubeContentData newInstance(String videoId) {
    YoutubeContentData youtubeContentData = new YoutubeContentData();

    Bundle bundle = new Bundle();
    bundle.putString(EXTRA_VIDEO_ID, videoId);
    youtubeContentData.setArguments(bundle);

    return youtubeContentData;
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

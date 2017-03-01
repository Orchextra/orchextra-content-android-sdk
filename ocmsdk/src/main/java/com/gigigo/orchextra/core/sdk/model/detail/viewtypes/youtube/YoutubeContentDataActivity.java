package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import com.gigigo.orchextra.core.sdk.di.base.BaseActivity;
import com.gigigo.orchextra.ocmsdk.R;


public class YoutubeContentDataActivity extends BaseActivity {

  private static final String EXTRA_YOUTUBE_VIDEO_ID = "EXTRA_YOUTUBE_VIDEO_ID";
  public static final int RESULT_CODE_YOUTUBE_PLAYER = 6189;

  public static void open(Activity activity, String videoId) {
    Intent intent = new Intent(activity, YoutubeContentDataActivity.class);
    intent.putExtra(EXTRA_YOUTUBE_VIDEO_ID, videoId);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    activity.startActivityForResult(intent, RESULT_CODE_YOUTUBE_PLAYER);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_youtube_main_container_layout);

    String videoId = getIntent().getStringExtra(EXTRA_YOUTUBE_VIDEO_ID);

    YoutubeFragment youtubeElements = YoutubeFragment.newInstance(videoId);

    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction()
        .replace(R.id.youtube_main_container, youtubeElements)
        .commit();
  }
}

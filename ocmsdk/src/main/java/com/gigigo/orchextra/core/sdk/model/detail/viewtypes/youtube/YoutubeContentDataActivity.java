package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import com.gigigo.orchextra.core.sdk.di.base.BaseActivity;
import com.gigigo.orchextra.ocmsdk.R;

public class YoutubeContentDataActivity extends BaseActivity {

  private static final String EXTRA_YOUTUBE_VIDEO_ID = "EXTRA_YOUTUBE_VIDEO_ID";
  private static final String TAG_RETAINED_FRAGMENT = "TAG_RETAINED_FRAGMENT";

  public static void open(Context context, String videoId) {
    if (context != null) {
      Intent intent = new Intent(context, YoutubeContentDataActivity.class);
      intent.putExtra(EXTRA_YOUTUBE_VIDEO_ID, videoId);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intent);
    }
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_youtube_main_container_layout);

    FragmentManager fm = getSupportFragmentManager();
    YoutubeFragment youtubeElementsFragment =
        (YoutubeFragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);

    // create the fragment and data the first time
    if (youtubeElementsFragment == null) {

      String videoId = getIntent().getStringExtra(EXTRA_YOUTUBE_VIDEO_ID);
      // videoId = "17uHCHfgs60";//"ikO91fQBsTQ";
      youtubeElementsFragment = YoutubeFragment.newInstance(videoId, getScreenOrientation());
      FragmentManager fragmentManager = getSupportFragmentManager();
      fragmentManager.beginTransaction()
          .replace(R.id.youtube_main_container, youtubeElementsFragment, TAG_RETAINED_FRAGMENT)
          .commit();
    }
  }

  @Override public void onPause() {
    super.onPause();

    if (isFinishing()) {
      FragmentManager fm = getSupportFragmentManager();

      YoutubeFragment youtubeElementsFragment =
          (YoutubeFragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);

      fm.beginTransaction().remove(youtubeElementsFragment).commit();
    }
  }

  public int getScreenOrientation() {
    Display getOrient = getWindowManager().getDefaultDisplay();
    int orientation;
    if (getOrient.getWidth() == getOrient.getHeight()) {
      orientation = Configuration.ORIENTATION_SQUARE;
    } else {
      if (getOrient.getWidth() < getOrient.getHeight()) {
        orientation = Configuration.ORIENTATION_PORTRAIT;
      } else {
        orientation = Configuration.ORIENTATION_LANDSCAPE;
      }
    }
    return orientation;
  }
}

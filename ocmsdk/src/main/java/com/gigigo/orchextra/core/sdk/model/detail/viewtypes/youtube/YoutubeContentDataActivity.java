package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Display;
import com.gigigo.orchextra.core.sdk.di.base.BaseActivity;
import com.gigigo.orchextra.ocmsdk.R;

public class YoutubeContentDataActivity extends BaseActivity {

  private static final String EXTRA_YOUTUBE_VIDEO_ID = "EXTRA_YOUTUBE_VIDEO_ID";
  public static final int RESULT_CODE_YOUTUBE_PLAYER = 6189;
  private static final String TAG_RETAINED_FRAGMENT = "TAG_RETAINED_FRAGMENT";
  private YoutubeFragment youtubeElementsFragment;

  public static void open(Activity activity, String videoId) {

    if (!activity.isFinishing()) {
      Intent intent = new Intent(activity, YoutubeContentDataActivity.class);
      intent.putExtra(EXTRA_YOUTUBE_VIDEO_ID, videoId);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      activity.startActivityForResult(intent, RESULT_CODE_YOUTUBE_PLAYER);
    } else {
      Log.e("+++", "++++++++++++++++++++++++++++++++++++++++++++FINISHING");
    }
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_youtube_main_container_layout);

    FragmentManager fm = getSupportFragmentManager();
    youtubeElementsFragment =
        (YoutubeFragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);

    // create the fragment and data the first time
    if (youtubeElementsFragment == null) {

      String videoId = getIntent().getStringExtra(EXTRA_YOUTUBE_VIDEO_ID);
      // videoId = "17uHCHfgs60";//"ikO91fQBsTQ";
      youtubeElementsFragment = YoutubeFragment.newInstance(videoId, getScreenOrientation());
      FragmentManager fragmentManager = getSupportFragmentManager();
      fragmentManager.beginTransaction().replace(R.id.youtube_main_container,
          youtubeElementsFragment, TAG_RETAINED_FRAGMENT).commit();
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

  @Override
  public void onPause() {
    super.onPause();

    // this means that this activity will not be recreated now, user is leaving it
    // or the activity is otherwise finishing
    if(isFinishing()) {
      FragmentManager fm = getSupportFragmentManager();
      // we will not need this fragment anymore, this may also be a good place to signal
      // to the retained fragment object to perform its own cleanup.
      fm.beginTransaction().remove(youtubeElementsFragment).commit();
    }
  }
}

package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.widget.Toast;
import com.gigigo.orchextra.core.sdk.di.base.BaseActivity;
import com.gigigo.orchextra.ocmsdk.R;
import com.google.android.youtube.player.YouTubePlayer;
import java.util.List;

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
   // videoId = "17uHCHfgs60";//"ikO91fQBsTQ";
    YoutubeFragment youtubeElements = YoutubeFragment.newInstance(videoId, getScreenOrientation());
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction()
        .replace(R.id.youtube_main_container, youtubeElements)
        .commitAllowingStateLoss();
  }

  public int getScreenOrientation()
  {
    Display getOrient = getWindowManager().getDefaultDisplay();
    int orientation = Configuration.ORIENTATION_UNDEFINED;
    if(getOrient.getWidth()==getOrient.getHeight()){
      orientation = Configuration.ORIENTATION_SQUARE;
    } else{
      if(getOrient.getWidth() < getOrient.getHeight()){
        orientation = Configuration.ORIENTATION_PORTRAIT;
      }else {
        orientation = Configuration.ORIENTATION_LANDSCAPE;
      }
    }
    return orientation;
  }

}

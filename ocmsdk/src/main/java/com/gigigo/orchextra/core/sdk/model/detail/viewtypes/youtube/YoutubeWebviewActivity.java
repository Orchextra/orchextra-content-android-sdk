package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.gigigo.orchextra.core.sdk.di.base.BaseActivity;
import com.gigigo.orchextra.ocmsdk.R;


//https://developers.google.com/youtube/player_parameters?hl=es-419
public class YoutubeWebviewActivity extends BaseActivity {

  private static final String EXTRA_YOUTUBE_VIDEO_ID = "EXTRA_YOUTUBE_VIDEO_ID";
  public static final int RESULT_CODE_YOUTUBE_PLAYER = 573;

  public static void open(Activity activity, String videoId) {
    Intent intent = new Intent(activity, YoutubeContentDataActivity.class);
    intent.putExtra(EXTRA_YOUTUBE_VIDEO_ID, videoId);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    activity.startActivityForResult(intent, RESULT_CODE_YOUTUBE_PLAYER);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_youtube_webview_container_layout);

    String videoId = getIntent().getStringExtra(EXTRA_YOUTUBE_VIDEO_ID);

    WebView webviewYoutubeContainer = (WebView) findViewById(R.id.webviewYoutubeContainer);
    webviewYoutubeContainer.getSettings().setJavaScriptEnabled(true);
    webviewYoutubeContainer.getSettings().setPluginState(WebSettings.PluginState.ON);
    webviewYoutubeContainer.loadUrl("http://www.youtube.com/embed/" + videoId + "?autoplay=1&vq=small");
    webviewYoutubeContainer.setWebChromeClient(new WebChromeClient());
  }
}

package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.gigigo.ggglib.device.AndroidSdkVersion;
import com.gigigo.orchextra.core.sdk.di.base.BaseActivity;
import com.gigigo.orchextra.ocmsdk.R;

//https://developers.google.com/youtube/player_parameters?hl=es-419
public class YoutubeWebviewActivity extends BaseActivity {

  private static final String EXTRA_YOUTUBE_VIDEO_ID = "EXTRA_YOUTUBE_VIDEO_ID";
  public static final int RESULT_CODE_YOUTUBE_PLAYER = 573;

  public static void open(Activity activity, String videoId) {
    Intent intent = new Intent(activity, YoutubeWebviewActivity.class);
    intent.putExtra(EXTRA_YOUTUBE_VIDEO_ID, videoId);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    activity.startActivityForResult(intent, RESULT_CODE_YOUTUBE_PLAYER);
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1) @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_youtube_webview_container_layout);

    String videoId = getIntent().getStringExtra(EXTRA_YOUTUBE_VIDEO_ID);

    //String videoId="EtRmU5TgjqU";
    StringBuilder builder = new StringBuilder();
    builder.append("<html><body style='margin:0px; padding:0px;'>\n"
        + "       <script type='text/javascript' src='http://www.youtube.com/iframe_api'>\n"
        + "</script>\n"
        + "<script type='text/javascript'>\n"
        + "  var player;\n"
        + "function onYouTubeIframeAPIReady()\n"
        + "{  \n"
        + " player=new YT.Player('playerId',{events:{onReady:onPlayerReady}});\n"
        + "alert(player.src);\n"
        + "}\n"
        + "function onPlayerReady(event){\n"
        + "//alert(\"hola, entro\");\n"
        + "//player.playVideo();\n"
        + "alert(player.src);\n"
        + "}\n"
        + "</script>\n"
        + "\n"
        //+ "<iframe id='playerId'  type='text/html' width='100%' height='100%' frameborder='0'\n"
        + "<iframe id='playerId' onload=\"javascript:onYouTubeIframeAPIReady();\" type='text/html' width='100%' height='100%' allowfullscreen frameborder='0' \n"
        + "src=\"https://www.youtube.com/embed/sTAan-fd9PU?autoplay=true\"/>\n"
        //+ "src=\"https://www.youtube.com/embed/sTAan-fd9PU?enablejsapi=1&amp;rel=0&amp;playsinline=1&amp;autoplay=true&amp;showinfo=0&amp;autohide=1&amp;controls=1&amp;modestbranding=1&amp;fs=0\" />\n"
        + "</body></html>");





    //String test = "<html><body><iframe src='http://www.youtube.com/embed/HSlD79sqHp4?rel=0&amp;autoplay=1' frameborder='0″' width='100%' height=100%'/></body></html>";


    final WebView webviewYoutubeContainer = (WebView) findViewById(R.id.webviewYoutubeContainer);

    CookieManager.getInstance().setAcceptCookie(true);
    if (Build.VERSION.SDK_INT >= 21) {
      CookieManager.getInstance().setAcceptThirdPartyCookies(webviewYoutubeContainer, true);
    }


    webviewYoutubeContainer.getSettings().setJavaScriptEnabled(true);
    webviewYoutubeContainer.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

    if (AndroidSdkVersion.hasJellyBean16()) {
      webviewYoutubeContainer.getSettings().setAllowFileAccess(true);
      webviewYoutubeContainer.getSettings().setAllowFileAccessFromFileURLs(true);
      webviewYoutubeContainer.getSettings().setAllowUniversalAccessFromFileURLs(true);
    }
    webviewYoutubeContainer.getSettings().setDomStorageEnabled(true);
    webviewYoutubeContainer.getSettings().setPluginState(WebSettings.PluginState.ON);
    webviewYoutubeContainer.getSettings().setDatabaseEnabled(true);

    final String newUserAgent;
    newUserAgent = webviewYoutubeContainer.getSettings().getUserAgentString().replace("Mobile", "eliboM").replace("Android", "diordnA");

    webviewYoutubeContainer.getSettings().setUserAgentString(newUserAgent);
    webviewYoutubeContainer.getSettings().setUseWideViewPort(true);
    webviewYoutubeContainer.getSettings().setLoadWithOverviewMode(true);
    webviewYoutubeContainer.getSettings().setSupportZoom(true);
    webviewYoutubeContainer.getSettings().setBuiltInZoomControls(true);
    webviewYoutubeContainer.setWebViewClient(new WebViewClient());
    webviewYoutubeContainer.setWebChromeClient(new WebChromeClient());
    webviewYoutubeContainer.getSettings().setMediaPlaybackRequiresUserGesture(false);

    webviewYoutubeContainer.loadData(String.valueOf(builder), "text/html", "UTF-8");
    //webviewYoutubeContainer.setWebChromeClient(new WebChromeClient());

   /* webviewYoutubeContainer.setWebViewClient(new WebViewClient() {
      public void onPageFinished(WebView view, String url) {
        new Handler().postDelayed(new Runnable() {
          @Override public void run() {
            webviewYoutubeContainer.loadUrl("javascript:(function() { document.getElementsByTagName('playerId')[0].play(); })()");
          }
        }, 3000);
      }
    });*/

   //final Handler handler = new Handler();
   // handler.postDelayed(new Runnable() {
   //   @Override public void run() {
   //     webviewYoutubeContainer.performClick()
   //
   //         handler.postDelayed(new Runnable() {
   //           @Override public void run() {
   //             webviewYoutubeContainer.performClick()
   //           }
   //         }, 2000);
   //   }
   // }, 2000);
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
    this.finish();
  }
}

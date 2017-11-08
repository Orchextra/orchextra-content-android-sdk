package com.gigigo.orchextra.core.sdk.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.gigigo.ggglib.device.AndroidSdkVersion;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheRender;
import com.gigigo.orchextra.core.domain.entities.elementcache.FederatedAuthorization;
import com.gigigo.orchextra.core.sdk.di.base.BaseActivity;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.WebViewContentData;
import com.gigigo.orchextra.core.sdk.ui.views.toolbars.DetailToolbarView;
import com.gigigo.orchextra.ocmsdk.R;

@Deprecated public class OcmWebViewActivity extends BaseActivity {

  private static final String EXTRA_URL = "EXTRA_URL";
  private static final String EXTRA_FA = "EXTRA_FA";
  private static final String EXTRA_HEADER_TEXT = "EXTRA_HEADER_TEXT";

  String uriImgPreview = "";
  String webviewTitle = "";

  public static void open(Context context, ElementCacheRender render, String toolbarText) {
    Intent intent = new Intent(context, OcmWebViewActivity.class);
    intent.putExtra(EXTRA_URL, render.getUrl());
    intent.putExtra(EXTRA_FA, render.getFederatedAuth());
    intent.putExtra(EXTRA_HEADER_TEXT, toolbarText);
    context.startActivity(intent);
  }

  public static void open(Context context, String url, String toolbarText) {
    Intent intent = new Intent(context, OcmWebViewActivity.class);
    intent.putExtra(EXTRA_URL, url);
    intent.putExtra(EXTRA_HEADER_TEXT, toolbarText);
    context.startActivity(intent);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent i = getIntent();
    if (i != null) {
      if (i.getStringExtra(EXTRA_HEADER_TEXT) != null) {
        webviewTitle = i.getStringExtra(EXTRA_HEADER_TEXT);
      }
    }

    setContentView(R.layout.activity_ocm_webview_layout);

    setStatusbar();
    setToolbar();
    setWebViewFragment();
  }

  private void setToolbar() {
    DetailToolbarView ocmToolbar = (DetailToolbarView) findViewById(R.id.ocmToolbar);
    ocmToolbar.setShareButtonVisible(false);
    ocmToolbar.switchBetweenButtonAndToolbar(true, false);
    ocmToolbar.blockSwipeEvents(true);
    ocmToolbar.setOnClickBackButtonListener(v -> finish());
    ocmToolbar.setToolbarTitle(webviewTitle);
    ocmToolbar.setToolbarIcon(R.drawable.ox_close);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP) private void setStatusbar() {
    if (AndroidSdkVersion.hasLollipop21()) {
      getWindow().setStatusBarColor(getResources().getColor(R.color.oc_status_bar_color));
    }

    if (AndroidSdkVersion.hasJellyBean16()) {
      getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
  }

  private void setWebViewFragment() {
    String url = getIntent().getStringExtra(EXTRA_URL);
    FederatedAuthorization fa = (FederatedAuthorization) getIntent().getSerializableExtra(EXTRA_FA);


    WebViewContentData webViewContentDataFragment;
    if (url != null) {
      webViewContentDataFragment = WebViewContentData.newInstance(url);
    } else {
      webViewContentDataFragment = WebViewContentData.newInstance(url, fa);
    }

    if (uriImgPreview.equals("")) {
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.ocmWebViewContainer, webViewContentDataFragment)
          .commit();
    } else {
      //todo preview y tal
      //WebViewContentData webViewContentData =(WebViewContentData) findViewById(R.id.webviewData);

    }
  }
}

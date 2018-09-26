package com.gigigo.orchextra.core.sdk.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import com.gigigo.ggglib.device.AndroidSdkVersion;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheRender;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheShare;
import com.gigigo.orchextra.core.domain.entities.elementcache.FederatedAuthorization;
import com.gigigo.orchextra.core.sdk.di.base.BaseActivity;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.WebViewContentData;
import com.gigigo.orchextra.core.sdk.ui.views.toolbars.DetailToolbarView;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.OcmEvent;
import com.gigigo.orchextra.ocmsdk.R;
import timber.log.Timber;

@Deprecated public class OcmWebViewActivity extends BaseActivity {

  private static final String EXTRA_URL = "EXTRA_URL";
  private static final String EXTRA_FA = "EXTRA_FA";
  private static final String EXTRA_HEADER_TEXT = "EXTRA_HEADER_TEXT";
  private static final String EXTRA_SHARE = "EXTRA_SHARE";

  String uriImgPreview = "";
  String webviewTitle = "";

  public static void open(Context context, ElementCacheRender render, String toolbarText) {
    Intent intent = new Intent(context, OcmWebViewActivity.class);
    intent.putExtra(EXTRA_URL, render.getUrl());
    intent.putExtra(EXTRA_FA, render.getFederatedAuth());
    intent.putExtra(EXTRA_HEADER_TEXT, toolbarText);
    context.startActivity(intent);
  }

  public static void open(Context context, ElementCacheRender render, String toolbarText,
      ElementCacheShare share) {
    Intent intent = new Intent(context, OcmWebViewActivity.class);
    intent.putExtra(EXTRA_URL, render.getUrl());
    intent.putExtra(EXTRA_FA, render.getFederatedAuth());
    intent.putExtra(EXTRA_HEADER_TEXT, toolbarText);
    intent.putExtra(EXTRA_SHARE, share);
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
    DetailToolbarView ocmToolbar = findViewById(R.id.ocmToolbar);
    ocmToolbar.switchBetweenButtonAndToolbar(false, true, false);
    ocmToolbar.blockSwipeEvents(true);
    ocmToolbar.setOnClickBackButtonListener(v -> finish());
    ocmToolbar.setToolbarTitle(webviewTitle);
    ocmToolbar.setToolbarIcon(R.drawable.ox_close);

    ocmToolbar.setShareButtonVisible(false);

    try {
      ElementCacheShare share = (ElementCacheShare) getIntent().getSerializableExtra(EXTRA_SHARE);
      ocmToolbar.setShareButtonVisible(share != null);
      if (share != null) {
        ocmToolbar.setOnClickShareButtonListener(v -> showShare(share));
      }
    } catch (Exception e) {
      Timber.e(e, "setToolbar()");
    }
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

    WebViewContentData webViewContentDataFragment = null;
    if (url != null && fa != null) {
      webViewContentDataFragment = WebViewContentData.newInstance(url, fa);
    } else if (url != null) {
      webViewContentDataFragment = WebViewContentData.newInstance(url);
    }

    if (webViewContentDataFragment != null && uriImgPreview.equals("")) {
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.ocmWebViewContainer, webViewContentDataFragment)
          .commit();
    } else {
      //todo preview y tal
      //WebViewContentData webViewContentData =(WebViewContentData) findViewById(R.id.webviewData);

    }
  }

  private void showShare(ElementCacheShare shareElement) {
    String shareText = retrieveShareText(shareElement);
    if (!TextUtils.isEmpty(shareText)) {
      shareElement(shareText);
    }
  }

  private void shareElement(String shareText) {
    OCManager.notifyEvent(OcmEvent.SHARE, null);
    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_SEND);
    intent.putExtra(Intent.EXTRA_TEXT, shareText);
    intent.setType("text/plain");
    startActivity(intent);
  }

  @Nullable private String retrieveShareText(ElementCacheShare shareElement) {
    String shareText = shareElement.getText();
    String shareUrl = shareElement.getUrl();

    String share;
    if (!TextUtils.isEmpty(shareText) && !TextUtils.isEmpty(shareUrl)) {
      share = shareText + " " + shareUrl;
    } else {
      share = (!TextUtils.isEmpty(shareText)) ? shareText : shareUrl;
    }
    return share;
  }
}

package com.gigigo.orchextra.core.sdk.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.gigigo.ggglib.device.AndroidSdkVersion;
import com.gigigo.orchextra.core.sdk.di.base.BaseActivity;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.WebViewContentData;
import com.gigigo.orchextra.core.sdk.ui.views.toolbars.DetailToolbarView;
import com.gigigo.orchextra.ocmsdk.R;

public class OcmWebViewActivity extends BaseActivity {

  private static final String EXTRA_URL = "EXTRA_URL";

  public static void open(Context context, String url) {
    Intent intent = new Intent(context, OcmWebViewActivity.class);
    intent.putExtra(EXTRA_URL, url);
    context.startActivity(intent);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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

    WebViewContentData webViewContentDataFragment = WebViewContentData.newInstance(url);

    getSupportFragmentManager().beginTransaction()
        .replace(R.id.ocmWebViewContainer, webViewContentDataFragment)
        .commit();
  }
}

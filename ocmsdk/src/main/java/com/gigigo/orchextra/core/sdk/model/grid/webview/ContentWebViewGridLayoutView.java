package com.gigigo.orchextra.core.sdk.model.grid.webview;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.gigigo.ggglib.device.AndroidSdkVersion;
import com.gigigo.ggglogger.GGGLogImpl;
import com.gigigo.orchextra.core.sdk.model.grid.dto.ClipToPadding;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import com.gigigo.orchextra.ocmsdk.R;

public class ContentWebViewGridLayoutView extends UiGridBaseContentData {

  private static final String EXTRA_URL = "EXTRA_URL";

  private WebView webView;
  private View progress;

  public static ContentWebViewGridLayoutView newInstance(String url) {
    ContentWebViewGridLayoutView fragment = new ContentWebViewGridLayoutView();

    Bundle args = new Bundle();
    args.putString(EXTRA_URL, url);
    fragment.setArguments(args);

    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.view_webview_elements_item, container, false);

    webView = (WebView) view.findViewById(R.id.ocm_webView);
    progress = view.findViewById(R.id.webview_progress);

    return view;
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    init();
  }

  private void init() {
    initWebView();
    loadUrl();
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN) private void initWebView() {
    webView.setClickable(true);

    webView.getSettings().setJavaScriptEnabled(true);

    webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

    webView.setWebViewClient(new WebViewClient() {
      @Override public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        showProgressView(false);
      }
    });
    webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

    if (AndroidSdkVersion.hasJellyBean16()) {
      webView.getSettings().setAllowFileAccessFromFileURLs(true);
      webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
      webView.getSettings().setAllowContentAccess(true);
      webView.getSettings().setAllowFileAccess(true);
      webView.getSettings().setDatabaseEnabled(true);
      webView.getSettings().setDomStorageEnabled(true);
    }
    webView.setWebChromeClient(new WebChromeClient() {
      public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        callback.invoke(origin, true, false);
      }
    });

  }

  private void showProgressView(boolean visible) {
    progress.setVisibility(visible ? View.VISIBLE : View.GONE);
  }

  private void loadUrl() {
    showProgressView(true);
    String url = getArguments().getString(EXTRA_URL);
    GGGLogImpl.log("ContentWebViewGridLayoutView URL: " + url);
    if (!TextUtils.isEmpty(url)) {
      webView.loadUrl(url);
    }
  }

  @Override public void setFilter(String filter) {

  }

  @Override public void setClipToPaddingBottomSize(ClipToPadding clipToPadding) {
    if (webView != null) {
      webView.setClipToPadding(false);
      webView.setPadding(0, 0, 0, clipToPadding.getPadding());
    }
  }

  @Override public void scrollToTop() {
    if (webView != null) {
      webView.scrollTo(0, 0);
    }
  }

  @Override public void setEmptyView(View emptyView) {

  }

  @Override public void setErrorView(View errorLayoutView) {

  }

  @Override public void setProgressView(View progressView) {
    if (progressView != null) {
      progress = progressView;
    }
  }

  @Override public void reloadSection() {
    if (webView != null) {
      webView.reload();
    }
  }
}

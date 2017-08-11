package com.gigigo.orchextra.core.sdk.model.detail.viewtypes;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.bumptech.glide.Glide;
import com.gigigo.ggglib.device.AndroidSdkVersion;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheRender;
import com.gigigo.orchextra.core.domain.entities.elementcache.FederatedAuthorization;
import com.gigigo.orchextra.core.sdk.ui.views.TouchyWebView;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocmsdk.R;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class WebViewContentData extends UiBaseContentData {

  private static final String EXTRA_URL = "EXTRA_URL";
  private static final String EXTRA_FEDERATED_AUTH = "EXTRA_FEDERATED_AUTH";
  View mView;
  private TouchyWebView webView;
  private ProgressBar progress;
  private JsHandler jsInterface;
  private boolean localStorageUpdated;

  private static final String URL_START_QUERY_DELIMITER = "?";
  private static final String URL_CONCAT_QUERY_DELIMITER = "&";
  private static final String URL_QUERY_VALUE_DELIMITER = "=";

  public static WebViewContentData newInstance(ElementCacheRender render) {
    WebViewContentData webViewElements = new WebViewContentData();

    Bundle bundle = new Bundle();
    bundle.putString(EXTRA_URL, render.getUrl());
    bundle.putSerializable(EXTRA_FEDERATED_AUTH, render.getFederatedAuth());
    webViewElements.setArguments(bundle);

    return webViewElements;
  }

  public static WebViewContentData newInstance(String url) {
    WebViewContentData webViewElements = new WebViewContentData();

    Bundle bundle = new Bundle();
    bundle.putString(EXTRA_URL, url);
    webViewElements.setArguments(bundle);

    return webViewElements;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mView = inflater.inflate(R.layout.view_webview_detail_item, container, false);

    webView = (TouchyWebView) mView.findViewById(R.id.ocm_webView);
    progress = (ProgressBar) mView.findViewById(R.id.webview_progress);

    return mView;
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    init();
  }

  @Override public void onDestroy() {

    if (mView != null) {
      unbindDrawables(mView);
      System.gc();
      Glide.get(this.getContext()).clearMemory();
      webView = null;
      progress = null;

      ((ViewGroup) mView).removeAllViews();
      Glide.get(this.getContext()).clearMemory();

      mView = null;
      System.gc();
    }

    super.onDestroy();
  }

  private void unbindDrawables(View view) {
    System.gc();
    Runtime.getRuntime().gc();
    if (view.getBackground() != null) {
      view.getBackground().setCallback(null);
    }
    if (view instanceof ViewGroup) {
      for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
        unbindDrawables(((ViewGroup) view).getChildAt(i));
      }
      ((ViewGroup) view).removeAllViews();
    }
  }

  private void init() {
    initWebView();
    loadUrl();
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN) private void initWebView() {
    jsInterface = new JsHandler(webView);
    webView.setClickable(true);

    webView.getSettings().setJavaScriptEnabled(true);
    webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    webView.getSettings().setDomStorageEnabled(true);
    webView.getSettings().setSupportZoom(false);
    webView.getSettings().setAppCacheEnabled(false);
    webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    webView.getSettings().setDatabaseEnabled(true);
    String databasePath = this.getContext().getDir("databases", Context.MODE_PRIVATE).getPath();
    webView.getSettings().setDatabasePath(databasePath);

    if (AndroidSdkVersion.hasJellyBean16()) {
      webView.getSettings().setAllowFileAccessFromFileURLs(true);
      webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
    }

    webView.addJavascriptInterface(jsInterface, "JsHandler");

    webView.getSettings().setGeolocationDatabasePath(getContext().getFilesDir().getPath());
    webView.setWebChromeClient(new WebChromeClient() {
      public void onGeolocationPermissionsShowPrompt(String origin,
          GeolocationPermissions.Callback callback) {
        callback.invoke(origin, true, false);
      }
    });
    webView.setWebViewClient(new WebViewClient() {
      @Override public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        showProgressView(false);

        setCidLocalStorage();
      }
    });

    webView.setDownloadListener(new DownloadListener() {
      @Override public void onDownloadStart(String url, String userAgent, String contentDisposition,
          String mimetype, long contentLength) {
        getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
      }
    });
  }

  private void setCidLocalStorage() {
    if (!localStorageUpdated && webView != null) {
      Map<String, String> cidLocalStorage = OCManager.getLocalStorage();
      if (cidLocalStorage != null) {
        for (Map.Entry<String, String> element : cidLocalStorage.entrySet()) {
          final String key = element.getKey();
          final String value = element.getValue();
          String script = "window.localStorage.setItem(\'%1s\',\'%2s\')";
          //String result = jsInterface.getJSValue(this, String.format(script, new Object[]{key, value}));
          jsInterface.javaFnCall(String.format(script, new Object[] { key, value }));
        }
      }

      localStorageUpdated = true;
      webView.reload();
    }
  }

  private void showProgressView(boolean visible) {
    if (progress != null) {
      progress.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
  }

  private String getQueryDelimiter(String url) {
    try {
      return new URL(url).getQuery() != null ? URL_CONCAT_QUERY_DELIMITER
          : URL_START_QUERY_DELIMITER;
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    return null;
  }

  private String addQueryParamsToUrl(List<Pair<String, String>> queryParams, String url) {
    if (getQueryDelimiter(url) != null) {
      url = url + getQueryDelimiter(url);

      Iterator<Pair<String, String>> iterator = queryParams.iterator();
      while (iterator.hasNext()) {
        Pair<String, String> pair = iterator.next();
        url = url + pair.first + URL_QUERY_VALUE_DELIMITER + pair.second + URL_CONCAT_QUERY_DELIMITER;
      }

      return url.substring(0, url.length() - 2);
    } else {
      return null;
    }
  }

  private void loadUrl() {
    showProgressView(true);

    String url = getArguments().getString(EXTRA_URL);
    if (!url.isEmpty()) {
      FederatedAuthorization federatedAuthorization =
          (FederatedAuthorization) getArguments().getSerializable(EXTRA_FEDERATED_AUTH);

      if (federatedAuthorization.isActive() && Ocm.getQueryStringGenerator() != null) {
        Ocm.getQueryStringGenerator().createQueryString(federatedAuthorization, queryString -> {
          if (queryString != null && !queryString.isEmpty()) {
            String urlWithQueryParams =
                addQueryParamsToUrl(queryString, url);
            if (urlWithQueryParams != null) {
              webView.loadUrl(urlWithQueryParams);
            }
          } else {
            webView.loadUrl(url);
          }
        });
      } else {
        webView.loadUrl(url);
      }
    }
  }

  private class JsHandler {
    WeakReference<WebView> webView;
    private CountDownLatch latch = null;
    private String returnValue;

    public JsHandler(WebView _webView) {
      webView = new WeakReference<>(_webView);
    }

    /**
     * This function handles call from Android-Java
     */
    public synchronized String javaFnSyncCall(String jsString) {
      this.latch = new CountDownLatch(1);
      String code = "javascript:window.JsHandler.setValue((function(){try{return "
          + jsString
          + "+\"\";}catch(js_eval_err){return \'\';}})());";
      if (webView.get() != null) webView.get().loadUrl(code);

      try {
        this.latch.await(1L, TimeUnit.SECONDS);
        return this.returnValue;
      } catch (InterruptedException e) {
        Log.e("JsHandler", "Interrupted", e);
        Thread.currentThread().interrupt();
        return null;
      }
    }

    public void javaFnCall(String jsString) {
      final String webUrl = "javascript:" + jsString;
      // Add this to avoid android.view.windowmanager$badtokenexception unable to add window

      new Runnable() {
        @Override public void run() {
          if (webView.get() != null) webView.get().loadUrl(webUrl);
        }
      }.run();
    }

    @JavascriptInterface public void setValue(String value) {
      this.returnValue = value;

      try {
        this.latch.countDown();
      } catch (Exception var3) {
        ;
      }
    }
  }
}

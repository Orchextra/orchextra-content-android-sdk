package com.gigigo.orchextra.core.sdk.model.detail.layouts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.orchextra.Orchextra;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.BrowserContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.CustomTabsContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.DeepLinkContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.ScanContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.VuforiaContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube.YoutubeContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube.YoutubeContentDataActivity;
import com.gigigo.orchextra.core.sdk.ui.views.toolbars.DetailToolbarView;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.OcmEvent;
import com.gigigo.orchextra.ocm.views.UiDetailBaseContentData;
import com.gigigo.orchextra.ocmsdk.R;

public abstract class DetailParentContentData extends UiBaseContentData {

  protected UiDetailBaseContentData.OnFinishViewListener onFinishListener;
  protected OnShareListener onShareListener;
  protected DetailToolbarView detailToolbarView;
  private Context context;

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    this.context = context;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(getDetailLayout(), container, false);

    initDetailViews(view);

    return view;
  }

  private void initDetailViews(View view) {
    detailToolbarView = (DetailToolbarView) view.findViewById(R.id.detailToolbarView);

    initViews(view);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    setOnClickListenerButtons();
  }

  protected void setOnClickListenerButtons() {
    detailToolbarView.setOnClickBackButtonListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (onFinishListener != null) {
          onFinishListener.onFinish();
        }
      }
    });

    detailToolbarView.setShareButtonVisible(onShareListener != null);

    if (onShareListener != null) {
      detailToolbarView.setOnClickShareButtonListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onShareListener.onShare();
        }
      });
    }
  }

  public void setOnFinishListener(UiDetailBaseContentData.OnFinishViewListener onFinishListener) {
    this.onFinishListener = onFinishListener;
  }

  protected boolean checkIfOxActionAndExecute(UiBaseContentData uiBaseContentData) {
    Class<? extends UiBaseContentData> detailContentDataClass = uiBaseContentData.getClass();
    if (detailContentDataClass.equals(VuforiaContentData.class)) {
      launchOxVuforia();
      OCManager.notifyEvent(OcmEvent.OPEN_IR);
      return true;
    } else if (detailContentDataClass.equals(ScanContentData.class)) {
      lauchOxScan();
      OCManager.notifyEvent(OcmEvent.OPEN_BARCODE);
      return true;
    } else if (detailContentDataClass.equals(BrowserContentData.class)) {
      launchExternalBrowser(((BrowserContentData) uiBaseContentData).getUrl());
      OCManager.notifyEvent(OcmEvent.VISIT_URL);
      return true;
    } else if (detailContentDataClass.equals(YoutubeContentData.class)) {
      launchExternalYoutube(((YoutubeContentData) uiBaseContentData).getUrl());
      OCManager.notifyEvent(OcmEvent.PLAY_YOUTUBE);
      return true;
    } else if (detailContentDataClass.equals(DeepLinkContentData.class)) {
      processDeepLink(((DeepLinkContentData) uiBaseContentData).getUri());
      OCManager.notifyEvent(OcmEvent.VISIT_URL);
      return true;
    } else if (detailContentDataClass.equals(CustomTabsContentData.class)) {
      launchCustomTabs(((CustomTabsContentData) uiBaseContentData).getUrl());
      OCManager.notifyEvent(OcmEvent.VISIT_URL);
      return true;
    }
    return false;
  }

  private void processDeepLink(String uri) {
    OCManager.returnOcCustomSchemeCallback(uri);
  }

  private void launchExternalYoutube(String url) {
    YoutubeContentDataActivity.open(getActivity(), url);
  }

  private void launchOxVuforia() {
    Orchextra.startImageRecognition();
  }

  private void lauchOxScan() {
    Orchextra.startScannerActivity();
  }

  private void launchExternalBrowser(String url) {
    Intent i = new Intent(Intent.ACTION_VIEW);
    i.setData(Uri.parse(url));
    getContext().startActivity(i);
  }

  private void launchCustomTabs(String url) {
    DeviceUtils.openChromeTabs((Activity) context, url);
  }

  public void setOnShareListener(OnShareListener onShareListener) {
    this.onShareListener = onShareListener;
  }

  protected abstract void initViews(View view);

  protected abstract int getDetailLayout();

  public interface OnShareListener {
    void onShare();
  }
}

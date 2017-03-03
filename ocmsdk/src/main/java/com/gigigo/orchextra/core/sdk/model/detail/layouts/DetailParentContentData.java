package com.gigigo.orchextra.core.sdk.model.detail.layouts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.orchextra.Orchextra;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.BrowserContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.ScanContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.VuforiaContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube.YoutubeContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube.YoutubeContentDataActivity;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube.YoutubeWebviewActivity;
import com.gigigo.orchextra.core.sdk.ui.behaviours.ScrollShareButtonBehavior;
import com.gigigo.orchextra.ocm.views.UiDetailBaseContentData;
import com.gigigo.orchextra.ocmsdk.R;

public abstract class DetailParentContentData extends UiBaseContentData {

  private View backToolbarButton;
  protected View shareToolbarButton;

  protected UiDetailBaseContentData.OnFinishViewListener onFinishListener;
  protected OnShareListener onShareListener;

  @Override public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(getDetailLayout(), container, false);

    initDetailViews(view);

    return view;
  }

  private void initDetailViews(View view) {
    backToolbarButton = view.findViewById(R.id.back_toolbar_button);
    shareToolbarButton = view.findViewById(R.id.share_toolbar_button);

    initViews(view);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    setOnClickListenerButtons();
  }

  protected void setOnClickListenerButtons() {
    backToolbarButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (onFinishListener != null) {
          onFinishListener.onFinish();
        }
      }
    });

    if (onShareListener != null) {

      initShareButton();

      shareToolbarButton.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onShareListener.onShare();
        }
      });
    }
  }

  protected abstract void initShareButton();

  public void setOnFinishListener(UiDetailBaseContentData.OnFinishViewListener onFinishListener) {
    this.onFinishListener = onFinishListener;
  }

  protected boolean checkIfOxActionAndExecute(UiBaseContentData uiBaseContentData) {
    Class<? extends UiBaseContentData> detailContentDataClass = uiBaseContentData.getClass();
    if (detailContentDataClass.equals(VuforiaContentData.class)) {
      launchOxVuforia();
      return true;
    } else if (detailContentDataClass.equals(ScanContentData.class)) {
      lauchOxScan();
      return true;
    } else if (detailContentDataClass.equals(BrowserContentData.class)) {
      launchExternalBrowser(((BrowserContentData) uiBaseContentData).getUrl());
      return true;
    } else if (detailContentDataClass.equals(YoutubeContentData.class)) {
      launchExternalYoutube(((YoutubeContentData) uiBaseContentData).getUrl());
      return true;
    }
    return false;
  }

  private void launchExternalYoutube(String url) {
    //YoutubeContentDataActivity.open(getActivity(), url);
    YoutubeWebviewActivity.open(getActivity(), url);
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

  public void setOnShareListener(OnShareListener onShareListener) {
    this.onShareListener = onShareListener;
  }

  public interface OnShareListener {
    void onShare();
  }

  protected abstract void initViews(View view);

  protected abstract int getDetailLayout();
}

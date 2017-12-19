package com.gigigo.orchextra.core.sdk.model.detail.layouts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheType;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.BrowserContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.PreviewContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.WebViewContentData;
import com.gigigo.orchextra.ocmsdk.R;

public class DetailSimpleLayoutContentData extends DetailParentContentData {

  private UiBaseContentData uiBaseContentData;
  private View contentMainLayout;

  public static DetailSimpleLayoutContentData newInstance() {
    return new DetailSimpleLayoutContentData();
  }

  @Override protected void initViews(View view) {
    contentMainLayout = view.findViewById(R.id.contentMainLayout);
  }

  @Override protected int getDetailLayout() {
    return R.layout.view_detail_elements_single_layout;
  }

  @Override protected void closeView() {
    if (onFinishListener != null) {
      onFinishListener.onFinish();
    }
  }

  public void setViews(UiBaseContentData uiBaseContentData) {
    this.uiBaseContentData = uiBaseContentData;
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    if (uiBaseContentData != null && isAdded()) {
      if (!checkIfOxActionAndExecute(uiBaseContentData)) {
        ((AppCompatActivity) getContext()).getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.contentMainLayout, uiBaseContentData)
            .commit();

        if (uiBaseContentData instanceof PreviewContentData) {
          setOnClickListenerButtons();
        } else {
          if (!(uiBaseContentData instanceof WebViewContentData) && !(uiBaseContentData instanceof BrowserContentData)) {
            detailToolbarView.switchBetweenButtonAndToolbar(true,true);
          } else {
            detailToolbarView.switchBetweenButtonAndToolbar(false,true);
          }
          setPaddingTop();
        }
      } else {
        closeView();
      }
    }

    if (onFinishListener != null) {
      onFinishListener.setAppbarExpanded(false);
    }
  }

  private void setPaddingTop() {
    int dimension =
        (int) getContext().getResources().getDimension(R.dimen.ocm_height_detail_toolbar);
    contentMainLayout.setPadding(0, dimension, 0, 0);
  }

  @Override public void onDestroy() {

    //private UiBaseContentData uiBaseContentData;
    //private View contentMainLayout;
    System.out.println(
        "----onDestroy------------------------------------------artivcle coordinator content data");
    if (contentMainLayout != null) unbindDrawables(contentMainLayout);

    ((ViewGroup) contentMainLayout).removeAllViews();
    System.gc();

    Glide.get(this.getContext()).clearMemory();

    try {
      if (uiBaseContentData != null) {
        uiBaseContentData.onDestroy();
      }
    } catch (Exception ignore) {
    }
    uiBaseContentData = null;
    contentMainLayout = null;

    Glide.get(this.getContext()).clearMemory();

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

  public void setTypeContent(ElementCacheType type) {
    switch (type) {
      case WEBVIEW:
        changeIconToolbar();
        break;
    }
  }
}

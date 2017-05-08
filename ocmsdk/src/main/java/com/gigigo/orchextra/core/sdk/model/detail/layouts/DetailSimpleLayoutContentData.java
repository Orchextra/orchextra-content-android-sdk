package com.gigigo.orchextra.core.sdk.model.detail.layouts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.PreviewContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.CardContentData;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;

public class DetailSimpleLayoutContentData extends DetailParentContentData {

  private UiBaseContentData uiBaseContentData;
  private View contentMainLayout;

  public static DetailSimpleLayoutContentData newInstance() {
    return new DetailSimpleLayoutContentData();
  }

  @Override
  protected void initViews(View view) {
    contentMainLayout = view.findViewById(R.id.contentMainLayout);
  }

  @Override protected int getDetailLayout() {
    return R.layout.view_detail_elements_single_layout;
  }

  public void setViews(UiBaseContentData uiBaseContentData) {
    this.uiBaseContentData = uiBaseContentData;
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    if (uiBaseContentData != null) {
      if (!checkIfOxActionAndExecute(uiBaseContentData)) {
        ((AppCompatActivity) getContext()).getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.contentMainLayout, uiBaseContentData)
            .commit();

        if (uiBaseContentData instanceof PreviewContentData) {
          setOnClickListenerButtons();
        } else {
          detailToolbarView.switchBetweenButtonAndToolbar(true);
          setPaddingTop();
        }

      } else {
        if (onFinishListener != null) {
          onFinishListener.onFinish();
        }
      }
    }
  }

  private void setPaddingTop() {
    int dimension =
        (int) getContext().getResources().getDimension(R.dimen.ocm_height_detail_toolbar);
    contentMainLayout.setPadding(0, dimension, 0, 0);
  }
}

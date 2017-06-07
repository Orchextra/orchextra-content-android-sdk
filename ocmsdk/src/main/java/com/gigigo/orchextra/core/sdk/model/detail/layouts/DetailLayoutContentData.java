package com.gigigo.orchextra.core.sdk.model.detail.layouts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.orchextra.core.controller.dto.DetailViewInfo;
import com.gigigo.orchextra.core.controller.model.detail.DetailElementsView;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.OcmEvent;
import com.gigigo.orchextra.ocm.callbacks.OnFinishViewListener;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.orchextra.core.controller.model.detail.DetailElementsViewPresenter;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.ocm.views.UiDetailBaseContentData;

public class DetailLayoutContentData extends UiDetailBaseContentData implements DetailElementsView {

  private String elementUrl;
  private DetailElementsViewPresenter presenter;
  private final View.OnClickListener retryButtonListener = new View.OnClickListener() {
    @Override public void onClick(View v) {
      presenter.loadSection(elementUrl);
    }
  };
  private OnFinishViewListener onFinishListener;
  private Context context;
  private DetailParentContentData.OnShareListener onShareListener =
      new DetailParentContentData.OnShareListener() {
        @Override public void onShare() {
          presenter.shareElement();
        }
      };

  public static DetailLayoutContentData newInstance() {
    return new DetailLayoutContentData();
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);

    this.context = context;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    return inflater.inflate(R.layout.view_detail_elements_layout, container, false);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    if (presenter != null) {
      presenter.attachView(this);
    }
  }

  @Override public void setOnFinishListener(OnFinishViewListener onFinishListener) {
    this.onFinishListener = onFinishListener;
  }

  public void setElementUrl(String elementUrl) {
    this.elementUrl = elementUrl;
  }

  public void setPresenter(DetailElementsViewPresenter presenter) {
    this.presenter = presenter;
  }

  @Override public void initUi() {
    View ocmRetryButton = getView().findViewById(R.id.ocm_retry_button);
    ocmRetryButton.setOnClickListener(retryButtonListener);

    presenter.loadSection(elementUrl);
  }

  @Override public void renderDetailViewWithPreview(UiBaseContentData previewContentData,
      UiBaseContentData detailContentData, DetailViewInfo detailViewInfo) {

    addLayoutToCoordinatorLayoutView(previewContentData, detailContentData, detailViewInfo);
  }

  private void addLayoutToCoordinatorLayoutView(UiBaseContentData previewContentData,
      UiBaseContentData detailContentData, DetailViewInfo detailViewInfo) {

    DetailCoordinatorLayoutContentData detailCoordinatorLayoutContentData =
        DetailCoordinatorLayoutContentData.newInstance();

    detailCoordinatorLayoutContentData.setViews(previewContentData, detailContentData);
    detailCoordinatorLayoutContentData.setOnFinishListener(onFinishListener);
    detailCoordinatorLayoutContentData.setArticleName(detailViewInfo.getNameArticle());
    if (detailViewInfo.isShareable()) {
      detailCoordinatorLayoutContentData.setOnShareListener(onShareListener);
    }

    ((AppCompatActivity) context).getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.detail_container_layout, detailCoordinatorLayoutContentData)
        .commit();
  }

  @Override public void renderDetailView(UiBaseContentData detailContentData, DetailViewInfo detailViewInfo) {
    addLayoutToView(detailContentData, detailViewInfo);
  }

  @Override public void renderPreview(UiBaseContentData previewContentData, DetailViewInfo detailViewInfo) {
    addLayoutToView(previewContentData, detailViewInfo);
  }

  @Override public void showProgressView(boolean visible) {

  }

  @Override public void showEmptyView(boolean isEmpty) {
    if (getView() != null) {
      View emptyView = getView().findViewById(R.id.view_retry);
      emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }
  }

  @Override public void shareElement(String shareText) {
    OCManager.notifyEvent(OcmEvent.SHARE, null);
    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_SEND);
    intent.putExtra(Intent.EXTRA_TEXT, shareText);
    intent.setType("text/plain");
    startActivity(intent);
  }

  @Override public void finishView() {
    if (onFinishListener != null) {
      onFinishListener.setAppbarExpanded(true);
      onFinishListener.onFinish();
    }
  }

  private void addLayoutToView(UiBaseContentData uiBaseContentData, DetailViewInfo detailViewInfo) {

    DetailSimpleLayoutContentData detailSimpleLayoutContentData =
        DetailSimpleLayoutContentData.newInstance();

    detailSimpleLayoutContentData.setViews(uiBaseContentData);
    detailSimpleLayoutContentData.setOnFinishListener(onFinishListener);
    detailSimpleLayoutContentData.setArticleName(detailViewInfo.getNameArticle());
    if (detailViewInfo.isShareable()) {
      detailSimpleLayoutContentData.setOnShareListener(onShareListener);
    }

    ((AppCompatActivity) context).getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.detail_container_layout, detailSimpleLayoutContentData)
        .commit();
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override public void onDestroyView() {
    System.out.println("----------------------------------------------destroyview");

    if (context instanceof Activity) {
      if (presenter != null) {
        presenter.detachView();
      }
      onFinishListener = null;
      ((Activity) context).finish();

    }
    this.context = null;
    super.onDestroyView();
  }

}

package com.gigigo.orchextra.ocm.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.gigigo.multiplegridrecyclerview.entities.Cell;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.core.sdk.model.grid.dto.ClipToPadding;
import com.gigigo.ui.imageloader.ImageLoader;
import java.util.List;

public abstract class UiListedBaseContentData extends UiBaseContentData {

  protected ListedContentListener listedContentListener;

  protected View emptyView;
  protected View errorView;
  protected View loadingView;
  protected ClipToPadding clipToPadding;
  protected Authoritation authoritation;
  protected ImageLoader imageLoader;

  public abstract void setData(List<Cell> cellDataList);

  public abstract void scrollToTop();

  public abstract void showErrorView();

  public abstract void showEmptyView();

  public abstract void showProgressView(boolean isVisible);

  public abstract void setOnScrollListener(RecyclerView.OnScrollListener onScrollListener);

  public void setImageLoader(ImageLoader imageLoader) {
    this.imageLoader = imageLoader;
  }

  public void setAuthoritation(Authoritation authoritation) {
    this.authoritation = authoritation;
  }

  public void setClipToPadding(ClipToPadding clipToPadding) {
    this.clipToPadding = clipToPadding;
  }

  public void setEmptyViewLayout(View emptyView) {
    this.emptyView = emptyView;
  }

  public void setErrorViewLayout(View errorView) {
    this.errorView = errorView;
  }

  public void setLoadingViewLayout(View loadingView) {
    this.loadingView = loadingView;
  }

  public void setListedContentListener(ListedContentListener listedContentListener) {
    this.listedContentListener = listedContentListener;
  }

  public interface ListedContentListener {
    void reloadSection();

    void onItemClicked(int position, View view);
  }
}

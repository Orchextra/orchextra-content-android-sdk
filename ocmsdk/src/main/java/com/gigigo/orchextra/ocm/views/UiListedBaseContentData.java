package com.gigigo.orchextra.ocm.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.gigigo.multiplegridrecyclerview.entities.Cell;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.core.sdk.model.grid.dto.ClipToPadding;
import com.gigigo.ui.imageloader.ImageLoader;
import java.util.List;

public abstract class UiListedBaseContentData extends LinearLayout {

  protected ListedContentListener listedContentListener;

  protected View emptyView;
  protected View errorView;
  protected View loadingView;
  protected ClipToPadding clipToPadding = ClipToPadding.PADDING_NONE;
  protected Authoritation authoritation;
  protected ImageLoader imageLoader;

  //4 carrusel
  public boolean bIsSliderActive = false;
  public boolean bIsYOffsetSetted = false;
  public int mTime = 0;
  public float mYOffset = 0;

  public UiListedBaseContentData(Context context) {
    super(context);
  }

  public UiListedBaseContentData(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public UiListedBaseContentData(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public abstract void setData(List<Cell> cellDataList);

  public abstract void scrollToTop();

  public abstract void showErrorView();

  public abstract void showEmptyView();

  public abstract void showProgressView(boolean isVisible);

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

  public void setParams(ClipToPadding clipToPadding, ImageLoader imageLoader,
      Authoritation authoritation) {
    this.clipToPadding = clipToPadding;
    this.imageLoader = imageLoader;
    this.authoritation = authoritation;

    init();
  }

  protected abstract void init();

  public interface ListedContentListener {
    void reloadSection();

    void onItemClicked(int position, View view);
  }
}

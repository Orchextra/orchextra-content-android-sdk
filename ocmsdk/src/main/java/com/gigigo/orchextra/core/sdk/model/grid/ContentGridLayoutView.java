package com.gigigo.orchextra.core.sdk.model.grid;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.gigigo.multiplegridrecyclerview.entities.Cell;
import com.gigigo.orchextra.core.controller.model.home.grid.ContentView;
import com.gigigo.orchextra.core.controller.model.home.grid.ContentViewPresenter;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItemTypeLayout;
import com.gigigo.orchextra.core.sdk.di.injector.Injector;
import com.gigigo.orchextra.core.sdk.model.grid.dto.ClipToPadding;
import com.gigigo.orchextra.core.sdk.model.grid.horizontalviewpager.HorizontalViewPager;
import com.gigigo.orchextra.core.sdk.model.grid.spannedgridrecyclerview.SpannedGridRecyclerView;
import com.gigigo.orchextra.core.sdk.model.grid.verticalviewpager.VerticalViewContent;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import com.gigigo.orchextra.ocm.views.UiListedBaseContentData;
import com.gigigo.orchextra.ocmsdk.R;
import java.util.List;
import orchextra.javax.inject.Inject;

public class ContentGridLayoutView extends UiGridBaseContentData implements ContentView {

  public boolean bIsSliderActive = false;
  public int mTime = 100;
  @Inject ContentViewPresenter presenter;
  private UiListedBaseContentData uiListedBaseContentData;
  UiListedBaseContentData.ListedContentListener listedContentListener =
      new UiListedBaseContentData.ListedContentListener() {
        @Override public void reloadSection() {
          if (presenter != null) {
            presenter.loadSectionAndNotifyMenu();
          }
          uiListedBaseContentData.showProgressView(false);
        }

        @Override public void onItemClicked(int position, View view) {
          if (presenter != null) {
            presenter.onItemClicked(position, view);
          }
        }
      };
  private ClipToPadding clipToPadding = ClipToPadding.PADDING_NONE;
  private int addictionalPadding = 0;
  private Context context;
  private View retryButton;
  private View moreButton;
  private UiMenu uiMenu;
  private int imagesToDownload;
  private String emotion;
  private View emptyView;
  private View errorView;
  private View progressView;
  private FrameLayout listedDataContainer;
  private boolean thumbnailEnabled;
  private LoadContentCallback loadContentCallback;

  private View newContentContainer;
  private final View.OnClickListener onNewContentClickListener = new View.OnClickListener() {
    @Override public void onClick(View v) {
      newContentContainer.setVisibility(View.GONE);
      if (presenter != null) {
        presenter.loadSection();
      }
    }
  };

  private View.OnClickListener onClickDiscoverMoreButtonListener = v -> {
    if (onLoadMoreContentListener != null) {
      onLoadMoreContentListener.onLoadMoreContent();
    }
  };
  private View.OnClickListener onClickRetryButtonListener = new View.OnClickListener() {
    @Override public void onClick(View v) {
      if (presenter != null) {
        presenter.loadSection();
      }
    }
  };

  public static ContentGridLayoutView newInstance() {
    return new ContentGridLayoutView();
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    this.context = context;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.ocm_content_grid_layout_view, container, false);
    initView(view);
    initDI();
    return view;
  }

  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    setListeners();

    try {
      presenter.attachView(this);
    } catch (NullPointerException e) {
      Ocm.logException(e);
    }
  }

  private void initDI() {
    Injector injector = OCManager.getInjector();
    try {
      injector.injectContentGridLayoutView(this);
      thumbnailEnabled = injector.provideOcmStyleUi().isThumbnailEnabled();
    } catch (NullPointerException e) {
      Ocm.logException(e);
    }
  }

  private void initView(View view) {
    if (emptyView == null) {
      emptyView = view.findViewById(R.id.ocm_empty_layout);
    }
    if (errorView == null) {
      errorView = view.findViewById(R.id.ocm_error_layout);
    }
    retryButton = view.findViewById(R.id.ocm_retry_button);
    moreButton = view.findViewById(R.id.ocm_more_button);
    listedDataContainer = (FrameLayout) view.findViewById(R.id.listedDataContainer);
    newContentContainer = view.findViewById(R.id.newContentContainer);
  }

  private void setListeners() {
    retryButton.setOnClickListener(onClickRetryButtonListener);
    moreButton.setOnClickListener(onClickDiscoverMoreButtonListener);
  }

  public void setViewId(UiMenu uiMenu, int imagesToDownload) {
    this.uiMenu = uiMenu;
    this.imagesToDownload = imagesToDownload;
  }

  @Override public void initUi() {
    if (uiMenu != null && presenter != null) {
      presenter.setPadding(clipToPadding.getPadding());
      presenter.setImagesToDownload(imagesToDownload);
      presenter.loadSection(uiMenu, emotion);
    }
  }

  @Override public void onResume() {
    super.onResume();
  }

  @Override public void setData(List<Cell> cellDataList, ContentItemTypeLayout type) {

    if (loadContentCallback != null) {
      loadContentCallback.onLoadContent(type);
    }

    switch (type) {
      case GRID:
        setDataGrid(cellDataList);
        break;
      case CAROUSEL:
        setDataCarousel(cellDataList);
        break;
      case FULLSCREEN:
        setDataFullScreen(cellDataList);
        break;
    }
  }

  private void setDataGrid(List<Cell> cellDataList) {
    if (uiListedBaseContentData == null) {

      uiListedBaseContentData = new SpannedGridRecyclerView(context);

      uiListedBaseContentData.setListedContentListener(listedContentListener);
      uiListedBaseContentData.setParams(clipToPadding, addictionalPadding, thumbnailEnabled);
      uiListedBaseContentData.setData(cellDataList);

      listedDataContainer.removeAllViews();
      listedDataContainer.addView(uiListedBaseContentData);
    } else {
      uiListedBaseContentData.setData(cellDataList);
    }
  }

  private void setDataCarousel(List<Cell> cellDataList) {
    uiListedBaseContentData = new HorizontalViewPager(context);

    if (this.bIsSliderActive) this.setViewPagerAutoSlideTime(this.mTime);

    uiListedBaseContentData.setListedContentListener(listedContentListener);
    uiListedBaseContentData.setParams(ClipToPadding.PADDING_NONE, addictionalPadding,
        thumbnailEnabled);
    uiListedBaseContentData.setData(cellDataList);

    listedDataContainer.removeAllViews();
    listedDataContainer.addView(uiListedBaseContentData);
  }

  private void setDataFullScreen(List<Cell> cellDataList) {

    uiListedBaseContentData = new VerticalViewContent(context);
    ((VerticalViewContent) uiListedBaseContentData).setFragmentManager(getChildFragmentManager());

    uiListedBaseContentData.setListedContentListener(listedContentListener);
    uiListedBaseContentData.setParams(ClipToPadding.PADDING_NONE, addictionalPadding,
        thumbnailEnabled);
    uiListedBaseContentData.setData(cellDataList);

    listedDataContainer.removeAllViews();
    listedDataContainer.addView(uiListedBaseContentData);
  }

  @Override public void showEmptyView(boolean isVisible) {
    if (emptyView != null) {
      emptyView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
  }

  @Override public void showErrorView(boolean isVisible) {
    if (errorView != null) {
      errorView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
  }

  @Override public void showProgressView(boolean isVisible) {
    if (progressView != null) {
      progressView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
    if (uiListedBaseContentData != null) {
      uiListedBaseContentData.showProgressView(isVisible);
    }
  }

  @Override public void setFilter(String filter) {
    if (presenter != null) {
      presenter.setFilter(filter);
      if (uiListedBaseContentData != null && presenter.getChildCount() > 0) {
        uiListedBaseContentData.scrollToTop();
      }
    }
  }

  @Override
  public void setClipToPaddingBottomSize(ClipToPadding clipToPadding, int addictionalPadding) {
    this.clipToPadding = clipToPadding;
    this.addictionalPadding = addictionalPadding;
  }

  @Override public void scrollToTop() {
    if (uiListedBaseContentData != null) {
      uiListedBaseContentData.scrollToTop();
    }
  }

  @Override public void setEmptyView(View emptyView) {
    this.emptyView = emptyView;
  }

  public void setErrorView(View errorView) {
    this.errorView = errorView;
  }

  @Override public void setProgressView(View progressView) {
    this.progressView = progressView;
  }

  @Override public void reloadSection(boolean hasToShowNewContentButton) {
    if (presenter != null) {
      presenter.setHasToCheckNewContent(hasToShowNewContentButton);
      presenter.loadSection(!hasToShowNewContentButton, uiMenu, emotion, false);
    }
  }

  public void setEmotion(String emotion) {
    this.emotion = emotion;
  }

  public void setViewPagerAutoSlideTime(int time) {
    if (time > 0) {
      this.mTime = time;
      this.bIsSliderActive = true;
      if (uiListedBaseContentData instanceof HorizontalViewPager) {
        ((HorizontalViewPager) uiListedBaseContentData).setViewPagerAutoSlideTime(time);
      }
    } else {
      System.out.println("You must set positive value");
    }
  }

  @Override public void showNewExistingContent() {
    newContentContainer.setVisibility(View.VISIBLE);
    newContentContainer.setOnClickListener(onNewContentClickListener);
  }

  @Override public void onDestroy() {
    if (presenter != null) {
      presenter.destroy();
      presenter.detachView();
    }

    super.onDestroy();
  }

  @Override public void contentNotAvailable() {
    Snackbar.make(listedDataContainer, R.string.oc_error_content_not_available_without_internet,
        Snackbar.LENGTH_SHORT).show();
  }

  public void setLoadContentCallback(LoadContentCallback loadContentCallback) {
    this.loadContentCallback = loadContentCallback;
  }

  public interface LoadContentCallback {
    void onLoadContent(ContentItemTypeLayout type);
  }
}

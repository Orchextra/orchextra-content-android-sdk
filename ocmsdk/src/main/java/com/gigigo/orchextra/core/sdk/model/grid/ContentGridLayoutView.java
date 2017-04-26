package com.gigigo.orchextra.core.sdk.model.grid;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.gigigo.multiplegridrecyclerview.entities.Cell;
import com.gigigo.orchextra.core.controller.model.grid.ContentView;
import com.gigigo.orchextra.core.controller.model.grid.ContentViewPresenter;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItemTypeLayout;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.core.sdk.di.injector.Injector;
import com.gigigo.orchextra.core.sdk.model.detail.DetailActivity;
import com.gigigo.orchextra.core.sdk.model.grid.dto.ClipToPadding;
import com.gigigo.orchextra.core.sdk.model.grid.spangridrecyclerview.SpannedGridRecyclerView;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import com.gigigo.orchextra.ocm.views.UiListedBaseContentData;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;
import java.util.List;
import orchextra.javax.inject.Inject;

public class ContentGridLayoutView extends UiGridBaseContentData implements ContentView {

  @Inject ContentViewPresenter presenter;
  @Inject ImageLoader imageLoader;
  @Inject Authoritation authoritation;
  UiListedBaseContentData.ListedContentListener listedContentListener =
      new UiListedBaseContentData.ListedContentListener() {
        @Override public void reloadSection() {
          if (presenter != null) {
            presenter.reloadSection();
          }
        }

        @Override public void onItemClicked(int position, View view) {
          if (presenter != null) {
            presenter.onItemClicked(position, (AppCompatActivity) getActivity(), view);
          }
        }
      };
  private FragmentManager fragmentManager;
  private SpannedGridRecyclerView uiListedBaseContentData;
  private ClipToPadding clipToPadding = ClipToPadding.PADDING_NONE;
  private Context context;
  private View retryButton;
  private View moreButton;
  private String viewId;
  private String emotion;
  private View emptyView;
  private View errorView;
  private View progressView;
  private View appEmptyView;
  private View appErrorView;
  private View.OnClickListener onClickDiscoverMoreButtonListener = new View.OnClickListener() {
    @Override public void onClick(View v) {
      if (onLoadMoreContentListener != null) {
        onLoadMoreContentListener.onLoadMoreContent();
      }
    }
  };
  private View.OnClickListener onClickRetryButtonListener = new View.OnClickListener() {
    @Override public void onClick(View v) {
      if (presenter != null) {
        presenter.reloadSection();
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

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.ocm_content_grid_layout_view, container, false);

    initDI();

    initView(view);
    setListeners();
    presenter.attachView(this);

    return view;
  }

  private void initDI() {
    Injector injector = OCManager.getInjector();
    if (injector != null) {
      injector.injectContentGridLayoutView(this);
    }
  }

  private void initView(View view) {
    emptyView = view.findViewById(R.id.ocm_empty_layout);
    errorView = view.findViewById(R.id.ocm_error_layout);
    retryButton = view.findViewById(R.id.ocm_retry_button);
    moreButton = view.findViewById(R.id.ocm_more_button);
  }

  private void setListeners() {
    retryButton.setOnClickListener(onClickRetryButtonListener);
    moreButton.setOnClickListener(onClickDiscoverMoreButtonListener);
  }

  private void setEmptyViewLayout(View emptyView) {
    uiListedBaseContentData.setEmptyViewLayout(emptyView);
  }

  private void setErrorViewLayout(View errorView) {
    uiListedBaseContentData.setErrorViewLayout(errorView);
  }

  private void setLoadingViewLayout() {
    if (progressView != null) {
      uiListedBaseContentData.setLoadingViewLayout(progressView);
    }
  }

  public void setViewId(String viewId) {
    this.viewId = viewId;
  }

  @Override public void initUi() {
    if (viewId != null) {
      presenter.setPadding(clipToPadding.getPadding());
      presenter.loadSection(viewId, emotion);
    }
  }

  @Override public void setData(List<Cell> cellDataList, ContentItemTypeLayout type) {
    switch (type) {
      case GRID:
        setDataGrid(cellDataList);
        break;
      case CAROUSEL:
    }
  }

  private void setDataGrid(List<Cell> cellDataList) {
    if (uiListedBaseContentData == null) {

      uiListedBaseContentData = SpannedGridRecyclerView.newInstance();

      uiListedBaseContentData.setClipToPadding(clipToPadding);
      uiListedBaseContentData.setImageLoader(imageLoader);
      uiListedBaseContentData.setAuthoritation(authoritation);
      setEmptyViewLayout(appEmptyView != null ? appEmptyView : emptyView);
      setErrorViewLayout(appErrorView != null ? appErrorView : errorView);
      setLoadingViewLayout();
      if (onScrollListener != null) {
        uiListedBaseContentData.setOnScrollListener(onScrollListener);
      }
      uiListedBaseContentData.setListedContentListener(listedContentListener);

      fragmentManager.beginTransaction()
          .replace(R.id.listedDataContainer, uiListedBaseContentData)
          .commit();
    }
    uiListedBaseContentData.setData(cellDataList);
  }

  @Override public void showEmptyView() {
    uiListedBaseContentData.showEmptyView();
  }

  @Override public void showErrorView() {
    uiListedBaseContentData.showErrorView();
  }

  @Override public void navigateToDetailView(String elementUrl, String urlImageToExpand,
      AppCompatActivity activity, View view) {

    ImageView imageViewToExpandInDetail =
        (ImageView) view.findViewById(R.id.image_to_expand_in_detail);

    if (urlImageToExpand != null) {
      String imageUrl = ImageGenerator.generateImageUrl(urlImageToExpand,
          DeviceUtils.calculateRealWidthDevice(context),
          DeviceUtils.calculateRealHeightDevice(context));

      imageLoader.load(imageUrl).into(imageViewToExpandInDetail);
    }

    DetailActivity.open(activity, elementUrl, urlImageToExpand,
        DeviceUtils.calculateRealWidthDevice(context),
        DeviceUtils.calculateRealHeightDevice(context), imageViewToExpandInDetail);
  }

  @Override public void showAuthDialog() {
    OCManager.notifyRequiredLoginToContinue();
  }

  @Override public void showProgressView(boolean isVisible) {
    if (uiListedBaseContentData == null) {
      progressView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    } else {
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

  @Override public void setClipToPaddingBottomSize(ClipToPadding clipToPadding) {
    this.clipToPadding = clipToPadding;
  }

  @Override public void scrollToTop() {
    if (uiListedBaseContentData != null) {
      uiListedBaseContentData.scrollToTop();
    }
  }

  @Override public void setEmptyView(View emptyView) {
    this.appEmptyView = emptyView;
  }

  public void setErrorView(View errorView) {
    this.appErrorView = errorView;
  }

  @Override public void setProgressView(View progressView) {
    this.progressView = progressView;
  }

  @Override public void reloadSection() {
    presenter.reloadSection();
  }

  public void setEmotion(String emotion) {
    this.emotion = emotion;
  }
}

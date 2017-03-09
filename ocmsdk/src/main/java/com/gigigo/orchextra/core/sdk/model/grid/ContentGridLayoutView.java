package com.gigigo.orchextra.core.sdk.model.grid;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.multiplegridrecyclerview.MultipleGridRecyclerView;
import com.gigigo.multiplegridrecyclerview.entities.Cell;
import com.gigigo.multiplegridrecyclerview.entities.CellBlankElement;
import com.gigigo.multiplegridrecyclerview.viewholder.CellBlankViewHolder;
import com.gigigo.orchextra.core.controller.model.grid.ContentView;
import com.gigigo.orchextra.core.controller.model.grid.ContentViewPresenter;
import com.gigigo.orchextra.core.domain.entities.elements.Element;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.core.sdk.di.injector.Injector;
import com.gigigo.orchextra.core.sdk.model.detail.DetailActivity;
import com.gigigo.orchextra.core.sdk.model.grid.dto.CellElementAdapter;
import com.gigigo.orchextra.core.sdk.model.grid.factory.ElementsViewHolderFactory;
import com.gigigo.orchextra.core.sdk.model.grid.viewholders.CellImageViewHolder;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.dto.BottomPadding;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;
import java.util.ArrayList;
import java.util.List;
import orchextra.javax.inject.Inject;

public class ContentGridLayoutView extends UiGridBaseContentData implements ContentView {

  @Inject ContentViewPresenter presenter;
  @Inject ImageLoader imageLoader;
  @Inject Authoritation authoritation;

  private Context context;
  private MultipleGridRecyclerView recyclerView;
  private View retryButton;
  private View moreButton;
  private String viewId;
  private String emotion;
  //private int clipToPaddingSize;
  private View emptyView;
  private View errorView;
  private View progressView;

  private View.OnClickListener onClickDiscoverMoreButtonListener = new View.OnClickListener() {
    @Override public void onClick(View v) {
      if (onLoadMoreContentListener != null) {
        onLoadMoreContentListener.onLoadMoreContent();
      }
    }
  };

  private View.OnClickListener onClickRetryButtonListener = new View.OnClickListener() {
    @Override public void onClick(View v) {
      presenter.reloadSection();
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

    initDI();

    initView(view);
    setListeners();
    initRecyclerView();

    return view;
  }

  @Override public void onResume() {
    super.onResume();

    presenter.attachView(this);
  }

  private void initDI() {
    Injector injector = OCManager.getInjector();
    if (injector != null) {
      injector.injectContentGridLayoutView(this);
    }
  }

  private void initView(View view) {
    recyclerView = (MultipleGridRecyclerView) view.findViewById(R.id.multiple_grid_recycler_view);
    View emptyView = view.findViewById(R.id.ocm_empty_layout);
    View errorView = view.findViewById(R.id.ocm_error_layout);
    retryButton = view.findViewById(R.id.ocm_retry_button);
    moreButton = view.findViewById(R.id.ocm_more_button);

    setEmptyViewLayout(this.emptyView != null ? this.emptyView : emptyView);
    setErrorViewLayout(this.errorView != null ? this.errorView : errorView);

    if (progressView != null) {
      recyclerView.setLoadingViewLayout(progressView);
    }
  }

  private void setListeners() {
    retryButton.setOnClickListener(onClickRetryButtonListener);
    moreButton.setOnClickListener(onClickDiscoverMoreButtonListener);
  }

  private void initRecyclerView() {
    setAdapterDataViewHolders();

    //TODO Resolve clip to padding flashing when last row is 3 items 1x1. Remove logic in presenter
    recyclerView.setGridColumns(3 * 2);

    recyclerView.setOnRefreshListener(new MultipleGridRecyclerView.OnRefreshListener() {
      @Override public void onRefresh() {
        if (presenter != null) {
          presenter.reloadSection();
        }
      }
    });

    recyclerView.setItemClickListener(new BaseViewHolder.OnItemClickListener() {
      @Override public void onItemClick(int position, View view) {
        presenter.onItemClicked(position, (AppCompatActivity) getActivity(), view);
      }
    });

    //TODO Resolve clip to padding flashing when last row is 3 items 1x1. Remove logic in presenter
    //if (clipToPaddingSize > 0) {
    //recyclerView.setClipToPaddingBottomSize(clipToPaddingSize);
    //}

    if (onScrollListener != null) {
      recyclerView.setOnScrollListener(onScrollListener);
    }
  }

  private void setAdapterDataViewHolders() {
    ElementsViewHolderFactory factory =
        new ElementsViewHolderFactory(context, imageLoader, authoritation);

    recyclerView.setAdapterViewHolderFactory(factory);

    recyclerView.setAdapterDataViewHolder(CellElementAdapter.class, CellImageViewHolder.class);
    recyclerView.setAdapterDataViewHolder(CellBlankElement.class, CellBlankViewHolder.class);

    recyclerView.setUndecoratedViewHolder(CellBlankViewHolder.class);
  }

  public void setEmptyViewLayout(View emptyView) {
    recyclerView.setEmptyViewLayout(emptyView);
  }

  public void setErrorViewLayout(View errorView) {
    recyclerView.setErrorViewLayout(errorView);
  }

  public void setLoadingViewLayout(View loadingView) {
    recyclerView.setLoadingViewLayout(loadingView);
  }

  public void setViewId(String viewId) {
    this.viewId = viewId;
  }

  @Override public void initUi() {
    if (viewId != null) {
      presenter.loadSection(viewId, true, emotion);
    }
  }

  @Override public void setData(List<Cell> cellGridContentDataList) {
    List<Cell> cellElementList = new ArrayList<>();

    for (Cell cellGridContentData : cellGridContentDataList) {
      Cell cellElement;
      if (cellGridContentData.getData() instanceof Element) {
        cellElement = new CellElementAdapter();
        cellElement.setRow(cellGridContentData.getRow());
        cellElement.setColumn(cellGridContentData.getColumn());
        cellElement.setData(cellGridContentData.getData());
      } else {
        cellElement = new CellBlankElement();
        cellElement.setRow(cellGridContentData.getRow());
        cellElement.setColumn(cellGridContentData.getColumn());
      }

      cellElementList.add(cellElement);
    }

    recyclerView.addAll(cellElementList);
  }

  @Override public void showEmptyView() {
    recyclerView.showEmptyView();
  }

  @Override public void showErrorView() {
    recyclerView.showErrorView();
  }

  @Override public void navigateToDetailView(String elementUrl, String urlImageToExpand,
      AppCompatActivity activity, View view) {

    ImageView imageViewToExpand = (ImageView) view.findViewById(R.id.expand_image_view);

    if (urlImageToExpand != null) {
      String imageUrl = ImageGenerator.generateImageUrl(urlImageToExpand,
          DeviceUtils.calculateRealWidthDevice(context),
          DeviceUtils.calculateRealHeightDevice(context));

      imageLoader.load(imageUrl).into(imageViewToExpand).build();
    }

    DetailActivity.open(activity, elementUrl, urlImageToExpand,
        DeviceUtils.calculateRealWidthDevice(context),
        DeviceUtils.calculateRealHeightDevice(context), imageViewToExpand);
  }

  @Override public void showAuthDialog() {
    OCManager.notifyRequiredLoginToContinue();
  }

  @Override public void showProgressView(boolean isVisible) {
    recyclerView.showLoadingView(isVisible);
  }

  @Override public void setFilter(String filter) {
    if (presenter != null) {
      presenter.setFilter(filter);
      if (recyclerView != null && recyclerView.getChildCount() > 0) {
        recyclerView.scrollToTop();
      }
    }
  }

  @Override public void setClipToPaddingSize(int clipToPaddingSize) {
    //this.clipToPaddingSize = clipToPaddingSize;
  }

  @Override public void scrollToTop() {
    if (recyclerView != null) {
      recyclerView.scrollToTop();
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

  @Override public void reloadSection() {
    presenter.reloadSection();
  }

  public void setEmotion(String emotion) {
    this.emotion = emotion;
  }
}

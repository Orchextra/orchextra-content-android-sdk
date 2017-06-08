package com.gigigo.orchextra.core.sdk.model.searcher;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.multiplegridrecyclerview.MultipleGridRecyclerView;
import com.gigigo.multiplegridrecyclerview.entities.Cell;
import com.gigigo.multiplegridrecyclerview.entities.CellBlankElement;
import com.gigigo.multiplegridrecyclerview.viewholder.CellBlankViewHolder;
import com.gigigo.orchextra.core.controller.dto.CellGridContentData;
import com.gigigo.orchextra.core.controller.model.searcher.SearcherLayoutInterface;
import com.gigigo.orchextra.core.controller.model.searcher.SearcherLayoutPresenter;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.core.sdk.di.injector.Injector;
import com.gigigo.orchextra.core.sdk.model.detail.DetailActivity;
import com.gigigo.orchextra.core.sdk.model.grid.factory.ElementsViewHolderFactory;
import com.gigigo.orchextra.core.sdk.model.grid.viewholders.CellImageViewHolder;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.views.UiSearchBaseContentData;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;
import java.util.List;
import orchextra.javax.inject.Inject;

public class SearcherLayoutView extends UiSearchBaseContentData implements SearcherLayoutInterface {

  @Inject SearcherLayoutPresenter presenter;
  @Inject ImageLoader imageLoader;
  @Inject Authoritation authoritation;
  @Inject OcmController ocmController;

  private Context context;

  private MultipleGridRecyclerView recyclerView;
  private View emptyLayout;
  private View progressLayout;

  public static SearcherLayoutView newInstance() {
    return new SearcherLayoutView();
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    this.context = context;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_searcher_layout, container, false);

    initDI();
    findViews(view);

    return view;
  }

  private void initDI() {
    Injector injector = OCManager.getInjector();
    if (injector != null) {
      injector.injectSearcherLayoutView(this);
    }
  }

  private void findViews(View view) {
    recyclerView = (MultipleGridRecyclerView) view.findViewById(R.id.searcher_recycler_view);

    if (emptyLayout == null) {
      emptyLayout = view.findViewById(R.id.ocm_empty_layout);
    }

    if (progressLayout == null) {
      progressLayout = view.findViewById(R.id.loadingSearchProgressbar);
    }
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    presenter.attachView(this);
  }

  @Override public void initUi() {
    setMultipleGridLayout();
  }

  private void setMultipleGridLayout() {
    recyclerView.setLoadingViewLayout(progressLayout);
    recyclerView.setEmptyViewLayout(emptyLayout);

    setAdapterDataViewHolders();

    recyclerView.setOnRefreshListener(new MultipleGridRecyclerView.OnRefreshListener() {
      @Override public void onRefresh() {
        if (presenter != null) {
          recyclerView.showLoadingView(true);
          presenter.doSearch();
        }
      }
    });

    recyclerView.setItemClickListener(new BaseViewHolder.OnItemClickListener() {
      @Override public void onItemClick(int position, View view) {
        presenter.onItemClicked(position, (AppCompatActivity) getActivity(), view);
      }
    });
  }

  private void setAdapterDataViewHolders() {
    ElementsViewHolderFactory factory = new ElementsViewHolderFactory(context, authoritation);

    recyclerView.setAdapterViewHolderFactory(factory);

    recyclerView.setAdapterDataViewHolder(CellGridContentData.class, CellImageViewHolder.class);
    recyclerView.setAdapterDataViewHolder(CellBlankElement.class, CellBlankViewHolder.class);

    recyclerView.setUndecoratedViewHolder(CellBlankViewHolder.class);
  }

  @Override public void showProgressView(boolean isVisible) {
    recyclerView.showLoadingView(isVisible);
  }

  @Override public void showEmptyView() {
    recyclerView.showEmptyView();
  }

  @Override public void hideEmptyView() {
    emptyLayout.setVisibility(View.GONE);
  }

  @Override public void setData(List<Cell> cellGridContentDataList) {
    recyclerView.addAll(cellGridContentDataList);
    recyclerView.setVisibility(View.VISIBLE);
    recyclerView.showRecyclerView();
  }

  @Override public void doSearch(String textToSearch) {
    if (presenter != null) {
      presenter.doSearch(textToSearch);
    }
  }

  @Override public void setEmptyView(View emptyView) {
    this.emptyLayout = emptyView;
  }

  @Override public void setProgressView(View progressLayout) {
    this.progressLayout = progressLayout;
  }

  @Override public void onResume() {
    super.onResume();

    presenter.updateUi();
  }

  @Override public void onDestroy() {
    presenter.detachView();

    super.onDestroy();
  }

  @Override public void navigateToDetailView(String elementUrl, String urlImageToExpand,
      AppCompatActivity activity, View view) {

    ImageView imageViewToExpand = (ImageView) view.findViewById(R.id.image_to_expand_in_detail);

    String imageUrl = null;
    if (urlImageToExpand != null) {
      imageUrl = ImageGenerator.generateImageUrl(urlImageToExpand,
          DeviceUtils.calculateRealWidthDevice(context),
          DeviceUtils.calculateRealHeightDevice(context));

      Glide.with(getContext()).load(imageUrl).priority(Priority.NORMAL).into(imageViewToExpand);
    }

    DetailActivity.open(activity, elementUrl, imageUrl,
        DeviceUtils.calculateRealWidthDevice(context),
        DeviceUtils.calculateRealHeightDevice(context), imageViewToExpand);
  }

  @Override public void showAuthDialog() {
    OCManager.notifyRequiredLoginToContinue();
  }
}


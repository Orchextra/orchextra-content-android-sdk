package com.gigigo.orchextra.core.sdk.model.searcher;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.multiplegridrecyclerview.MultipleGridRecyclerView;
import com.gigigo.multiplegridrecyclerview.entities.Cell;
import com.gigigo.multiplegridrecyclerview.entities.CellBlankElement;
import com.gigigo.multiplegridrecyclerview.viewholder.CellBlankViewHolder;
import com.gigigo.orchextra.core.controller.dto.CellGridContentData;
import com.gigigo.orchextra.core.controller.model.searcher.SearcherLayoutInterface;
import com.gigigo.orchextra.core.controller.model.searcher.SearcherLayoutPresenter;
import com.gigigo.orchextra.core.sdk.di.injector.Injector;
import com.gigigo.orchextra.core.sdk.model.grid.factory.ElementsViewHolderFactory;
import com.gigigo.orchextra.core.sdk.model.grid.viewholders.CellImageViewHolder;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.views.UiSearchBaseContentData;
import com.gigigo.orchextra.ocmsdk.R;
import java.util.List;
import orchextra.javax.inject.Inject;

public class SearcherLayoutView extends UiSearchBaseContentData implements SearcherLayoutInterface {

  @Inject SearcherLayoutPresenter presenter;

  private Context context;

  private MultipleGridRecyclerView recyclerView;
  private View emptyLayout;
  private View progressLayout;
  private boolean thumbnailEnabled;
  private String lastSearch = "";

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
      thumbnailEnabled = injector.provideOcmStyleUi().isThumbnailEnabled();
    }
  }

  private void findViews(View view) {
    recyclerView = (MultipleGridRecyclerView) view.findViewById(R.id.searcher_recycler_view);

    if (emptyLayout == null) {
      emptyLayout = view.findViewById(R.id.search_empty_layout);
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

    recyclerView.setOnRefreshListener(() -> {
      if (presenter != null) {
        recyclerView.showLoadingView(true);
        presenter.doSearch(lastSearch);
      }
    });

    recyclerView.setItemClickListener(
        (position, view) -> presenter.onItemClicked(position, (AppCompatActivity) getActivity(),
            view));
  }

  private void setAdapterDataViewHolders() {
    ElementsViewHolderFactory factory = new ElementsViewHolderFactory(context, thumbnailEnabled);

    recyclerView.setAdapterViewHolderFactory(factory);

    recyclerView.setAdapterDataViewHolder(CellGridContentData.class, CellImageViewHolder.class);
    recyclerView.setAdapterDataViewHolder(CellBlankElement.class, CellBlankViewHolder.class);

    recyclerView.setUndecoratedViewHolder(CellBlankViewHolder.class);
    recyclerView.overrideScollingVelocityY(0.4f);
  }

  @Override public void showProgressView(boolean isVisible) {
    recyclerView.showLoadingView(isVisible);
  }

  @Override public void showEmptyView(boolean isVisible) {
    emptyLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);
  }

  @Override public void contentNotAvailable() {
    //Snackbar.make(listedDataContainer, R.string.oc_error_content_not_available_without_internet, Snackbar.LENGTH_SHORT).show();
  }

  @Override public void setData(List<Cell> cellGridContentDataList) {
    recyclerView.addAll(cellGridContentDataList);
    recyclerView.setVisibility(View.VISIBLE);
    recyclerView.showRecyclerView();
  }

  @Override public void doSearch(String textToSearch) {
    this.lastSearch = textToSearch;
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

  @Override public void onDestroy() {
    if (presenter != null) {
      presenter.destroy();
      presenter.detachView();
    }

    super.onDestroy();
  }
}
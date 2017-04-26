package com.gigigo.orchextra.core.sdk.model.grid.spannedgridrecyclerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.multiplegridrecyclerview.MultipleGridRecyclerView;
import com.gigigo.multiplegridrecyclerview.entities.Cell;
import com.gigigo.multiplegridrecyclerview.entities.CellBlankElement;
import com.gigigo.multiplegridrecyclerview.viewholder.CellBlankViewHolder;
import com.gigigo.orchextra.core.controller.dto.CellGridContentData;
import com.gigigo.orchextra.core.sdk.model.grid.dto.ClipToPadding;
import com.gigigo.orchextra.core.sdk.model.grid.factory.ElementsViewHolderFactory;
import com.gigigo.orchextra.core.sdk.model.grid.viewholders.CellImageViewHolder;
import com.gigigo.orchextra.ocm.views.UiListedBaseContentData;
import com.gigigo.orchextra.ocmsdk.R;
import java.util.List;

public class SpannedGridRecyclerView extends UiListedBaseContentData {

  private MultipleGridRecyclerView multipleGridRecyclerView;

  private List<Cell> cellDataList;

  public static SpannedGridRecyclerView newInstance() {
    return new SpannedGridRecyclerView();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.view_spanned_grid_recycler_item, container, false);

    initViews(view);
    initRecyclerView();

    return view;
  }

  private void initViews(View view) {
    multipleGridRecyclerView =
        (MultipleGridRecyclerView) view.findViewById(R.id.multipleGridRecyclerView);
  }

  private void initRecyclerView() {
    setAdapterDataViewHolders();

    //TODO Resolve clip to padding flashing when last row is 3 items 1x1. Remove logic in presenter
    int padding = clipToPadding.getPadding();
    multipleGridRecyclerView.setGridColumns(
        clipToPadding == ClipToPadding.PADDING_NONE ? 3 : 3 * padding);

    multipleGridRecyclerView.setOnRefreshListener(new MultipleGridRecyclerView.OnRefreshListener() {
      @Override public void onRefresh() {
        if (listedContentListener != null) {
          listedContentListener.reloadSection();
        }
      }
    });

    multipleGridRecyclerView.setItemClickListener(new BaseViewHolder.OnItemClickListener() {
      @Override public void onItemClick(int position, View view) {
        if (listedContentListener != null) {
          listedContentListener.onItemClicked(position, view);
        }
      }
    });

    multipleGridRecyclerView.setEmptyViewLayout(emptyView);
    multipleGridRecyclerView.setErrorViewLayout(errorView);
    multipleGridRecyclerView.setLoadingViewLayout(loadingView);
  }

  private void setAdapterDataViewHolders() {
    ElementsViewHolderFactory factory =
        new ElementsViewHolderFactory(getContext(), imageLoader, authoritation);

    multipleGridRecyclerView.setAdapterViewHolderFactory(factory);

    multipleGridRecyclerView.setAdapterDataViewHolder(CellGridContentData.class,
        CellImageViewHolder.class);
    multipleGridRecyclerView.setAdapterDataViewHolder(CellBlankElement.class,
        CellBlankViewHolder.class);

    multipleGridRecyclerView.setUndecoratedViewHolder(CellBlankViewHolder.class);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    multipleGridRecyclerView.addAll(cellDataList);
  }

  @Override public void setData(List<Cell> cellDataList) {
    this.cellDataList = cellDataList;
    if (multipleGridRecyclerView != null) {
      multipleGridRecyclerView.addAll(cellDataList);
    }
  }

  @Override public void scrollToTop() {
    if (multipleGridRecyclerView != null) {
      multipleGridRecyclerView.scrollToTop();
    }
  }

  @Override public void showErrorView() {
    if (multipleGridRecyclerView != null) {
      multipleGridRecyclerView.showErrorView();
    }
  }

  @Override public void showEmptyView() {
    if (multipleGridRecyclerView != null) {
      multipleGridRecyclerView.showEmptyView();
    }
  }

  @Override public void showProgressView(boolean isVisible) {
    if (multipleGridRecyclerView != null) {
      multipleGridRecyclerView.showLoadingView(isVisible);
    }
  }
}

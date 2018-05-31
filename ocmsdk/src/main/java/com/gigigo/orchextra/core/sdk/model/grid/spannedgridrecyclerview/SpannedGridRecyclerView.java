package com.gigigo.orchextra.core.sdk.model.grid.spannedgridrecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
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
  private View testview;

  public SpannedGridRecyclerView(Context context) {
    super(context);
  }

  public SpannedGridRecyclerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public SpannedGridRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void init() {
    View view = inflateLayout();
    initViews(view);
    initRecyclerView();
  }

  private View inflateLayout() {
    LayoutInflater inflater =
        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    return inflater.inflate(R.layout.view_spanned_grid_recycler_item, this, true);
  }

  private void initViews(View view) {
    multipleGridRecyclerView =
        (MultipleGridRecyclerView) view.findViewById(R.id.multipleGridRecyclerView);

    testview = view.findViewById(R.id.testview);
  }

  private void initRecyclerView() {
    setAdapterDataViewHolders();

    //TODO Resolve clip to padding flashing when last row is 3 items 1x1. Remove logic in presenter
    int padding = (clipToPadding != null) ? clipToPadding.getPadding()
        : ClipToPadding.PADDING_NONE.getPadding();

    multipleGridRecyclerView.setGridColumns(
        clipToPadding == ClipToPadding.PADDING_NONE ? 3 : 3 * padding);

    multipleGridRecyclerView.setMillis(1500);

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

    multipleGridRecyclerView.overrideScollingVelocityY(0.4f);
    multipleGridRecyclerView.setClipToPaddingSize(addictionalPadding);

    multipleGridRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
      static final int SCROLL_DIRECTION_UP = -1;

      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (!recyclerView.canScrollVertically(SCROLL_DIRECTION_UP)) {
          testview.setVisibility(VISIBLE);
          // TODO: 31/5/18 mostrar el botón de scroll
        } else {
          testview.setVisibility(GONE);
        }
      }
    });
  }

  private void setAdapterDataViewHolders() {
    ElementsViewHolderFactory factory =
        new ElementsViewHolderFactory(getContext(), thumbnailEnabled);

    multipleGridRecyclerView.setAdapterViewHolderFactory(factory);

    multipleGridRecyclerView.setAdapterDataViewHolder(CellGridContentData.class,
        CellImageViewHolder.class);
    multipleGridRecyclerView.setAdapterDataViewHolder(CellBlankElement.class,
        CellBlankViewHolder.class);

    multipleGridRecyclerView.setUndecoratedViewHolder(CellBlankViewHolder.class);
  }

  @Override public void setData(List<Cell> cellDataList) {
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

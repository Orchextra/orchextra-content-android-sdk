package com.gigigo.orchextra.core.sdk.model.grid.verticalviewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.gigigo.multiplegridrecyclerview.entities.Cell;
import com.gigigo.orchextra.core.controller.dto.CellCarouselContentData;
import com.gigigo.orchextra.ocm.views.UiListedBaseContentData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VerticalViewPagerAdapter extends FragmentStatePagerAdapter {

  private final UiListedBaseContentData.ListedContentListener listedContentListener;
  private List<Cell> cellDataList = new ArrayList<>();

  public VerticalViewPagerAdapter(FragmentManager fm,
      UiListedBaseContentData.ListedContentListener listedContentListener) {
    super(fm);
    this.listedContentListener = listedContentListener;
  }

  @Override public Fragment getItem(final int position) {

    VerticalItemPageFragment verticalItemPageFragment = VerticalItemPageFragment.newInstance();
    verticalItemPageFragment.setOnClickItem(view -> {
      if (listedContentListener != null) {
        listedContentListener.onItemClicked(position, view);
      }
    });

    if (cellDataList.get(position) instanceof CellCarouselContentData) {
      CellCarouselContentData cell = (CellCarouselContentData) cellDataList.get(position);
      verticalItemPageFragment.setCell(cell);
    }

    return verticalItemPageFragment;
  }

  @Override public int getCount() {
    return cellDataList.size();
  }

  public void setItems(Collection<Cell> cellDataList) {
    this.cellDataList.clear();
    this.cellDataList.addAll(cellDataList);
    notifyDataSetChanged();
  }
}

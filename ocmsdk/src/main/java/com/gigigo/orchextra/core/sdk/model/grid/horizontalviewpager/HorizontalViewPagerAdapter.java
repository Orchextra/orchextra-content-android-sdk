package com.gigigo.orchextra.core.sdk.model.grid.horizontalviewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.gigigo.multiplegridrecyclerview.entities.Cell;
import com.gigigo.orchextra.core.controller.dto.CellCarouselContentData;
import com.gigigo.ui.imageloader.ImageLoader;
import java.util.List;

public class HorizontalViewPagerAdapter extends FragmentStatePagerAdapter {

  private final ImageLoader imageLoader;
  private List<Cell> cellDataList;

  public HorizontalViewPagerAdapter(FragmentManager fm, ImageLoader imageLoader) {
    super(fm);
    this.imageLoader = imageLoader;
  }

  public HorizontalViewPagerAdapter(FragmentManager fm, ImageLoader imageLoader, List<Cell> cellDataList) {
    super(fm);
    this.cellDataList = cellDataList;
    this.imageLoader = imageLoader;
  }

  @Override public Fragment getItem(int position) {
    CellCarouselContentData cell = (CellCarouselContentData) cellDataList.get(position);
    HorizontalItemPageFragment horizontalItemPageFragment =
        HorizontalItemPageFragment.newInstance();
    horizontalItemPageFragment.setImageLoader(imageLoader);
    horizontalItemPageFragment.setCell(cell);

    return horizontalItemPageFragment;
  }

  @Override public int getCount() {
    return cellDataList != null ? cellDataList.size() : 0;
  }

  public void setItems(List<Cell> cellDataList) {
    this.cellDataList = cellDataList;
    notifyDataSetChanged();
  }
}

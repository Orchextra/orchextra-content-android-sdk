package com.gigigo.orchextra.core.sdk.model.grid.horizontalviewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import com.gigigo.multiplegridrecyclerview.entities.Cell;
import com.gigigo.orchextra.core.controller.dto.CellCarouselContentData;
import com.gigigo.orchextra.ocm.views.UiListedBaseContentData;
import com.gigigo.ui.imageloader.ImageLoader;
import java.util.List;

public class HorizontalViewPagerAdapter extends FragmentStatePagerAdapter {

  private final ImageLoader imageLoader;
  private final UiListedBaseContentData.ListedContentListener listedContentListener;
  private List<Cell> cellDataList;

  public HorizontalViewPagerAdapter(FragmentManager fm, ImageLoader imageLoader, UiListedBaseContentData.ListedContentListener listedContentListener) {
    super(fm);
    this.imageLoader = imageLoader;
    this.listedContentListener = listedContentListener;
  }

  @Override public Fragment getItem(final int position) {
    CellCarouselContentData cell = (CellCarouselContentData) cellDataList.get(position);
    HorizontalItemPageFragment horizontalItemPageFragment =
        HorizontalItemPageFragment.newInstance();
    horizontalItemPageFragment.setImageLoader(imageLoader);
    horizontalItemPageFragment.setOnItemClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (listedContentListener != null) {
          listedContentListener.onItemClicked(position, v);
        }
      }
    });
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

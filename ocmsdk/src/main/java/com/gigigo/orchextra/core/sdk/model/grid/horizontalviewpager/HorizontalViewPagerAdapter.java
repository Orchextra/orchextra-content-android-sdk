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
  int mLoops = 1;

  public HorizontalViewPagerAdapter(FragmentManager fm, ImageLoader imageLoader,
      UiListedBaseContentData.ListedContentListener listedContentListener) {
    super(fm);
    this.imageLoader = imageLoader;
    this.listedContentListener = listedContentListener;
  }

  @Override public Fragment getItem(int position) {

    int realSize = 1;
    if (getCount() > 0) realSize = getCount() / mLoops;

    if (mLoops > 1) position = position % realSize;
    final int finalPosition = position;
    CellCarouselContentData cell = (CellCarouselContentData) cellDataList.get(finalPosition);
    HorizontalItemPageFragment horizontalItemPageFragment =
        HorizontalItemPageFragment.newInstance();
    horizontalItemPageFragment.setImageLoader(imageLoader);
    horizontalItemPageFragment.setOnClickHorizontalItem(
        new HorizontalItemPageFragment.OnClickHorizontalItem() {
          @Override public void onClickItem(View view) {
            if (listedContentListener != null) {
              listedContentListener.onItemClicked(finalPosition, view);
            }
          }
        });
    horizontalItemPageFragment.setCell(cell);

    return horizontalItemPageFragment;
  }

  @Override public int getCount() {
    return cellDataList != null ? cellDataList.size() * mLoops : 0;
  }

  public void setItems(List<Cell> cellDataList) {
    this.cellDataList = cellDataList;
    notifyDataSetChanged();
  }

  public void setLoops(int mLoops) {
    this.mLoops = mLoops;
    notifyDataSetChanged();
  }
}

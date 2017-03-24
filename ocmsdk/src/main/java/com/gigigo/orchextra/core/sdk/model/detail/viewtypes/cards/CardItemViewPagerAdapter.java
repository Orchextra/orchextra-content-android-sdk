package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import java.util.ArrayList;
import java.util.List;

public class CardItemViewPagerAdapter extends FragmentStatePagerAdapter {

  private List<UiBaseContentData> items = new ArrayList<>();

  public CardItemViewPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override public Fragment getItem(int position) {
    UiBaseContentData uiBaseContentData = items.get(position);

    if (uiBaseContentData instanceof PreviewCardContentData) {
      ((PreviewCardContentData) uiBaseContentData).notifyDataChanged();
    }

    return uiBaseContentData;
  }

  @Override public int getCount() {
    return items.size();
  }

  public void setFragments(List<UiBaseContentData> fragments) {
    items.addAll(fragments);
    notifyDataSetChanged();
  }
}

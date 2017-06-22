package com.gigigo.sample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import java.util.List;

public class ScreenSlidePagerAdapter extends FragmentPagerAdapter {

  private List<UiMenu> menuContent;

  public ScreenSlidePagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override public Fragment getItem(int position) {
    UiMenu menu = menuContent.get(position);

    return ScreenSlidePageFragment.newInstance(menu.getElementUrl());
  }

  public void setDataItems(List<UiMenu> menuContent) {
    this.menuContent = menuContent;
    notifyDataSetChanged();
  }

  @Override public int getItemPosition(Object object) {
    return super.getItemPosition(object);
  }

  @Override public int getCount() {
    return (menuContent != null) ? menuContent.size() : 0;
  }
}
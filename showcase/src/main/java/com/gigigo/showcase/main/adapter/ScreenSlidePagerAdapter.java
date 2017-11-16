package com.gigigo.showcase.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.showcase.main.ScreenSlidePageFragment;
import java.util.List;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

  private List<UiMenu> menuContent;

  public ScreenSlidePagerAdapter(FragmentManager fm, List<UiMenu> menus) {
    super(fm);
    menuContent = menus;
  }

  @Override public Fragment getItem(int position) {
    UiMenu menu = menuContent.get(position);

    return ScreenSlidePageFragment.newInstance(menu.getElementUrl(),
        getNumberOfImagesToDownload(position));
  }

  @Override public int getCount() {
    return menuContent.size();
  }

  private int getNumberOfImagesToDownload(int position) {
    int number = 12;
    if (position == 0) {
      number = 12;
    } else if (position == 1 || position == 2) number = 6;
    return number;
  }
}
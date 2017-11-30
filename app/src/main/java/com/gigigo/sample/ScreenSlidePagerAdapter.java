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

    return ScreenSlidePageFragment.newInstance(menu.getElementCache(),
        getNumberOfImagesToDownload(position));
  }

  public void setDataItems(List<UiMenu> menuContent) {
    this.menuContent = menuContent;
    notifyDataSetChanged();
  }

  @Override public int getItemPosition(Object object) {
    if (object instanceof ScreenSlidePageFragment) {
      ((ScreenSlidePageFragment) object).reloadSection();
    }
    return super.getItemPosition(object);
  }

  @Override public int getCount() {
    return (menuContent != null) ? menuContent.size() : 0;
  }

  public void reloadSections() {
    notifyDataSetChanged();
  }

  private int getNumberOfImagesToDownload(int position) {
    int number = 0;
    if (position == 0) {
      number = 12;
    } else if (position == 1 || position == 2) number = 6;
    return number;
  }
}
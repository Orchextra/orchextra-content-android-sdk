package com.gigigo.orchextra.ocm.sample;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

  private List<UiMenu> menuContent;
  private List<ScreenSlidePageFragment> fragments;

  public ScreenSlidePagerAdapter(FragmentManager fm) {
    super(fm);
    menuContent = new ArrayList<>();
    fragments = new ArrayList<>();
  }

  @Override public Fragment getItem(int position) {
    UiMenu menu = menuContent.get(position);
    ScreenSlidePageFragment screenSlidePageFragment = ScreenSlidePageFragment.newInstance();
    screenSlidePageFragment.setItemMenu(menu);
    screenSlidePageFragment.setNumberOfImagesToDownload(getNumberOfImagesToDownload(position));
    fragments.add(screenSlidePageFragment);

    return screenSlidePageFragment;
  }

  public void setDataItems(Collection<UiMenu> collection) {
    menuContent.clear();
    menuContent.addAll(collection);
    notifyDataSetChanged();
    updateFragmentData(collection);
  }

  private void updateFragmentData(Collection<UiMenu> collection) {
    for (UiMenu uiMenu : collection) {
      if (uiMenu.hasNewVersion()) {
        for (ScreenSlidePageFragment fragment : fragments) {
          if (fragment.getSlug().equals(uiMenu.getSlug())) {
            fragment.showNewVersionButton();
          }
        }
      }
    }
  }

  @Override public int getItemPosition(@NonNull Object object) {

    if (object instanceof ScreenSlidePageFragment) {
      String slug = ((ScreenSlidePageFragment) object).getSlug();
      for (int i = 0; i < menuContent.size(); i++) {
        if (menuContent.get(i).getSlug().equals(slug)) {
          return i;
        }
      }
    }
    return POSITION_NONE;
  }

  @Override public int getCount() {
    return menuContent.size();
  }

  public void reloadSections(int currentItem) {
    if (currentItem < fragments.size()) {
      fragments.get(currentItem).reloadSection(false);
    }
  }

  private int getNumberOfImagesToDownload(int position) {
    int number = 0;
    if (position == 0) {
      number = 12;
    } else if (position == 1 || position == 2) number = 6;
    return number;
  }
}
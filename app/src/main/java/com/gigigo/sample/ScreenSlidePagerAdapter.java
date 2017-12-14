package com.gigigo.sample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import java.util.ArrayList;
import java.util.List;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

  private final FragmentManager fm;
  private List<UiMenu> menuContent;
  private List<ScreenSlidePageFragment> fragments = new ArrayList<>();
  private String emotion;

  public ScreenSlidePagerAdapter(FragmentManager fm) {
    super(fm);
    this.fm = fm;
    menuContent = new ArrayList<>();
  }

  @Override public Fragment getItem(int position) {
    UiMenu menu = menuContent.get(position);
    ScreenSlidePageFragment screenSlidePageFragment = ScreenSlidePageFragment.newInstance();
    screenSlidePageFragment.setItemMenu(menu);
    screenSlidePageFragment.setEmotion(emotion);
    screenSlidePageFragment.setNumberOfImagesToDownload(getNumberOfImagesToDownload(position));

    if (position < fragments.size()) {
      fragments.set(position, screenSlidePageFragment);
    } else {
      fragments.add(screenSlidePageFragment);
    }

    return screenSlidePageFragment;
  }

  public void setEmotion(String emotion){
    try {
      if (!emotion.equals(this.emotion)) {
        this.emotion = emotion;
        notifyDataSetChanged();
      }
    } catch (Exception ignored) {
    }
  }
  public void setDataItems(List<UiMenu> menuContent) {
    if (fm != null && !fm.isDestroyed() && menuContent != null && menuContent.size() > 0) {
      fragments = new ArrayList<>(menuContent.size());
      this.menuContent = menuContent;
      notifyDataSetChanged();
    }
  }

  @Override public int getItemPosition(Object object) {
    //if (object instanceof ScreenSlidePageFragment) {
    //  ((ScreenSlidePageFragment) object).updateEmotion(emotion);
    //}
    return POSITION_NONE;
  }

  @Override public int getCount() {
    return (menuContent != null) ? menuContent.size() : 0;
  }

  public void reloadSections() {
    for (ScreenSlidePageFragment fragment : fragments) {
      fragment.reloadSection(false);
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
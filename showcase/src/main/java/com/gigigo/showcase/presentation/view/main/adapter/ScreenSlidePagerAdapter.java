package com.gigigo.showcase.presentation.view.main.adapter;

import android.os.Parcelable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import com.gigigo.orchextra.ocm.callbacks.OnLoadMoreContentListener;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

  private final FragmentManager fm;
  private List<UiMenu> menuContent;
  private String emotion = "";
  private OnLoadMoreContentListener onLoadMoreContentListener;
  private List<ScreenSlidePageFragment> fragments = new ArrayList<>();
  private final ScreenSlidePageFragment.EmptyContentCallback emptyContentCallback;

  public ScreenSlidePagerAdapter(FragmentManager fm,
      ScreenSlidePageFragment.EmptyContentCallback emptyContentCallback) {
    super(fm);
    this.fm = fm;
    this.emptyContentCallback = emptyContentCallback;
    menuContent = new ArrayList<>();
  }

  @Override public Fragment getItem(int position) {
    UiMenu menu = menuContent.get(position);

    ScreenSlidePageFragment screenSlidePageFragment = ScreenSlidePageFragment.newInstance();
    screenSlidePageFragment.setItemMenu(menu);
    screenSlidePageFragment.setEmotion(emotion);
    screenSlidePageFragment.setNumberOfImagesToDownload(getNumberOfImagesToDownload(position));
    screenSlidePageFragment.setEmptyContentCallback(emptyContentCallback);

    if (position < fragments.size()) {
      fragments.set(position, screenSlidePageFragment);
    } else {
      fragments.add(screenSlidePageFragment);
    }

    if (onLoadMoreContentListener != null) {
      screenSlidePageFragment.setOnLoadMoreContentListener(onLoadMoreContentListener);
    }

    return screenSlidePageFragment;
  }

  public List<UiMenu> getMenuContent() {
    return menuContent;
  }

  public void setDataItems(List<UiMenu> menuContent) {
    if (fm != null && !fm.isDestroyed() && menuContent != null && menuContent.size() > 0) {
      fragments = new ArrayList<>(menuContent.size());
      this.menuContent = menuContent;
      notifyDataSetChanged();
    }
  }

  @Override public int getItemPosition(Object object) {
    if (object instanceof ScreenSlidePageFragment) {
      ((ScreenSlidePageFragment) object).updateEmotion(emotion);
    }
    return POSITION_NONE;
  }

  @Override public int getCount() {
    return (menuContent != null) ? menuContent.size() : 0;
  }

  public void setEmotion(String emotion) {
    try {
      if (!emotion.equals(this.emotion)) {
        this.emotion = emotion;
        notifyDataSetChanged();
      }
    } catch (Exception ignored) {
    }
  }

  public void setOnLoadMoreContentListener(OnLoadMoreContentListener onLoadMoreContentListener) {
    this.onLoadMoreContentListener = onLoadMoreContentListener;
  }

  public void updateUserLogged() {
    notifyDataSetChanged();
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

  @Override public void finishUpdate(ViewGroup container) {
    try {
      super.finishUpdate(container);
    } catch (Exception exception) {
      Timber.e(exception, "Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
    }
  }

  @Override public Parcelable saveState() {
    return null;
  }
}
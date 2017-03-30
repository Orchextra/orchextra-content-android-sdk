package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.emoiluj.doubleviewpager.DoubleViewPager;
import com.emoiluj.doubleviewpager.DoubleViewPagerAdapter;
import com.emoiluj.doubleviewpager.HorizontalViewPager;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;
import java.util.ArrayList;

public class CardItemRecyclerViewContainer extends LinearLayout {

  private final Context context;
  private ImageLoader imageLoader;
  private DoubleViewPager doubleViewPager;
  private ElementCache elements;
  private DoubleViewPagerAdapter doubleViewPagerAdapter;

  private Handler handler = new Handler();

  public CardItemRecyclerViewContainer(Context context) {
    super(context);
    this.context = context;

    init();
  }

  public CardItemRecyclerViewContainer(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    this.context = context;

    init();
  }

  public CardItemRecyclerViewContainer(Context context, @Nullable AttributeSet attrs,
      int defStyle) {
    super(context, attrs, defStyle);
    this.context = context;

    init();
  }

  private void init() {
    LayoutInflater inflater =
        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.view_card_item_list_layout, this, true);

    initViews(view);
  }

  private void initViews(View view) {
    doubleViewPager = (DoubleViewPager) view.findViewById(R.id.verticalViewPager);
  }

  public void setImageLoader(ImageLoader imageLoader) {
    this.imageLoader = imageLoader;
  }

  public void initialize() {
    if (imageLoader == null) {
      Log.e(getClass().getCanonicalName(), "ImageLoader and Fragment Manager can't be null");
      return;
    }

    ArrayList<PagerAdapter> verticalPagerAdapters = generateVerticalViewPagerAdapters();
    initViewPager(verticalPagerAdapters);

    startSwitchingPageAutomatically();
  }

  private ArrayList<PagerAdapter> generateVerticalViewPagerAdapters() {
    ArrayList<PagerAdapter> verticalAdapters = new ArrayList<>();

    for (int i = 0; i < 4; i++) {
      verticalAdapters.add(createVerticalAdapters());
    }

    return verticalAdapters;
  }

  private VerticalPagerAdapter createVerticalAdapters() {
    return new VerticalPagerAdapter(context, imageLoader, elements);
  }

  private void initViewPager(ArrayList<PagerAdapter> verticalPagerAdapters) {
    doubleViewPagerAdapter =
        new DoubleViewPagerAdapter(getContext(), verticalPagerAdapters);

    doubleViewPager.setAdapter(doubleViewPagerAdapter);
    doubleViewPagerAdapter.notifyDataSetChanged();
    doubleViewPager.setOnSwipeMoveListener(onSwipeListener);
  }

  public void addCards(ElementCache elements) {
    this.elements = elements;
  }

  public void startSwitchingPageAutomatically() {
    handler.postDelayed(switchPageRunnable, 5000);
  }

  public void stopSwitchingPageAutomatically() {
    handler.removeCallbacks(switchPageRunnable);
  }

  public void restartSwitchingPageAutomatically() {
    stopSwitchingPageAutomatically();
    startSwitchingPageAutomatically();
  }

  Runnable switchPageRunnable = new Runnable() {
    @Override public void run() {
      int currentItem = doubleViewPager.getCurrentItem();

      currentItem++;

      if (currentItem >= doubleViewPager.getChildCount()) {
        currentItem = 0;
      }

      doubleViewPager.setCurrentItem(currentItem, true);

      startSwitchingPageAutomatically();
    }
  };

  private HorizontalViewPager.OnSwipeMoveListener onSwipeListener = new HorizontalViewPager.OnSwipeMoveListener() {
    @Override public void onSwipe() {
      stopSwitchingPageAutomatically();

      handler.postDelayed(startAutoSwipingWhenPositionIsZero, 1000);
    }
  };

  private Runnable startAutoSwipingWhenPositionIsZero = new Runnable() {
    @Override public void run() {
      int currentItem = doubleViewPager.getCurrentItem();
      int currentPageSelectedWhenScrolled =
          doubleViewPagerAdapter.getVerticalViewPager(currentItem)
              .getCurrentPageSelectedWhenScrolled();

      if (currentPageSelectedWhenScrolled == 0) {
        startSwitchingPageAutomatically();
      }
    }
  };
}

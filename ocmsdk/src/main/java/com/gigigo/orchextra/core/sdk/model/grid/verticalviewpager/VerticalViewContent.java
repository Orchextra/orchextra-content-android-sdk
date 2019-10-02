package com.gigigo.orchextra.core.sdk.model.grid.verticalviewpager;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import com.gigigo.multiplegridrecyclerview.entities.Cell;
import com.gigigo.orchextra.ocm.views.UiListedBaseContentData;
import com.gigigo.orchextra.ocmsdk.R;
import java.util.List;
import timber.log.Timber;

public class VerticalViewContent extends UiListedBaseContentData {

  private VerticalViewPager viewPager;
  private VerticalViewPagerAdapter adapter;
  private View scrollableArrow;
  private FragmentManager fragmentManager;

  public VerticalViewContent(Context context) {
    this(context, null);
  }

  public VerticalViewContent(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public VerticalViewContent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void init() {
    initViews();
    initViewPager();
  }

  private void initViews() {
    inflate(getContext(), R.layout.view_vertical_viewpager_item, this);
    viewPager = findViewById(R.id.verticalViewPager);
    scrollableArrow = findViewById(R.id.scrollable_arrow);
  }

  private void initViewPager() {
    if (viewPager != null) {
      adapter = new VerticalViewPagerAdapter(fragmentManager, listedContentListener);
      viewPager.setAdapter(adapter);

      viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
        @Override public void onPageSelected(int position) {
          super.onPageSelected(position);

          if (position == 0 && adapter.getCount() > 1) {
            scrollableArrow.setVisibility(VISIBLE);
          } else {
            scrollableArrow.setVisibility(GONE);
          }
        }
      });
    }
  }

  @Override public void setData(List<Cell> data) {
    if (viewPager != null) {
      Timber.d("setData(%s)", data.size());
      adapter.setData(data);
    } else {
      Timber.e("setData() with null viewPager");
    }

    if (data.size() <= 1) {
      scrollableArrow.setVisibility(GONE);
    }
  }

  @Override public void scrollToTop() {
    if (viewPager != null) {
      viewPager.setCurrentItem(0);
    }
  }

  @Override public void showErrorView() {
    if (viewPager != null) {
      viewPager.setVisibility(View.GONE);
      errorView.setVisibility(View.VISIBLE);
    }
  }

  @Override public void showEmptyView() {
    if (viewPager != null) {
      viewPager.setVisibility(View.GONE);
      emptyView.setVisibility(View.VISIBLE);
    }
  }

  @Override public void showProgressView(boolean isVisible) {
    if (loadingView != null) {
      loadingView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
  }

  public void setFragmentManager(FragmentManager fragmentManager) {
    this.fragmentManager = fragmentManager;
  }
}

package com.gigigo.orchextra.core.sdk.model.grid.verticalviewpager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import com.gigigo.multiplegridrecyclerview.entities.Cell;
import com.gigigo.orchextra.ocm.views.UiListedBaseContentData;
import com.gigigo.orchextra.ocmsdk.R;
import java.util.List;

public class VerticalViewContent extends UiListedBaseContentData {

  private VerticalViewPager viewPager;
  private VerticalViewPagerAdapter adapter;

  public VerticalViewContent(Context context) {
    super(context);
  }

  public VerticalViewContent(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public VerticalViewContent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void init() {
    View view = inflateLayout();
    initViews(view);
    initViewPager();
  }

  private View inflateLayout() {
    LayoutInflater inflater =
        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    return inflater.inflate(R.layout.view_vertical_viewpager_item, this, true);
  }

  private void initViews(View view) {
    viewPager = view.findViewById(R.id.verticalViewPager);
  }

  private void initViewPager() {
    if (viewPager != null) {
      FragmentManager fragmentManager =
          ((FragmentActivity) getContext()).getSupportFragmentManager();
      adapter = new VerticalViewPagerAdapter(fragmentManager, listedContentListener);
      viewPager.setAdapter(adapter);
    }
  }

  @Override public void setData(List<Cell> data) {
    if (viewPager != null) {
      adapter.setData(data);
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
}

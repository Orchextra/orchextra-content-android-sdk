package com.gigigo.orchextra.core.sdk.model.grid.horizontalviewpager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import com.gigigo.multiplegridrecyclerview.entities.Cell;
import com.gigigo.orchextra.ocm.views.CircleIndicator;
import com.gigigo.orchextra.ocm.views.UiListedBaseContentData;
import com.gigigo.orchextra.ocmsdk.R;
import java.util.List;

public class HorizontalViewPager extends UiListedBaseContentData {

  private ViewPager listedHorizontalViewPager;
  private CircleIndicator indicator;
  private HorizontalViewPagerAdapter adapter;
  private FragmentManager fragmentManager;

  public HorizontalViewPager(Context context) {
    super(context);
  }

  public HorizontalViewPager(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public HorizontalViewPager(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void init() {
    fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();

    View view = inflateLayout();
    initViews(view);
    initViewPager();
  }

  private View inflateLayout() {
    LayoutInflater inflater =
        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    return inflater.inflate(R.layout.view_horizontal_viewpager_item, this, true);
  }

  private void initViews(View view) {
    listedHorizontalViewPager = (ViewPager) view.findViewById(R.id.listedHorizontalViewPager);
    indicator = (CircleIndicator) view.findViewById(R.id.ci_indicator);
  }

  private void initViewPager() {
    if (listedHorizontalViewPager != null) {
      adapter = new HorizontalViewPagerAdapter(fragmentManager, imageLoader, listedContentListener);
      listedHorizontalViewPager.setAdapter(adapter);
    }
  }

  @Override public void setData(List<Cell> cellDataList) {
    if (listedHorizontalViewPager != null) {
      adapter.setItems(cellDataList);
      indicator.setViewPager(listedHorizontalViewPager);
    }
  }

  @Override public void scrollToTop() {

  }

  @Override public void showErrorView() {
    if (listedHorizontalViewPager != null) {
      listedHorizontalViewPager.setVisibility(View.GONE);
      errorView.setVisibility(View.VISIBLE);
    }
  }

  @Override public void showEmptyView() {
    if (listedHorizontalViewPager != null) {
      listedHorizontalViewPager.setVisibility(View.GONE);
      emptyView.setVisibility(View.VISIBLE);
    }
  }

  @Override public void showProgressView(boolean isVisible) {
    if (loadingView != null) {
      loadingView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
  }
}

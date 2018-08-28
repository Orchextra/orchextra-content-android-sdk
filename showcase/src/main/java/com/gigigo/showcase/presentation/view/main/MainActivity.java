package com.gigigo.showcase.presentation.view.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.showcase.App;
import com.gigigo.showcase.R;
import com.gigigo.showcase.presentation.presenter.MainPresenter;
import com.gigigo.showcase.presentation.view.main.adapter.ScreenSlidePagerAdapter;
import com.gigigo.showcase.presentation.view.settings.SettingsActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView {

  private static final String INSTANCE_TAB_STATE = "INSTANCE_TAB_STATE";

  private TabLayout tabLayout;
  private View loadingView;
  private View emptyView;
  private View errorView;
  private View networkErrorView;
  private ViewPager viewPager;

  private MainPresenter presenter;
  private Parcelable tabState;
  private ScreenSlidePagerAdapter pagerAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();

    App app = (App) getApplication();
    presenter = new MainPresenter(app.getDataManager(), app.getContentManager());
    presenter.attachView(this, true);
  }

  private void initViews() {
    tabLayout = findViewById(R.id.tabLayout);
    viewPager = findViewById(R.id.viewpager);
    loadingView = findViewById(R.id.loading_view);
    emptyView = findViewById(R.id.empty_view);
    errorView = findViewById(R.id.error_view);
    networkErrorView = findViewById(R.id.network_error_view);

    pagerAdapter =
        new ScreenSlidePagerAdapter(getSupportFragmentManager(), () -> presenter.reloadContent());
    viewPager.setAdapter(pagerAdapter);
    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    errorView.setOnClickListener(v -> presenter.reloadContent());
    emptyView.setOnClickListener(v -> presenter.reloadContent());
    networkErrorView.setOnClickListener(v -> presenter.reloadContent());

    ImageButton settingsButton = findViewById(R.id.settingsButton);
    settingsButton.setOnClickListener(view -> presenter.onSettingsClick());
  }

  @Override public void showLoading() {
    loadingView.setVisibility(View.VISIBLE);
    viewPager.setVisibility(View.GONE);
    emptyView.setVisibility(View.GONE);
    errorView.setVisibility(View.GONE);
    networkErrorView.setVisibility(View.GONE);
  }

  @Override public void showEmptyView() {
    loadingView.setVisibility(View.GONE);
    viewPager.setVisibility(View.GONE);
    errorView.setVisibility(View.GONE);
    emptyView.setVisibility(View.VISIBLE);
    networkErrorView.setVisibility(View.GONE);
  }

  @Override public void showErrorView() {
    loadingView.setVisibility(View.GONE);
    viewPager.setVisibility(View.GONE);
    emptyView.setVisibility(View.GONE);
    errorView.setVisibility(View.VISIBLE);
    networkErrorView.setVisibility(View.GONE);
  }

  @Override public void showContentView(@NonNull List<UiMenu> uiMenuList) {
    loadingView.setVisibility(View.GONE);
    emptyView.setVisibility(View.GONE);
    errorView.setVisibility(View.GONE);
    viewPager.setVisibility(View.VISIBLE);
    networkErrorView.setVisibility(View.GONE);

    viewPager.setOffscreenPageLimit(uiMenuList.size());

    tabLayout.removeAllTabs();
    if (uiMenuList.size() <= 4) {
      tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
      tabLayout.setTabMode(TabLayout.MODE_FIXED);
    } else {
      tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
      tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    for (UiMenu section : uiMenuList) {
      TabLayout.Tab tab = tabLayout.newTab().setText(section.getText());
      tabLayout.addTab(tab);
    }

    pagerAdapter.setDataItems(uiMenuList);
    viewPager.onRestoreInstanceState(tabState);
  }

  @Override public void showNetworkErrorView() {
    loadingView.setVisibility(View.GONE);
    emptyView.setVisibility(View.GONE);
    errorView.setVisibility(View.GONE);
    viewPager.setVisibility(View.GONE);
    networkErrorView.setVisibility(View.VISIBLE);
  }

  @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    tabState = savedInstanceState.getParcelable(INSTANCE_TAB_STATE);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == SettingsActivity.SETTINGS_RESULT_CODE && resultCode == Activity.RESULT_OK) {
      presenter.attachView(this, false);
    }
  }

  @Override public void showSettingsView() {
    SettingsActivity.openForResult(MainActivity.this);
  }
}

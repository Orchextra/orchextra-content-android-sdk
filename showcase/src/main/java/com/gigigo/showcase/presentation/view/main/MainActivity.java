package com.gigigo.showcase.presentation.view.main;

import android.os.Bundle;
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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainView {

  private TabLayout tabLayout;
  private View loadingView;
  private View emptyView;
  private View errorView;
  private View networkErrorView;
  private ViewPager viewpager;
  private View newContentMainContainer;

  private MainPresenter presenter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();

    presenter = new MainPresenter(((App) getApplication()).getContentManager());
  }

  @Override protected void onResume() {
    super.onResume();
    presenter.attachView(this, true);
  }

  private void initViews() {
    tabLayout = findViewById(R.id.tabLayout);
    viewpager = findViewById(R.id.viewpager);
    newContentMainContainer = findViewById(R.id.newContentMainContainer);
    loadingView = findViewById(R.id.loading_view);
    emptyView = findViewById(R.id.empty_view);
    errorView = findViewById(R.id.error_view);
    networkErrorView = findViewById(R.id.network_error_view);

    ScreenSlidePagerAdapter pagerAdapter =
        new ScreenSlidePagerAdapter(getSupportFragmentManager(), new ArrayList<UiMenu>());
    viewpager.setAdapter(pagerAdapter);
    viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    //errorView.setOnClickListener(v -> getContent());
    //emptyView.setOnClickListener(v -> getContent());
    //networkErrorView.setOnClickListener(v -> getContent());

    ImageButton settingsButton = findViewById(R.id.settingsButton);
    settingsButton.setOnClickListener(view -> SettingsActivity.openForResult(MainActivity.this));
  }

  @Override public void showLoading() {
    loadingView.setVisibility(View.VISIBLE);
    viewpager.setVisibility(View.GONE);
    emptyView.setVisibility(View.GONE);
    errorView.setVisibility(View.GONE);
    networkErrorView.setVisibility(View.GONE);
  }

  @Override public void showEmptyView() {
    loadingView.setVisibility(View.GONE);
    viewpager.setVisibility(View.GONE);
    errorView.setVisibility(View.GONE);
    emptyView.setVisibility(View.VISIBLE);
    networkErrorView.setVisibility(View.GONE);
  }

  @Override public void showErrorView() {
    loadingView.setVisibility(View.GONE);
    viewpager.setVisibility(View.GONE);
    emptyView.setVisibility(View.GONE);
    errorView.setVisibility(View.VISIBLE);
    networkErrorView.setVisibility(View.GONE);
  }

  @Override public void showContentView() {
    loadingView.setVisibility(View.GONE);
    emptyView.setVisibility(View.GONE);
    errorView.setVisibility(View.GONE);
    viewpager.setVisibility(View.VISIBLE);
    networkErrorView.setVisibility(View.GONE);
  }

  @Override public void showNetworkErrorView() {
    loadingView.setVisibility(View.GONE);
    emptyView.setVisibility(View.GONE);
    errorView.setVisibility(View.GONE);
    viewpager.setVisibility(View.GONE);
    networkErrorView.setVisibility(View.VISIBLE);
  }
}

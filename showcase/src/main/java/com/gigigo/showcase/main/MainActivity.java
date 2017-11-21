package com.gigigo.showcase.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import com.gigigo.orchextra.Orchextra;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.OcmCallbacks;
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver;
import com.gigigo.orchextra.ocm.callbacks.OnRequiredLoginCallback;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.ocm.dto.UiMenuData;
import com.gigigo.showcase.ContentManager;
import com.gigigo.showcase.R;
import com.gigigo.showcase.Utils;
import com.gigigo.showcase.main.adapter.ScreenSlidePagerAdapter;
import com.gigigo.showcase.settings.SettingsActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private TabLayout tabLayout;
  private View loadingView;
  private View emptyView;
  private View errorView;
  private View networkErrorView;
  private ViewPager viewpager;
  private View newContentMainContainer;
  private List<UiMenu> menuContent;

  private OnRequiredLoginCallback onDoRequiredLoginCallback = new OnRequiredLoginCallback() {
    @Override public void doRequiredLogin() {
      Toast.makeText(getApplicationContext(), "Item needs permissions", Toast.LENGTH_SHORT).show();
    }

    @Override public void doRequiredLogin(String elementUrl) {
      Toast.makeText(getApplicationContext(), "Item needs permissions " + elementUrl,
          Toast.LENGTH_SHORT).show();
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();

    Ocm.setOnDoRequiredLoginCallback(onDoRequiredLoginCallback);

    initDefaultOcm();
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
    errorView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getContent();
      }
    });
    emptyView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getContent();
      }
    });
    networkErrorView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getContent();
      }
    });

    ImageButton settingsButton = findViewById(R.id.settingsButton);
    settingsButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        SettingsActivity.openForResult(MainActivity.this);
      }
    });
  }

  private void initDefaultOcm() {

    if (!Utils.isOnline(getApplicationContext())) {
      showNetworkErrorView();
      return;
    }

    showLoading();
    ContentManager contentManager = ContentManager.getInstance();
    contentManager.start(new ContentManager.ContentManagerCallback<String>() {
      @Override public void onSuccess(String result) {
        getContent();
      }

      @Override public void onError(Exception exception) {
        Toast.makeText(MainActivity.this, "Credentails error: " + exception.getMessage(),
            Toast.LENGTH_SHORT).show();
      }
    });

    Ocm.setOnCustomSchemeReceiver(new OnCustomSchemeReceiver() {
      @Override public void onReceive(String customScheme) {
        Toast.makeText(MainActivity.this, customScheme, Toast.LENGTH_SHORT).show();
        Orchextra.startScannerActivity();
      }
    });
    Ocm.start();
  }

  void getContent() {
    Ocm.getMenus(true, new OcmCallbacks.Menus() {
      @Override public void onMenusLoaded(UiMenuData menus) {

        List<UiMenu> uiMenu = menus.getUiMenuList();

        if (uiMenu == null) {
          Toast.makeText(MainActivity.this, "menu is null", Toast.LENGTH_SHORT).show();
          showErrorView();
        } else {
          if (uiMenu.isEmpty()) {
            showEmptyView();
          } else {
            showContentView();
            tabLayout.removeAllTabs();
            viewpager.setOffscreenPageLimit(uiMenu.size());
            onGoDetailView(uiMenu);
            menuContent = uiMenu;

            ScreenSlidePagerAdapter pagerAdapter =
                new ScreenSlidePagerAdapter(getSupportFragmentManager(), uiMenu);
            viewpager.setAdapter(pagerAdapter);

            checkIfMenuHasChanged(uiMenu);
          }
        }
      }

      @Override public void onMenusFails(Throwable e) {
        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        showErrorView();
      }
    });
  }

  private void checkIfMenuHasChanged(final List<UiMenu> oldMenus) {
    Ocm.getMenus(true, new OcmCallbacks.Menus() {
      @Override public void onMenusLoaded(UiMenuData menus) {

        List<UiMenu> newMenus = menus.getUiMenuList();

        if (oldMenus == null || newMenus == null) {
          return;
        }
        if (oldMenus.size() != newMenus.size()) {
          showIconNewContent(newMenus);
        } else {
          for (int i = 0; i < newMenus.size(); i++) {
            if (oldMenus.get(i).getUpdateAt() != newMenus.get(i).getUpdateAt()) {
              showIconNewContent(newMenus);
              return;
            }
          }
        }
      }

      @Override public void onMenusFails(Throwable e) {
      }
    });
  }

  private void showIconNewContent(final List<UiMenu> newMenus) {
    newContentMainContainer.setVisibility(View.VISIBLE);
    newContentMainContainer.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        newContentMainContainer.setVisibility(View.GONE);

        showContentView();
        menuContent = newMenus;
        viewpager.removeAllViews();
        ScreenSlidePagerAdapter pagerAdapter =
            new ScreenSlidePagerAdapter(getSupportFragmentManager(), newMenus);
        viewpager.setAdapter(pagerAdapter);

        tabLayout.removeAllTabs();
        onGoDetailView(newMenus);
      }
    });
  }

  private void onGoDetailView(List<UiMenu> uiMenu) {
    tabLayout.removeAllTabs();
    if (uiMenu.size() > 0) {
      UiMenu menu;
      TabLayout.Tab tab;

      for (int i = 0; i < uiMenu.size(); i++) {
        menu = uiMenu.get(i);
        tab = tabLayout.newTab().setText(menu.getText());
        tabLayout.addTab(tab);
      }
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == SettingsActivity.RESULT_CODE) {
      if (resultCode == Activity.RESULT_OK) {
        ContentManager contentManager = ContentManager.getInstance();
        contentManager.clear();
        getContent();
      }
    }
  }

  private void showLoading() {
    loadingView.setVisibility(View.VISIBLE);
    viewpager.setVisibility(View.GONE);
    emptyView.setVisibility(View.GONE);
    errorView.setVisibility(View.GONE);
    networkErrorView.setVisibility(View.GONE);
  }

  private void showEmptyView() {
    loadingView.setVisibility(View.GONE);
    viewpager.setVisibility(View.GONE);
    errorView.setVisibility(View.GONE);
    emptyView.setVisibility(View.VISIBLE);
    networkErrorView.setVisibility(View.GONE);
  }

  private void showErrorView() {
    loadingView.setVisibility(View.GONE);
    viewpager.setVisibility(View.GONE);
    emptyView.setVisibility(View.GONE);
    errorView.setVisibility(View.VISIBLE);
    networkErrorView.setVisibility(View.GONE);
  }

  private void showContentView() {
    loadingView.setVisibility(View.GONE);
    emptyView.setVisibility(View.GONE);
    errorView.setVisibility(View.GONE);
    viewpager.setVisibility(View.VISIBLE);
    networkErrorView.setVisibility(View.GONE);
  }

  private void showNetworkErrorView() {
    loadingView.setVisibility(View.GONE);
    emptyView.setVisibility(View.GONE);
    errorView.setVisibility(View.GONE);
    viewpager.setVisibility(View.GONE);
    networkErrorView.setVisibility(View.VISIBLE);
  }
}

package com.gigigo.sample;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.gigigo.orchextra.Orchextra;
import com.gigigo.orchextra.core.sdk.model.grid.dto.ClipToPadding;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.OcmCallbacks;
import com.gigigo.orchextra.ocm.callbacks.OcmCredentialCallback;
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private TabLayout tabLayout;
  private ViewPager viewpager;
  private ScreenSlidePagerAdapter adapter;

  private List<UiMenu> uiMenu;

  private TabLayout.OnTabSelectedListener onTabSelectedListener =
      new TabLayout.OnTabSelectedListener() {
        @Override public void onTabSelected(TabLayout.Tab tab) {
          viewpager.setCurrentItem(tab.getPosition());
        }

        @Override public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override public void onTabReselected(TabLayout.Tab tab) {
          viewpager.setCurrentItem(tab.getPosition());
        }
      };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();
    startCredentials();
  }

  private void initViews() {
    tabLayout = (TabLayout) findViewById(R.id.tabLayout);
    viewpager = (ViewPager) findViewById(R.id.viewpager);

    adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
    viewpager.setAdapter(adapter);
    viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
  }

  private void startCredentials() {
    Ocm.startWithCredentials(App.API_KEY, App.API_SECRET, new OcmCredentialCallback() {
      @Override public void onCredentialReceiver(String accessToken) {
        //TODO Fix in Orchextra
        runOnUiThread(new Runnable() {
          @Override public void run() {
            if (uiMenu == null || uiMenu.size() == 0) {
              getContent();
            }
          }
        });
      }

      @Override public void onCredentailError(String code) {
        Snackbar.make(tabLayout, "No Internet Connection: " + code, Snackbar.LENGTH_INDEFINITE)
            .show();
      }
    });

    Ocm.setOnCustomSchemeReceiver(new OnCustomSchemeReceiver() {
      @Override public void onReceive(String customScheme) {
        Toast.makeText(MainActivity.this, customScheme, Toast.LENGTH_SHORT).show();
        Orchextra.startScannerActivity();
      }
    });
  }

  private void getContent() {

    Ocm.getMenus(new OcmCallbacks.Menus() {
      @Override public void onMenusLoaded(List<UiMenu> menus) {
        uiMenu = menus;
        if (uiMenu == null) {
          Toast.makeText(MainActivity.this, "menu is null", Toast.LENGTH_SHORT).show();
        } else {
          viewpager.setOffscreenPageLimit(uiMenu.size());
          onGoDetailView(uiMenu);
          adapter.setDataItems(uiMenu);
        }
      }

      @Override public void onMenusFails(Throwable e) {
        if (uiMenu == null) {
          Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  private void onGoDetailView(List<UiMenu> uiMenu) {
    if (uiMenu.size() > 0) {
      for (int i = 0; i < uiMenu.size(); i++) {
        UiMenu menu = uiMenu.get(i);
        TabLayout.Tab tab = tabLayout.newTab().setText(menu.getText());
        tabLayout.addTab(tab);
      }
    }

    tabLayout.addOnTabSelectedListener(onTabSelectedListener);
  }
}

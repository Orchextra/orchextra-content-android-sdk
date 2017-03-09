package com.gigigo.sample;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.callbacks.OcmCredentialCallback;
import com.gigigo.orchextra.ocm.callbacks.OnRetrieveUiMenuListener;
import com.gigigo.orchextra.ocm.dto.BottomPadding;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private TabLayout tabLayout;

  private List<UiMenu> uiMenu;



  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();
    startCredentials();
  }

  private void initViews() {
    tabLayout = (TabLayout) findViewById(R.id.tabLayout);
  }

  private void startCredentials() {
    Ocm.startWithCredentials(App.API_KEY, App.API_SECRET, new OcmCredentialCallback() {
      @Override public void onCredentialReceiver(String accessToken) {
        if (uiMenu == null || uiMenu.size() == 0) {
          getContent(accessToken);
        }
      }
    });
  }

  private void getContent(String accessToken) {
    Ocm.getMenus(new OnRetrieveUiMenuListener() {
      @Override public void onResult(final List<UiMenu> uiMenu) {
        runOnUiThread(new Runnable() {
          @Override public void run() {
            onGoDetailView(uiMenu);
            selectFirstTab();
          }
        });
      }

      @Override public void onNoNetworkConnectionError() {
        runOnUiThread(new Runnable() {
          @Override public void run() {
            Toast.makeText(MainActivity.this, "onNoNetworkConnectionError", Toast.LENGTH_SHORT)
                .show();
          }
        });
      }

      @Override public void onResponseDataError() {
        runOnUiThread(new Runnable() {
          @Override public void run() {
            Toast.makeText(MainActivity.this, "onResponseDataError", Toast.LENGTH_SHORT).show();
          }
        });
      }
    });
  }

  private void onGoDetailView(List<UiMenu> uiMenu) {
    this.uiMenu = uiMenu;
    if (uiMenu.size() > 0) {
      for (int i = 0; i < uiMenu.size(); i++) {
        UiMenu menu = uiMenu.get(i);
        TabLayout.Tab tab = tabLayout.newTab().setText(menu.getText());
        tabLayout.addTab(tab);
      }
    }

    tabLayout.addOnTabSelectedListener(onTabSelectedListener);
  }

  private TabLayout.OnTabSelectedListener onTabSelectedListener =
      new TabLayout.OnTabSelectedListener() {
        @Override public void onTabSelected(TabLayout.Tab tab) {
          loadFragment(tab);
        }

        @Override public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override public void onTabReselected(TabLayout.Tab tab) {
          loadFragment(tab);
        }
      };

  private void loadFragment(TabLayout.Tab tab) {
    UiGridBaseContentData uiGridBaseContentData =
        Ocm.generateGridView(uiMenu.get(tab.getPosition()).getElementUrl(), null);

    getSupportFragmentManager().beginTransaction()
        .replace(R.id.contentLayout, uiGridBaseContentData)
        .commit();
  }

  private void selectFirstTab() {
    if (tabLayout.getChildCount() > 0) {
      TabLayout.Tab tab = tabLayout.getTabAt(0);
      loadFragment(tab);
    }
  }
}

package com.gigigo.sample;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.gigigo.orchextra.Orchextra;
import com.gigigo.orchextra.core.sdk.model.grid.dto.ClipToPadding;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.callbacks.OcmCredentialCallback;
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private TabLayout tabLayout;
  private View progressbar;

  private List<UiMenu> uiMenu;
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

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();
    startCredentials();
  }

  private void initViews() {
    tabLayout = (TabLayout) findViewById(R.id.tabLayout);
    progressbar = findViewById(R.id.progressbar);
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
    });

    Ocm.setOnCustomSchemeReceiver(new OnCustomSchemeReceiver() {
      @Override public void onReceive(String customScheme) {
        Toast.makeText(MainActivity.this, customScheme, Toast.LENGTH_SHORT).show();
        Orchextra.startScannerActivity();
      }
    });
  }

  private void getContent() {
    uiMenu = Ocm.getMenus();

    if (uiMenu == null) {
      Toast.makeText(MainActivity.this, "menu is null", Toast.LENGTH_SHORT).show();
    } else {
      onGoDetailView(uiMenu);
      selectFirstTab();
    }
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

  private void loadFragment(TabLayout.Tab tab) {
    UiGridBaseContentData uiGridBaseContentData =
        Ocm.generateGridView(uiMenu.get(tab.getPosition()).getElementUrl(), null);

    uiGridBaseContentData.setProgressView(progressbar);
    uiGridBaseContentData.setClipToPaddingBottomSize(ClipToPadding.PADDING_NONE);

    getSupportFragmentManager().beginTransaction()
        .replace(R.id.contentLayout, uiGridBaseContentData)
        .commitAllowingStateLoss();
  }

  private void selectFirstTab() {
    if (tabLayout.getChildCount() > 0) {
      TabLayout.Tab tab = tabLayout.getTabAt(0);
      loadFragment(tab);
    }
  }
}

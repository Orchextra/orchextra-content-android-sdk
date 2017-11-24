package com.gigigo.sample;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.gigigo.orchextra.Orchextra;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.OcmCallbacks;
import com.gigigo.orchextra.ocm.callbacks.OcmCredentialCallback;
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver;
import com.gigigo.orchextra.ocm.callbacks.OnRequiredLoginCallback;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.ocm.dto.UiMenuData;
import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private TabLayout tabLayout;
  private ViewPager viewpager;
  private ScreenSlidePagerAdapter adapter;
  private View newContentMainContainer;
  private TabLayout.OnTabSelectedListener onTabSelectedListener =
      new TabLayout.OnTabSelectedListener() {
        @Override public void onTabSelected(TabLayout.Tab tab) {
          viewpager.setCurrentItem(tab.getPosition());
          ScreenSlidePageFragment frag =
              ((ScreenSlidePageFragment) adapter.getItem(viewpager.getCurrentItem()));
          frag.reloadSection();
        }

        @Override public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override public void onTabReselected(TabLayout.Tab tab) {
          viewpager.setCurrentItem(tab.getPosition());
          ((ScreenSlidePageFragment) adapter.getItem(viewpager.getCurrentItem())).reloadSection();
        }
      };

  private OnRequiredLoginCallback onDoRequiredLoginCallback = new OnRequiredLoginCallback() {
    @Override public void doRequiredLogin() {
      Toast.makeText(getApplicationContext(), "Item needs permissions", Toast.LENGTH_SHORT).show();
    }

    @Override public void doRequiredLogin(String elementUrl) {
      Toast.makeText(getApplicationContext(), "Item needs permissions" + elementUrl,
          Toast.LENGTH_SHORT).show();
    }
  };

  public static boolean deleteDir(File dir) {
    if (dir != null && dir.isDirectory()) {
      String[] children = dir.list();
      for (int i = 0; i < children.length; i++) {
        boolean success = deleteDir(new File(dir, children[i]));
        if (!success) {
          return false;
        }
      }
    }

    return dir.delete();
  }

  @Override protected void onResume() {
    super.onResume();

    //ReadedArticles
    //if (OCManager.getShowReadArticles() && adapter != null) {
    //  adapter.reloadSections();
    //}
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();

    Ocm.setOnDoRequiredLoginCallback(onDoRequiredLoginCallback);

    startCredentials();
  }

  private void initViews() {
    tabLayout = findViewById(R.id.tabLayout);
    viewpager = findViewById(R.id.viewpager);
    //View fabReload = findViewById(R.id.fabReload);
    //View fabChange = findViewById(R.id.fabChange);
    View fabClean = findViewById(R.id.fabClean);

    fabClean.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        //Orchextra.startScannerActivity();
        Orchextra.startImageRecognition();
      }
    });

    //fabChange.setOnClickListener(new View.OnClickListener() {
    //  @Override public void onClick(View v) {
    //    startCredentials();
    //if (OCManager.getShowReadArticles() && adapter != null) {
    //OCManager.transform+=1;
    //adapter.reloadSections();
    //Toast.makeText(MainActivity.this, "Refresh grid from integratied app if readed articles are enabled transform number"
    //    + OCManager.transform, Toast.LENGTH_LONG).show();
    //}
    //}
    //});

    newContentMainContainer = findViewById(R.id.newContentMainContainer);

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
            getContent();
          }
        });
      }

      @Override public void onCredentailError(String code) {
        Snackbar.make(tabLayout,
            "No Internet Connection: " + code + "\n check Credentials-Enviroment",
            Snackbar.LENGTH_INDEFINITE).show();
      }
    });

    Ocm.setOnCustomSchemeReceiver(new OnCustomSchemeReceiver() {
      @Override public void onReceive(String customScheme) {
        // Toast.makeText(MainActivity.this, customScheme, Toast.LENGTH_SHORT).show();
        Orchextra.startScannerActivity();
      }
    });
    Ocm.start();//likewoah
  }
  //endregion

  public void clearApplicationData() {
    File cache = getCacheDir();
    File appDir = new File(cache.getParent());
    if (appDir.exists()) {
      String[] children = appDir.list();
      for (String s : children) {
        if (!s.equals("lib")) {
          deleteDir(new File(appDir, s));
        }
      }
    }
  }

  private void getContent() {
    Ocm.getMenus(false, new OcmCallbacks.Menus() {
      @Override public void onMenusLoaded(UiMenuData oldUiMenuData) {
        final List<UiMenu> oldUiMenuList = oldUiMenuData.getUiMenuList();

        if (oldUiMenuList == null) {
          Toast.makeText(MainActivity.this, "menu is null", Toast.LENGTH_SHORT).show();
          return;
        }

        onGoDetailView(oldUiMenuList);

        if (!oldUiMenuData.isFromCloud()) {
          Ocm.getMenus(true, new OcmCallbacks.Menus() {
            @Override public void onMenusLoaded(UiMenuData newUiMenuData) {
              List<UiMenu> newUiMenuList = newUiMenuData.getUiMenuList();
              if (newUiMenuList == null) {
                return;
              }

              checkIfMenuHasChanged(oldUiMenuList, newUiMenuList);
            }

            @Override public void onMenusFails(Throwable e) {

            }
          });
        }
      }

      @Override public void onMenusFails(Throwable e) {
        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }

  private void checkIfMenuHasChanged(List<UiMenu> oldUiMenuList, List<UiMenu> newUiMenuList) {
    if (oldUiMenuList == null || newUiMenuList == null) {
      return;
    }

    if (oldUiMenuList.size() != newUiMenuList.size()) {
      showIconNewContent(newUiMenuList);
    } else {
      for (int i = 0; i < newUiMenuList.size(); i++) {
        if (oldUiMenuList.get(i).getUpdateAt() != newUiMenuList.get(i).getUpdateAt()) {
          showIconNewContent(newUiMenuList);
          return;
        }
      }
    }
  }

  private void showIconNewContent(final List<UiMenu> newMenus) {
    newContentMainContainer.setVisibility(View.VISIBLE);
    newContentMainContainer.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        newContentMainContainer.setVisibility(View.GONE);

        adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        adapter.setDataItems(newMenus);

        tabLayout.removeAllTabs();
        onGoDetailView(newMenus);
      }
    });
  }

  private void onGoDetailView(List<UiMenu> uiMenu) {
    tabLayout.removeAllTabs();
    if (uiMenu.size() > 0) {
      for (int i = 0; i < uiMenu.size(); i++) {
        UiMenu menu = uiMenu.get(i);
        TabLayout.Tab tab = tabLayout.newTab().setText(menu.getText());
        tabLayout.addTab(tab);

        adapter.setDataItems(uiMenu);
      }
    }

    tabLayout.addOnTabSelectedListener(onTabSelectedListener);
  }
}

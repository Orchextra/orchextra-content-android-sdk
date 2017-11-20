package com.gigigo.sample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.gigigo.orchextra.ocm.dto.UiVersionData;
import java.io.File;
import java.util.List;
import jp.wasabeef.glide.transformations.internal.Utils;

public class MainActivity extends AppCompatActivity {

  private static String PREFS_VERSION = "PREFS_VERSION";
  private SharedPreferences sharedPreferences;
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

    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    Ocm.setOnDoRequiredLoginCallback(onDoRequiredLoginCallback);

    startCredentials();
  }

  private void initViews() {
    tabLayout = (TabLayout) findViewById(R.id.tabLayout);
    viewpager = (ViewPager) findViewById(R.id.viewpager);
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
            getContent(false);
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
  //endregion

  private void checkVersion() {
    //Ocm.getVersion(new OcmCallbacks.Version() {
    //  @Override public void onVersionLoaded(UiVersionData version) {
    //
    //    boolean forceReload = false;
    //    long localVersion = sharedPreferences.getLong(PREFS_VERSION, 0);
    //    long cloudVersion = version.getVersion();
    //
    //    if (cloudVersion > localVersion) {
    //      forceReload = true;
    //      saveVersion(cloudVersion);
    //    }
    //
    //    getContent(forceReload);
    //  }
    //
    //  @Override public void onVersionFails(Throwable e) {
    //    Toast.makeText(MainActivity.this, "Unable to get version", Toast.LENGTH_LONG).show();
    //  }
    //});
  }

  private void saveVersion(long version) {
    sharedPreferences.edit().putLong(PREFS_VERSION, version).apply();
  }

  private void getContent(boolean forceReload) {

    Ocm.getMenus(forceReload, new OcmCallbacks.Menus() {
      @Override public void onMenusLoaded(UiMenuData uiMenuData) {
        if (uiMenuData.getUiMenuList() == null) {
          Toast.makeText(MainActivity.this, "menu is null", Toast.LENGTH_SHORT).show();
        } else {
          List<UiMenu> uiMenu = uiMenuData.getUiMenuList();
          viewpager.setOffscreenPageLimit(uiMenu.size());
          onGoDetailView(uiMenu);
          adapter.setDataItems(uiMenu);

          //if (uiMenuData.isFromCache()) {
          //  checkIfMenuHasChanged(uiMenu);
          //}
        }
      }

      @Override public void onMenusFails(Throwable e) {
        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }

  private void checkIfMenuHasChanged(final List<UiMenu> oldMenus) {
    Ocm.getMenus(true, new OcmCallbacks.Menus() {
      @Override public void onMenusLoaded(UiMenuData newMenudata) {
        List<UiMenu> newMenus = newMenudata.getUiMenuList();
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

        adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        adapter.setDataItems(newMenus);
        viewpager.removeAllViews();
        viewpager.setAdapter(adapter);

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
      }
    }

    tabLayout.addOnTabSelectedListener(onTabSelectedListener);
  }
}

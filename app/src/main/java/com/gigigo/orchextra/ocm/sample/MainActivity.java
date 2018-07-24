package com.gigigo.orchextra.ocm.sample;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.gigigo.orchextra.ocm.OCManagerCallbacks;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.OcmCallbacks;
import com.gigigo.orchextra.ocm.callbacks.OnRequiredLoginCallback;
import com.gigigo.orchextra.ocm.customProperties.Disabled;
import com.gigigo.orchextra.ocm.customProperties.OcmCustomBehaviourDelegate;
import com.gigigo.orchextra.ocm.customProperties.ViewCustomizationType;
import com.gigigo.orchextra.ocm.customProperties.ViewLayer;
import com.gigigo.orchextra.ocm.customProperties.ViewType;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.ocm.dto.UiMenuData;
import com.gigigo.orchextra.ocm.sample.ocm.OcmWrapper;
import com.gigigo.orchextra.ocm.sample.ocm.OcmWrapperImp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

  private OcmWrapper ocmWrapper;
  private TabLayout tabLayout;
  private ViewPager viewpager;
  private ScreenSlidePagerAdapter adapter;
  private TabLayout.OnTabSelectedListener onTabSelectedListener =
      new TabLayout.OnTabSelectedListener() {
        @Override public void onTabSelected(TabLayout.Tab tab) {
          viewpager.setCurrentItem(tab.getPosition());
          ScreenSlidePageFragment frag =
              ((ScreenSlidePageFragment) adapter.getItem(viewpager.getCurrentItem()));
          frag.reloadSection(false);
        }

        @Override public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override public void onTabReselected(TabLayout.Tab tab) {
          viewpager.setCurrentItem(tab.getPosition());
          ((ScreenSlidePageFragment) adapter.getItem(viewpager.getCurrentItem())).reloadSection(
              false);
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

  private OcmCustomBehaviourDelegate customPropertiesDelegate = new OcmCustomBehaviourDelegate() {

    @Override public void customizationForContent(@NotNull Map<String, ?> customProperties,
        @NotNull ViewType viewType, @NotNull
        Function1<? super List<? extends ViewCustomizationType>, Unit> onGetCustomization) {

      Handler handler = new Handler();
      handler.postDelayed(() -> {
        List<ViewCustomizationType> viewCustomizationType = new ArrayList<>();
        viewCustomizationType.add(new Disabled());

        if (customProperties.containsKey("requiredAuth") && customProperties.containsValue(
            "logged")) {
          View view = getLayoutInflater().inflate(R.layout.padlock_view, null);
          viewCustomizationType.add(new ViewLayer(view));
        }

        onGetCustomization.invoke(viewCustomizationType);
      }, 3000);
    }

    @Override public void contentNeedsValidation(@NotNull Map<String, ?> customProperties,
        @NotNull ViewType viewType, @NotNull Function1<? super Boolean, Unit> completion) {

      Set<? extends Map.Entry<String, ?>> entrySet = customProperties.entrySet();
      Iterator<? extends Map.Entry<String, ?>> iterator = entrySet.iterator();
      while (iterator.hasNext()) {
        Map.Entry<String, ?> next = iterator.next();
        String property = next.getKey();
        Object value = next.getValue();

        Handler handler = new Handler();
        switch (property) {
          case "requiredAuth":
            //completion.invoke(true);
            handler.postDelayed(() -> {
              if (value.equals("logged")) {
                completion.invoke(false);
                Toast.makeText(MainActivity.this, "can't continue, requires authorization",
                    Toast.LENGTH_SHORT).show();
              } else {
                completion.invoke(true);
              }
            }, 500);
            break;
        }
      }
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();

    ocmWrapper = new OcmWrapperImp(getApplication());

    Ocm.setOnDoRequiredLoginCallback(onDoRequiredLoginCallback);
    Ocm.setCustomBehaviourDelegate(customPropertiesDelegate);
    Ocm.setCustomUrlCallback(parameters -> {

      Map<String, String> map = new HashMap<>();
      for (String parameter : parameters) {
        map.put("TEST", "TEST");
      }

      return map;
    });
  }

  @Override protected void onResume() {
    super.onResume();
    initOcm();
  }

  private void initViews() {
    tabLayout = findViewById(R.id.tabLayout);
    viewpager = findViewById(R.id.viewpager);
    View fabSearch = findViewById(R.id.fabSearch);
    View scannerButton = findViewById(R.id.scannerButton);

    scannerButton.setOnClickListener(v -> ocmWrapper.scanCode(
        code -> Toast.makeText(MainActivity.this, "Code: " + code, Toast.LENGTH_SHORT).show()));

    fabSearch.setOnClickListener(v -> SearcherActivity.open(MainActivity.this));
    adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
    viewpager.setAdapter(adapter);
    viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
  }

  private void initOcm() {
    ocmWrapper.startWithCredentials(BuildConfig.API_KEY, BuildConfig.API_SECRET,
        BuildConfig.BUSSINES_UNIT, new OcmWrapper.OnStartWithCredentialsCallback() {
          @Override public void onCredentialReceiver(String accessToken) {
            Timber.d("onCredentialReceiver()");
            runOnUiThread(() -> getContent());
          }

          @Override public void onCredentailError() {
            Timber.e("onCredentailError");
            Toast.makeText(MainActivity.this, "onCredentailError", Toast.LENGTH_SHORT).show();
          }
        });
  }

  private void clearData() {
    Ocm.clearData(true, true, new OCManagerCallbacks.Clear() {
      @Override public void onDataClearedSuccessfull() {
        Timber.d("onDataClearedSuccessfull");
        runOnUiThread(() -> getContent());
      }

      @Override public void onDataClearFails(Exception e) {
        Timber.e("onDataClearFails");
        Toast.makeText(MainActivity.this, "Clear data fail!", Toast.LENGTH_SHORT).show();
      }
    });
  }

  private void getContent() {

    Ocm.getMenus(new OcmCallbacks.Menus() {
      @Override public void onMenusLoaded(final UiMenuData uiMenuData) {

        if (uiMenuData == null) {
          Toast.makeText(MainActivity.this, "menu is null", Toast.LENGTH_SHORT).show();
        } else {
          onGoDetailView(uiMenuData.getUiMenuList());
        }
      }

      @Override public void onMenusFails(Throwable e) {
        Timber.e(e, "getMenus()");
        Toast.makeText(MainActivity.this, "Get menu error!", Toast.LENGTH_SHORT).show();
      }
    });
  }

  private void showIconNewContent(final List<UiMenu> newMenus) {
    //newContentMainContainer.setVisibility(View.VISIBLE);
    //newContentMainContainer.setOnClickListener(v -> {
    //  newContentMainContainer.setVisibility(View.GONE);
    //
    //  tabLayout.removeAllTabs();
    //  onGoDetailView(newMenus);
    //});
  }

  private void onGoDetailView(List<UiMenu> uiMenu) {
    viewpager.clearOnPageChangeListeners();


    int pos = tabLayout.getTabCount();

    if (pos==0) {
      for (UiMenu menu : uiMenu) {
        TabLayout.Tab tab = tabLayout.newTab().setText(menu.getText());
        tabLayout.addTab(tab);
      }
    }


    //tabLayout.removeAllTabs();
    //if (uiMenu.size() > 0) {
    //  for (int i = 0; i < uiMenu.size(); i++) {
    //    UiMenu menu = uiMenu.get(i);
    //    TabLayout.Tab tab = tabLayout.newTab().setText(menu.getText());
    //    tabLayout.addTab(tab);
    //  }
    //}

    adapter.setDataItems(uiMenu);
    tabLayout.addOnTabSelectedListener(onTabSelectedListener);
    viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
  }
}
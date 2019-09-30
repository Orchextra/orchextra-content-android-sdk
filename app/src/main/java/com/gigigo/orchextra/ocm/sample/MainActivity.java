package com.gigigo.orchextra.ocm.sample;

import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.OcmCallbacks;
import com.gigigo.orchextra.ocm.callbacks.OnRequiredLoginCallback;
import com.gigigo.orchextra.ocm.customProperties.Disabled;
import com.gigigo.orchextra.ocm.customProperties.OcmCustomBehaviourDelegate;
import com.gigigo.orchextra.ocm.customProperties.OcmCustomTranslationDelegate;
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

  private OcmCustomBehaviourDelegate customPropertiesDelegate = new OcmCustomBehaviourDelegate() {

    @Override public void customizationForContent(@NonNull Map<String, ?> customProperties,
        @NonNull ViewType viewType, @NonNull
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
      }, 1000);
    }

    @Override public void contentNeedsValidation(@NonNull Map<String, ?> customProperties,
        @NonNull ViewType viewType, @NonNull Function1<? super Boolean, Unit> completion) {

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

    ocmWrapper = OcmWrapperImp.getInstance(getApplication());

    Ocm.setOnDoRequiredLoginCallback(onDoRequiredLoginCallback);
    Ocm.setCustomBehaviourDelegate(customPropertiesDelegate);
    Ocm.setCustomTranslationDelegate(ocmCustomTranslationDelegate);
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

    fabSearch.setOnClickListener(v -> {
      //SearcherActivity.open(MainActivity.this);
      ocmWrapper.processDeepLink("ocm/Minirin-Day-1-Learning-Card-1-Disease--SkIWhK7c7");
    });

    adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
    viewpager.setAdapter(adapter);
    viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
  }

  private void initOcm() {
    new Handler().postDelayed(this::getContent, 2000);
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
    },"Shop-AGQb1yH_f");
  }

  private void onGoDetailView(List<UiMenu> uiMenu) {
    viewpager.clearOnPageChangeListeners();
    viewpager.setOffscreenPageLimit(uiMenu.size());

    int count = tabLayout.getTabCount();

    if (count == 0) {
      for (UiMenu menu : uiMenu) {
        TabLayout.Tab tab = tabLayout.newTab().setText(menu.getText());
        tabLayout.addTab(tab);
      }
    } else {
      if (count != uiMenu.size()) {
        tabLayout.removeAllTabs();
        for (UiMenu menu : uiMenu) {
          TabLayout.Tab tab = tabLayout.newTab().setText(menu.getText());
          tabLayout.addTab(tab);
        }
        viewpager.setCurrentItem(0);
      }
    }

    adapter.setDataItems(uiMenu);
    tabLayout.addOnTabSelectedListener(onTabSelectedListener);
    viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
  }

  private OcmCustomTranslationDelegate ocmCustomTranslationDelegate = (key, completion) -> {
    if (com.gigigo.orchextra.ocmsdk.R.string.oc_error_content_not_available_without_internet
        == key) {
      completion.invoke("Sin internet :´(");
    } else {
      completion.invoke(null);
    }
  };
}
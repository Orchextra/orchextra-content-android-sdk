package com.gigigo.orchextra.ocm;

import android.app.Application;
import android.util.Log;
import com.gigigo.orchextra.CrmUser;
import com.gigigo.orchextra.CustomSchemeReceiver;
import com.gigigo.orchextra.Orchextra;
import com.gigigo.orchextra.OrchextraBuilder;
import com.gigigo.orchextra.OrchextraCompletionCallback;
import com.gigigo.orchextra.OrchextraLogLevel;
import com.gigigo.orchextra.core.controller.OcmViewGenerator;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.core.domain.entities.ocm.OxSession;
import com.gigigo.orchextra.core.sdk.OcmSchemeHandler;
import com.gigigo.orchextra.core.sdk.OcmStyleUi;
import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import com.gigigo.orchextra.core.sdk.application.OcmSdkLifecycle;
import com.gigigo.orchextra.core.sdk.di.components.DaggerOcmComponent;
import com.gigigo.orchextra.core.sdk.di.components.OcmComponent;
import com.gigigo.orchextra.core.sdk.di.injector.Injector;
import com.gigigo.orchextra.core.sdk.di.injector.InjectorImpl;
import com.gigigo.orchextra.core.sdk.di.modules.OcmModule;
import com.gigigo.orchextra.device.bluetooth.beacons.BeaconBackgroundModeScan;
import com.gigigo.orchextra.ocm.callbacks.OcmCredentialCallback;
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver;
import com.gigigo.orchextra.ocm.callbacks.OnEventCallback;
import com.gigigo.orchextra.ocm.callbacks.OnRequiredLoginCallback;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.ocm.views.UiDetailBaseContentData;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import com.gigigo.orchextra.ocm.views.UiSearchBaseContentData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import orchextra.javax.inject.Inject;

//import com.gigigo.vuforiaimplementation.ImageRecognitionVuforiaImpl;

public final class OCManager {

  private static OCManager instance;

  @Inject OcmSdkLifecycle ocmSdkLifecycle;
  @Inject OcmContextProvider ocmContextProvider;
  @Inject OcmViewGenerator ocmViewGenerator;
  @Inject OxSession oxSession;
  @Inject Authoritation authoritation;
  @Inject OcmSchemeHandler schemeHandler;
  @Inject OcmStyleUi ocmStyleUi;
  @Inject OcmController ocmController;

  private OnRequiredLoginCallback onRequiredLoginCallback;
  private OnEventCallback onEventCallback;
  private String language;
  private InjectorImpl injector;
  private Map<String, String> localStorage;
  private OcmCredentialCallback ocmCredentialCallback;

  private OnCustomSchemeReceiver onCustomSchemeReceiver;

  static void initSdk(Application application) {
    instance = new OCManager();
    instance.initOcm(application);
  }

  static void setDoRequiredLoginCallback(OnRequiredLoginCallback onRequiredLoginCallback) {
    if (instance != null) {
      instance.onRequiredLoginCallback = onRequiredLoginCallback;
    }
  }

  static void setEventCallback(OnEventCallback onEventCallback) {
    if (instance != null) {
      instance.onEventCallback = onEventCallback;
    }
  }

  static void setContentLanguage(String language) {
    if (instance != null) {
      instance.language = language;
    }
  }

  static List<UiMenu> getMenus() {
    if (instance != null) {
      return instance.ocmViewGenerator.getMenu();
    }
    return null;
  }

  static UiGridBaseContentData generateGridView(String viewId, String filter) {
    return instance.ocmViewGenerator.generateGridView(viewId, filter);
  }

  static UiDetailBaseContentData generateDetailView(String elementUrl) {
    return instance.ocmViewGenerator.generateDetailView(elementUrl);
  }

  static UiSearchBaseContentData generateSearchView() {
    return instance.ocmViewGenerator.generateSearchView();
  }

  static void setUserIsAuthorizated(boolean isAuthorizated) {
    if (instance != null) {
      instance.authoritation.setAuthorizatedUser(isAuthorizated);
    }
  }

  static void setStyleUi(OcmStyleUiBuilder ocmUiBuilder) {
    if (instance != null) {
      instance.ocmStyleUi.setStyleUi(ocmUiBuilder);
    }
  }

  static void processDeepLinks(String path) {
    if (instance != null) {
      instance.schemeHandler.processScheme(path);
    }
  }

  public static Injector getInjector() {
    if (instance == null || instance.injector == null) {
      return null;
    }
    return instance.injector;
  }

  public static void notifyRequiredLoginToContinue() {
    if (instance != null && instance.onRequiredLoginCallback != null) {
      instance.onRequiredLoginCallback.doRequiredLogin();
    }
  }

  public static void notifyEvent(OcmEvent event, Object data) {
    if (instance != null && instance.onEventCallback != null) {
      instance.onEventCallback.doEvent(event, data);
    }
  }

  static void initOrchextra(String oxKey, String oxSecret, Class notificationActivityClass) {
    if (OCManager.instance != null) {
      Application app = (Application) instance.ocmContextProvider.getApplicationContext();
      OCManager.instance.initOrchextra(app, oxKey, oxSecret, notificationActivityClass);
    }
  }

  static void setOrchextraBusinessUnit(String businessUnit) {
    List<String> bussinessUnits = new ArrayList();
    bussinessUnits.add(businessUnit);
    Orchextra.setDeviceBusinessUnits(bussinessUnits);
    //Orchextra.commitConfiguration();
  }

  static void setNewOrchextraCredentials(final String apiKey, final String apiSecret,
      final OcmCredentialCallback ocmCredentialCallback) {

    instance.oxSession.setCredentials(apiKey, apiSecret);

    instance.ocmCredentialCallback = ocmCredentialCallback;

    Orchextra.start();

    //Some case the start() and changeCredentials() method has concurrency problems
    Orchextra.changeCredentials(apiKey, apiSecret);
  }

  static void bindUser(CrmUser crmUser) {
    Orchextra.bindUser(crmUser);
    Orchextra.commitConfiguration();
  }

  //region Orchextra method

  public static Map<String, String> getLocalStorage() {
    if (instance == null) {
      return null;
    }
    return instance.localStorage;
  }

  //region cid
  public static void setLocalStorage(Map<String, String> localStorage) {
    if (instance != null) {
      instance.localStorage = localStorage;
    }
  }

  public static String getContentLanguage() {
    if (instance == null) {
      return null;
    }
    return instance.language;
  }

  public static void setOnCustomSchemeReceiver(OnCustomSchemeReceiver onCustomSchemeReceiver) {
    OCManager.instance.onCustomSchemeReceiver = onCustomSchemeReceiver;
  }

  private void initOcm(Application app) {
    initDependencyInjection(app);
    initLifecyle(app);
  }

  private void initDependencyInjection(Application app) {
    OcmComponent ocmComponent = DaggerOcmComponent.builder().ocmModule(new OcmModule(app)).build();

    injector = new InjectorImpl(ocmComponent);

    ocmComponent.injectOcm(OCManager.instance);
  }

  //endregion

  private void initLifecyle(Application app) {
    app.registerActivityLifecycleCallbacks(ocmSdkLifecycle);
  }

  private void initOrchextra(Application app, String oxKey, String oxSecret,
      Class notificationActivityClass) {

    OrchextraBuilder builder = new OrchextraBuilder(app);
    builder.setApiKeyAndSecret(oxKey, oxSecret).setLogLevel(OrchextraLogLevel.NETWORK)
        //.setGcmSenderId("117687721829")       //TODO Test sender Id nuborisar
        // .setImageRecognitionModule(new ImageRecognitionVuforiaImpl())
        .setBackgroundBeaconScanMode(BeaconBackgroundModeScan.HARDCORE)
        .setOrchextraCompletionCallback(new OrchextraCompletionCallback() {
          @Override public void onSuccess() {
            Log.d("WOAH", "Orchextra initialized successfully");
          }

          @Override public void onError(String error) {
            Log.d("WOAH", "onError: " + error);

            if (error.equals("401") && instance.ocmCredentialCallback != null) {
              ocmCredentialCallback.onCredentailError(error);
            }
          }

          @Override public void onInit(String s) {
            Log.d("WOAH", "onInit: " + s);
          }

          @Override public void onConfigurationReceive(String accessToken) {
            Log.d("WOAH", "onConfigurationReceive: " + accessToken);

            instance.oxSession.setToken(accessToken);

            if (instance.ocmCredentialCallback != null) {
              ocmCredentialCallback.onCredentialReceiver(accessToken);
            }
          }
        });

    if (notificationActivityClass != null) {
      builder.setNotificationActivityClass(notificationActivityClass.toString());
    }

    Orchextra.initialize(builder);

    Orchextra.setCustomSchemeReceiver(onOxCustomSchemeReceiver);
  }

  private CustomSchemeReceiver onOxCustomSchemeReceiver = new CustomSchemeReceiver() {
    @Override public void onReceive(String customScheme) {
      returnOcCustomSchemeCallback(customScheme);
    }
  };

  public static void clearCache() {
    OCManager instance = OCManager.instance;
    if (instance != null) {
      instance.ocmController.clearCache();
    }
  }

  public static void start() {
    Orchextra.start();
  }

  public static void returnOcCustomSchemeCallback(String customScheme) {
    if (instance.onCustomSchemeReceiver != null) {
      instance.onCustomSchemeReceiver.onReceive(customScheme);
    }
  }

  //endregion
}

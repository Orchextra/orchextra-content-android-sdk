package com.gigigo.orchextra.ocm;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebStorage;
import android.widget.ImageView;
import com.gigigo.imagerecognitioninterface.ImageRecognition;
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
import com.gigigo.orchextra.core.sdk.model.detail.DetailActivity;
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
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import orchextra.javax.inject.Inject;

public final class OCManager {

  static Application mApplication;
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
  private boolean isShowReadedArticlesInGrayScale = false;
  //cambio para el inicio selectivo, MEJORAR,
  //necesitamos un contexto para q la funcion setNewOrchextracredentials pueda comprobar las preferences
  //lo suyo es no guardarlo en las preferences, de momneto así y una mejora sencilla seria añadir el contexto a
  //la funcion de setNewOrchextracredentials, para no mantener el application cuando no es necesario
  private CustomSchemeReceiver onOxCustomSchemeReceiver = new CustomSchemeReceiver() {
    @Override public void onReceive(String customScheme) {
      returnOcCustomSchemeCallback(customScheme);
    }
  };

  static void initSdk(Application application) {
    getInstance();
    instance.initOcm(application);
    mApplication = application;//
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

  static void getMenus(boolean forceReload, final OCManagerCallbacks.Menus menusCallback) {
    if (instance != null) {
      instance.ocmViewGenerator.getMenu(forceReload,
          new OcmViewGenerator.GetMenusViewGeneratorCallback() {
            @Override public void onGetMenusLoaded(List<UiMenu> menus) {
              menusCallback.onMenusLoaded(menus);
            }

            @Override public void onGetMenusFails(Throwable e) {
              menusCallback.onMenusFails(e);
            }
          });
    }
  }

  static void generateSectionView(String viewId, String filter, int imagesToDownload,
      final OCManagerCallbacks.Section sectionCallback) {
    instance.ocmViewGenerator.generateSectionView(viewId, filter, imagesToDownload,
        new OcmViewGenerator.GetSectionViewGeneratorCallback() {
          @Override public void onSectionViewLoaded(UiGridBaseContentData uiGridBaseContentData) {
            sectionCallback.onSectionLoaded(uiGridBaseContentData);
          }

          @Override public void onSectionViewFails(Exception e) {
            sectionCallback.onSectionFails(e);
          }
        });
  }

  public static void clearData(boolean images, boolean data,
      final OCManagerCallbacks.Clear clearCallback) {
    if (instance != null) {

      instance.ocmController.clearCache(images, data, new OcmController.ClearCacheCallback() {
        @Override public void onClearCacheSuccess() {
          // clearCookiesFedexAuth();
          clearCallback.onDataClearedSuccessfull();
        }

        @Override public void onClearCacheFails(Exception e) {
          clearCallback.onDataClearFails(e);
        }
      });
    }
  }

  public static UiDetailBaseContentData generateDetailView(String elementUrl) {
    return instance.ocmViewGenerator.generateDetailView(elementUrl);
  }

  public static void generateDetailView(String elementUrl, String urlImageToExpand, int widthScreen,
      int heightScreen, ImageView imageViewToExpandInDetail) {

    if (instance != null) {
      instance.schemeHandler.processElementUrl(elementUrl, urlImageToExpand, widthScreen,
          heightScreen, imageViewToExpandInDetail);
    }
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
      instance.schemeHandler.processElementUrl(path);
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

  public static void notifyEvent(OcmEvent event) {
    if (instance != null && instance.onEventCallback != null) {
      instance.onEventCallback.doEvent(event);
    }
  }

  static void initOrchextra(String oxKey, String oxSecret, Class notificationActivityClass,
      String senderId) {
    if (OCManager.instance != null) {
      Application app = (Application) instance.ocmContextProvider.getApplicationContext();
      OCManager.instance.initOrchextra(app, oxKey, oxSecret, notificationActivityClass, senderId);
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

    Orchextra.start(); //this is new for repsol, esto hace q el primer changecredentials pase por el 401 y llege correctamente el token

    //Some case the start() and changeCredentials() method has concurrency problems
    Orchextra.updateSDKCredentials(apiKey, apiSecret, true);
  }

  static void bindUser(CrmUser crmUser) {
    Orchextra.bindUser(crmUser);
    Orchextra.commitConfiguration();
  }

  //region Orchextra method

  public static Map<String, String> getLocalStorage() {
    if (instance == null) {
      System.out.println("main getLocalStorageinstance ==null");
      return null;
    } else {
      System.out.println("main getLocalStorageinstance!==null");
    }
    return instance.localStorage;
  }

  //region cid
  public static void setLocalStorage(Map<String, String> localStorage) {
    if (instance != null) {
      instance.localStorage = localStorage;
      System.out.println("main setLocalStorage we have localstorage");
    } else {
      System.out.println("main setLocalStorage we have NOT localstorage");
    }
  }

  public static String getContentLanguage() {
    if (instance == null) {
      return null;
    }
    return instance.language;
  }

  static void setContentLanguage(String language) {
    if (instance != null) {
      instance.language = language;
    }
  }

  public static void setOnCustomSchemeReceiver(OnCustomSchemeReceiver onCustomSchemeReceiver) {
    OCManager.instance.onCustomSchemeReceiver = onCustomSchemeReceiver;
  }

  public static void start() {
    Orchextra.start();
  }

  //endregion

  public static void stop() {
    Orchextra.stop();
  }

  public static void returnOcCustomSchemeCallback(String customScheme) {
    if (instance.onCustomSchemeReceiver != null) {
      instance.onCustomSchemeReceiver.onReceive(customScheme);
    }
  }

  public static OcmContextProvider getOcmContextProvider() {
    OCManager instance = OCManager.instance;
    if (instance == null) {
      return null;
    }
    return OCManager.instance.ocmContextProvider;
  }

  static void closeDetailView() {
    OCManager instance = OCManager.instance;
    if (instance != null && instance.ocmContextProvider != null) {
      Activity currentActivity = instance.ocmContextProvider.getCurrentActivity();
      if (currentActivity != null && currentActivity instanceof DetailActivity) {
        currentActivity.finish();
      }
    }
  }

  public static synchronized OCManager getInstance() {
    if (instance != null) {
      return instance;
    }

    instance = new OCManager();
    return instance;
  }

  static void initOrchextra(String oxKey, String oxSecret, Class notificationActivityClass,
      String senderId, ImageRecognition vuforia) {
    if (OCManager.instance != null) {
      Application app = (Application) instance.ocmContextProvider.getApplicationContext();
      OCManager.instance.initOrchextra(app, oxKey, oxSecret, notificationActivityClass, senderId,
          vuforia);
    }
  }

  public static void showIconNewContent() {

  }

  private void initOcm(Application app) {
    initDependencyInjection(app);
    initLifecyle(app);
  }

  //endregion

  private void initDependencyInjection(Application app) {
    OcmComponent ocmComponent = DaggerOcmComponent.builder().ocmModule(new OcmModule(app)).build();

    injector = new InjectorImpl(ocmComponent);

    ocmComponent.injectOcm(OCManager.instance);
  }

  private void initLifecyle(Application app) {
    app.registerActivityLifecycleCallbacks(ocmSdkLifecycle);
  }

  static OrchextraCompletionCallback mOrchextraCompletionCallback =
      new OrchextraCompletionCallback() {
        @Override public void onSuccess() {
          Log.d("WOAH", "Orchextra initialized successfully");
        }

        @Override public void onError(String error) {
          Log.d("WOAH", "onError: " + error);
          //new Handler(Looper.getMainLooper()).post(new Runnable() {
          //  @Override public void run() {
          //    Toast.makeText(mApplication, "onError:  app" + error, Toast.LENGTH_LONG).show();
          //  }
          //});
          if (error.equals("401") && instance.ocmCredentialCallback != null) {
            instance.ocmCredentialCallback.onCredentailError(error);
          }
        }

        @Override public void onInit(String s) {
          Log.d("WOAH", "onInit: " + s);
          //asvox aki es cuando se va a background , en estepunto ox ya ha recuperado la config anterior(buena)
          //y cuando llega a onSuccess se rompio del todo

        }

        @Override public void onConfigurationReceive(String accessToken) {
          Log.d("WOAH", "onConfigurationReceive: " + accessToken);
          //new Handler(Looper.getMainLooper()).post(new Runnable() {
          //  @Override public void run() {
          //    Toast.makeText(mApplication, "onConfigurationReceive:  app" + accessToken, Toast.LENGTH_LONG).show();
          //  }
          //});
          instance.oxSession.setToken(accessToken);

          if (instance.ocmCredentialCallback
              != null) { //asv esto indica q se hace el changecredentials
            instance.ocmCredentialCallback.onCredentialReceiver(accessToken);
          }
        }
      };

  private void initOrchextra(Application app, String oxKey, String oxSecret,
      Class notificationActivityClass, String senderId) {
    initOrchextra(app, oxKey, oxSecret, notificationActivityClass, senderId, null);
  }

  private void initOrchextra(Application app, String oxKey, String oxSecret,
      Class notificationActivityClass, String senderId, ImageRecognition vuforia) {

    OrchextraBuilder builder = new OrchextraBuilder(app);
    builder.setApiKeyAndSecret(oxKey, oxSecret)
        .setLogLevel(OrchextraLogLevel.NETWORK)
        .setBackgroundBeaconScanMode(BeaconBackgroundModeScan.HARDCORE)
        .setOrchextraCompletionCallback(mOrchextraCompletionCallback);

    if (notificationActivityClass != null) {
      builder.setNotificationActivityClass(notificationActivityClass.toString());
    }
    if (senderId != null && senderId != "") {
      builder.setGcmSenderId(senderId);
    }
    if (vuforia != null) {
      builder.setImageRecognitionModule(vuforia);
    }
    Orchextra.initialize(builder);

    Orchextra.setCustomSchemeReceiver(onOxCustomSchemeReceiver);
  }

  //region cookies FedexAuth
  public static void clearCookiesFedexAuth() {

    //if (instance != null) {
    WebStorage.getInstance().deleteAllData();
      /*
      //recuperar url del preferences
      //dominio url ".facebook.com"
      String urlFedexAuth = "";
      //eliminar las cookies de esa url
      SharedPreferences prefs = instance.ocmContextProvider.getApplicationContext()
          .getSharedPreferences(Ocm.OCM_PREFERENCES, Context.MODE_PRIVATE);

      urlFedexAuth = prefs.getString(Ocm.OCM_FEDEX_AUTH_URL, "");
      if (!urlFedexAuth.equals("")) {
        android.webkit.CookieManager.getInstance().setCookie(urlFedexAuth, "locale=");
        android.webkit.CookieManager.getInstance().setCookie(urlFedexAuth, "datr=");
        android.webkit.CookieManager.getInstance().setCookie(urlFedexAuth, "s=");
        android.webkit.CookieManager.getInstance().setCookie(urlFedexAuth, "csm=");
        android.webkit.CookieManager.getInstance().setCookie(urlFedexAuth, "fr=");
        android.webkit.CookieManager.getInstance().setCookie(urlFedexAuth, "lu=");
        android.webkit.CookieManager.getInstance().setCookie(urlFedexAuth, "c_user=");
        android.webkit.CookieManager.getInstance().setCookie(urlFedexAuth, "xs=");

      }*/
    //}
  }

  /* public static void saveFedexAuth(String url) {
     if (instance != null) {
       SharedPreferences prefs = instance.ocmContextProvider.getApplicationContext()
           .getSharedPreferences(Ocm.OCM_PREFERENCES, Context.MODE_PRIVATE);
       SharedPreferences.Editor edit = prefs.edit();
       edit.putString(Ocm.OCM_FEDEX_AUTH_URL, url);
       edit.apply();
     }
   }
 */
  public static void addArticleToReadedArticles(String articleSlug) {
    if (instance != null && instance.isShowReadedArticlesInGrayScale) {
      SharedPreferences prefs = instance.ocmContextProvider.getApplicationContext()
          .getSharedPreferences(Ocm.OCM_PREFERENCES, Context.MODE_PRIVATE);
      String ArrayReadedArticlesSlug = prefs.getString(Ocm.OCM_READED_ARTICLES, "");

      SharedPreferences.Editor edit = prefs.edit();
      edit.putString(Ocm.OCM_READED_ARTICLES, ArrayReadedArticlesSlug + "|" + articleSlug + "|");
      edit.apply();
    }
  }

  public static boolean isThisArticleReaded(String articleSlug) {
    if (instance != null && instance.isShowReadedArticlesInGrayScale) {
      SharedPreferences prefs = instance.ocmContextProvider.getApplicationContext()
          .getSharedPreferences(Ocm.OCM_PREFERENCES, Context.MODE_PRIVATE);
      String ArrayReadedArticlesSlug = prefs.getString(Ocm.OCM_READED_ARTICLES, "");
      if (ArrayReadedArticlesSlug.contains("|" + articleSlug + "|")) {
        return true;
      }

      return false;
    } else {
      return false;
    }
  }

  /***
   * you must to reload grid in onResume if this feature are enabled
   * @param showReadArticles
   */
  public static void setShowReadArticles(boolean showReadArticles) {

    if (instance != null) {
      instance.isShowReadedArticlesInGrayScale = showReadArticles;
    }
  }

  public static boolean getShowReadArticles() {

    if (instance != null) {
      return instance.isShowReadedArticlesInGrayScale;
    } else {
      return false;
    }
  }

  //endregion
  //todo readed articles

  public static void setBitmapTransformReadArticles(
      com.bumptech.glide.load.Transformation<Bitmap> transformBitmap) {
    if (getInstance() != null) getInstance().readArticlesBitmapTransform = transformBitmap;
  }

  //public static int transform = -1;
  com.bumptech.glide.load.Transformation<Bitmap> readArticlesBitmapTransform ;

  public static com.bumptech.glide.load.Transformation<Bitmap> getBitmapTransformReadArticles() {
    if (getInstance() != null) {
      return getInstance().readArticlesBitmapTransform;
    } else {
      return new GrayscaleTransformation(mApplication);
    }
    //region normal Filters
/*
    switch (transform) {
      case 0:
        return new GrayscaleTransformation(mApplication);
      case 1:
        return new BlurTransformation(mApplication);
      case 2:
        return new ColorFilterTransformation(mApplication, Color.RED);
      case 3:
        return new CropCircleTransformation(mApplication);
      case 4:
        return new CropSquareTransformation(mApplication);
      case 5:
        return new RoundedCornersTransformation(mApplication, 25,
            25);//return new MaskTransformation(mApplication, 50); //fails
      case 6:
        return new RoundedCornersTransformation(mApplication, 25, 25);
      case 7:
        return new BrightnessFilterTransformation(mApplication);
      case 8:
        return new ContrastFilterTransformation(mApplication);
      case 9:
        return new InvertFilterTransformation(mApplication);
      case 10:
        return new KuwaharaFilterTransformation(mApplication);
      case 11:
        return new PixelationFilterTransformation(mApplication);
      case 12:
        return new SepiaFilterTransformation(mApplication);
      case 13:
        return new SketchFilterTransformation(mApplication);
      case 14:
        return new SwirlFilterTransformation(mApplication);
      case 15:
        return new ToonFilterTransformation(mApplication);
      default:
        transform = -1;
        return new VignetteFilterTransformation(mApplication);
    }
     */

  }
}

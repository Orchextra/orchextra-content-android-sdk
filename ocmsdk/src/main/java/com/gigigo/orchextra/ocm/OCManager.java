package com.gigigo.orchextra.ocm;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebStorage;
import android.widget.ImageView;
import com.gigigo.orchextra.core.controller.OcmViewGenerator;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.menus.DataRequest;
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
import com.gigigo.orchextra.ocm.callbacks.OcmCredentialCallback;
import com.gigigo.orchextra.ocm.callbacks.OnChangedMenuCallback;
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver;
import com.gigigo.orchextra.ocm.callbacks.OnEventCallback;
import com.gigigo.orchextra.ocm.callbacks.OnLoadContentSectionFinishedCallback;
import com.gigigo.orchextra.ocm.customProperties.OcmCustomBehaviourDelegate;
import com.gigigo.orchextra.ocm.customProperties.ViewCustomizationType;
import com.gigigo.orchextra.ocm.customProperties.ViewType;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.ocm.dto.UiMenuData;
import com.gigigo.orchextra.ocm.views.UiDetailBaseContentData;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import com.gigigo.orchextra.ocm.views.UiSearchBaseContentData;
import com.gigigo.orchextra.wrapper.CrmUser;
import com.gigigo.orchextra.wrapper.ImageRecognition;
import com.gigigo.orchextra.wrapper.OrchextraCompletionCallback;
import com.gigigo.orchextra.wrapper.OxManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import orchextra.javax.inject.Inject;
import org.jetbrains.annotations.NotNull;

public final class OCManager {

  private static OCManager instance;
  private static OrchextraCompletionCallback mOrchextraCompletionCallback =
      new OrchextraCompletionCallback() {
        @Override public void onSuccess() {
          Log.d("OCM", "Orchextra initialized successfully");
        }

        @Override public void onError(String error) {
          Log.d("OCM", "onError: " + error);
          //new Handler(Looper.getMainLooper()).post(new Runnable() {
          //  @Override public void run() {
          //    Toast.makeText(mApplication, "onError:  app" + error, Toast.LENGTH_LONG).show();
          //  }
          //});
          //performance for use ox3 we need to change the old code error of ox2.0 401 becomes 2000 now x example
          /*
          NoDatabase:{
       code:1100,
       message:'No database connection'
   },
   InvalidCredentials:{
       code:2001,
       statusCode: 403,
       message:'Invalid credentials supplied'
   },
   Unauthorized:{
       code:2002,
       statusCode: 401,
       message:'Unauthorized'
   },
   ProjectNotFound:{
       code:2000,
       statusCode: 404,
       message:'Project not found'
   },
   InvalidJSON:{
       code:2003,
       statusCode: 400,
       message:'Invalid JSON body',
       conversion: err =>
           err.name == 'SyntaxError'
           && (err.message.indexOf('Unexpected token')===0
           || err.message.indexOf('Unexpected string in JSON')===0)
   },
   ValidationError:{
       code:3000,
       statusCode: 400,
       message: 'Validation Error',
Add Comment C
           */

          //asv in ox 1.0 && ox 2.0 invalid credentials/or invalid enviroment(credentials from pro in stagign endpoint
          //was 401, in this case the ox onError must be throw to ocm credentials callback
          //in ox 3.0 the back error code will be 2000, for the same problem
          if ((error.equals("401") || error.equals("2000"))
              && instance.ocmCredentialCallback != null) {
            instance.ocmCredentialCallback.onCredentailError(error);
          }
        }

        @Override public void onInit(String s) {
          Log.d("OCM", "onInit: " + s);
          //asvox aki es cuando se va a background , en estepunto ox ya ha recuperado la config anterior(buena)
          //y cuando llega a onSuccess se rompio del todo

        }

        @Override public void onConfigurationReceive(String accessToken) {
          Log.d("OCM", "onConfigurationReceive: " + accessToken);
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
  //region serialize list of read articles slugs
  private final String READ_ARTICLES_FILE = "read_articles_file.ocm";
  @Inject OxManager oxManager;
  @Inject OcmSdkLifecycle ocmSdkLifecycle;
  @Inject OcmContextProvider ocmContextProvider;
  @Inject OcmViewGenerator ocmViewGenerator;
  @Inject OxSession oxSession;
  @Inject Authoritation authoritation;
  @Inject OcmSchemeHandler schemeHandler;
  @Inject OcmStyleUi ocmStyleUi;
  @Inject OcmController ocmController;
  private OnEventCallback onEventCallback;
  private String language;
  private InjectorImpl injector;
  private Map<String, String> localStorage;
  private OcmCredentialCallback ocmCredentialCallback;
  private OnChangedMenuCallback onChangedMenuCallback;
  private OnLoadContentSectionFinishedCallback onLoadContentSectionFinishedCallback;
  private OcmCustomBehaviourDelegate ocmCustomBehaviourDelegate;
  private UiMenu uiMenuToNotifyWhenSectionIsLoaded;
  private boolean isShowReadedArticles = false;
  private int maxReadArticles = 100;
  private com.bumptech.glide.load.Transformation<Bitmap> readArticlesBitmapTransform;

  static void initSdk(Application application) {
    getInstance().initOcm(application);
  }

  static void setCustomBehaviourDelegate(
      OcmCustomBehaviourDelegate ocmCustomBehaviourDelegate) {
    getInstance().ocmCustomBehaviourDelegate = ocmCustomBehaviourDelegate;
  }

  static void setEventCallback(OnEventCallback onEventCallback) {
    getInstance().onEventCallback = onEventCallback;
  }

  static void getMenus(DataRequest menuRequest, final OCManagerCallbacks.Menus menusCallback) {
    final long time = System.currentTimeMillis();
    if (instance != null) {
      instance.ocmViewGenerator.getMenu(menuRequest,
          new OcmViewGenerator.GetMenusViewGeneratorCallback() {
            @Override public void onGetMenusLoaded(UiMenuData menus) {
              Log.v("TT - LOADED menus", (System.currentTimeMillis() - time) / 1000 + "");
              if (menus != null
                  && menus.getUiMenuList() != null
                  && menus.getUiMenuList().size() > 0) {
                instance.uiMenuToNotifyWhenSectionIsLoaded = menus.getUiMenuList().get(0);
              }

              menusCallback.onMenusLoaded(menus);
            }

            @Override public void onGetMenusFails(Throwable e) {
              menusCallback.onMenusFails(e);
            }
          });
    }
  }

  static void generateSectionView(UiMenu uiMenu, String filter, int imagesToDownload,
      final OCManagerCallbacks.Section sectionCallback) {
    final long time = System.currentTimeMillis();

    instance.ocmViewGenerator.generateSectionView(uiMenu, filter, imagesToDownload,
        new OcmViewGenerator.GetSectionViewGeneratorCallback() {
          @Override public void onSectionViewLoaded(UiGridBaseContentData uiGridBaseContentData) {
            Log.v("TT - LOADED sections", (System.currentTimeMillis() - time) / 1000 + "");
            sectionCallback.onSectionLoaded(uiGridBaseContentData);
          }

          @Override public void onSectionViewFails(Exception e) {
            sectionCallback.onSectionFails(e);
          }
        });
  }

  static void clearData(boolean images, boolean data,
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

  static void processDeepLinks(String elementUrl) {
    if (instance != null) {
      instance.schemeHandler.processElementUrl(elementUrl);
    }
  }

  public static void processElementUrl(String elementUrl, ImageView imageViewToExpandInDetail, OcmSchemeHandler.ProcessElementCallback processElementCallback) {

    if (instance != null) {
      instance.schemeHandler.processElementUrl(elementUrl, imageViewToExpandInDetail, processElementCallback);
    }
  }

  static UiDetailBaseContentData generateDetailView(String elementUrl) {
    return instance.ocmViewGenerator.generateDetailView(elementUrl);
  }

  static UiSearchBaseContentData generateSearchView() {
    return instance.ocmViewGenerator.generateSearchView();
  }

  static void setStyleUi(OcmStyleUiBuilder ocmUiBuilder) {
    if (instance != null) {
      instance.ocmStyleUi.setStyleUi(ocmUiBuilder);
    }
  }

  public static Injector getInjector() {
    if (instance == null || instance.injector == null) {
      return null;
    }
    return instance.injector;
  }

  public static void notifyCustomBehaviourContinue(@NotNull Map<String, Object> customProperties,
      ViewType viewType, Function1<Boolean, Unit> completion) {
    if (instance != null && instance.ocmCustomBehaviourDelegate != null) {
      instance.ocmCustomBehaviourDelegate.contentNeedsValidation(customProperties, viewType,
          completion);
    }
  }

  public static void notifyCustomizationForContent(@NotNull Map<String, Object> customProperties,
      ViewType viewType,
      Function1<? super List<? extends ViewCustomizationType>, Unit> customizationListener) {
    if (instance != null && instance.ocmCustomBehaviourDelegate != null) {
      instance.ocmCustomBehaviourDelegate.customizationForContent(customProperties, viewType,
          customizationListener);
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
    instance.oxManager.bindDevice(businessUnit);
  }

  //region Orchextra method

  static void setNewOrchextraCredentials(final String apiKey, final String apiSecret,
      final OcmCredentialCallback ocmCredentialCallback) {

    instance.oxSession.setCredentials(apiKey, apiSecret);

    instance.ocmCredentialCallback = ocmCredentialCallback;

    //this is new for repsol, esto hace q el primer changecredentials pase por el 401 y llege correctamente el token
    instance.oxManager.start();

    //Some case the start() and changeCredentials() method has concurrency problems
    instance.oxManager.updateSDKCredentials(apiKey, apiSecret, true);
  }

  public static void start(OcmCredentialCallback onCredentialCallback) {
    instance.ocmCredentialCallback = onCredentialCallback;
    instance.oxManager.start();
  }

  static void bindUser(CrmUser crmUser) {
    instance.oxManager.bindUser(crmUser);
  }

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

  //endregion

  static void setContentLanguage(String language) {
    getInstance().language = language;
  }

  public static void setOnCustomSchemeReceiver(OnCustomSchemeReceiver onCustomSchemeReceiver) {
    OCManager.instance.oxManager.setOnCustomSchemeReceiver(onCustomSchemeReceiver);
  }

  public static void start() {
    instance.oxManager.start();
  }

  public static void stop() {
    instance.oxManager.stop();
  }

  public static void returnOcCustomSchemeCallback(String customScheme) {

    instance.oxManager.callOnCustomSchemeReceiver(customScheme);
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

  //endregion

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

  public static void addArticleToReadedArticles(String articleSlug) {
    if (instance != null && instance.isShowReadedArticles) {
      ArrayList<String> lstReadArticles = instance.readReadArticles();
      if (lstReadArticles.size() == instance.maxReadArticles) {
        lstReadArticles.remove(instance.maxReadArticles - 1);
      }
      lstReadArticles.add(0, articleSlug);
      instance.writeReadArticles(lstReadArticles);
    }
  }

  public static boolean isThisArticleReaded(String articleSlug) {
    if (instance != null && instance.isShowReadedArticles) {
      ArrayList<String> ArrayReadedArticlesSlug = instance.readReadArticles();
      if (ArrayReadedArticlesSlug.indexOf(articleSlug) > -1) {
        return true;
      }
      return false;
    } else {
      return false;
    }
  }

  public static <T extends Serializable> void saveSerializable(Context context, T objectToSave,
      String fileName) {
    try {
      FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

      objectOutputStream.writeObject(objectToSave);

      objectOutputStream.close();
      fileOutputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
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

  public static <T extends Serializable> T readSerializable(Context context, String fileName) {
    T objectToReturn = null;

    try {
      FileInputStream fileInputStream = context.openFileInput(fileName);
      ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
      objectToReturn = (T) objectInputStream.readObject();

      objectInputStream.close();
      fileInputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return objectToReturn;
  }

  public static void removeSerializable(Context context, String filename) {
    context.deleteFile(filename);
  }

  public static boolean isFileExists(String fileName) {

    File file = new File(fileName);
    if (file.exists()) {
      return true;
    } else {
      return false;
    }
  }

  public static void setMaxReadArticles(int maxReadArticles) {

    if (instance != null) {
      instance.maxReadArticles = maxReadArticles;
    }
  }

  public static boolean getShowReadArticles() {
    if (instance != null) {
      return instance.isShowReadedArticles;
    } else {
      return false;
    }
  }

  /***
   * you must to reload grid in onResume if this feature are enabled
   * @param showReadArticles
   */
  public static void setShowReadArticles(boolean showReadArticles) {
    getInstance().isShowReadedArticles = showReadArticles;
  }

  public static com.bumptech.glide.load.Transformation<Bitmap> getBitmapTransformReadArticles() {
    if (getInstance() != null) {
      return getInstance().readArticlesBitmapTransform;
    } else {
      return null;
    }
  }

  public static void setBitmapTransformReadArticles(
      com.bumptech.glide.load.Transformation<Bitmap> transformBitmap) {
    getInstance().readArticlesBitmapTransform = transformBitmap;
  }

  public static void setOnChangedMenuCallback(OnChangedMenuCallback onChangedMenuCallback) {
    OCManager instance = getInstance();
    if (instance != null) {
      instance.onChangedMenuCallback = onChangedMenuCallback;
    }
  }

  //endregion

  public static boolean hasOnChangedMenuCallback() {
    OCManager instance = getInstance();
    return instance != null && instance.onChangedMenuCallback != null;
  }

  public static void notifyOnMenuChanged(UiMenuData menus) {
    OCManager instance = getInstance();
    if (instance != null && instance.onChangedMenuCallback != null) {
      instance.onChangedMenuCallback.onChangedMenu(menus);
    }
  }

  public static void setOnLoadDataContentSectionFinished(
      OnLoadContentSectionFinishedCallback onLoadContentSectionFinishedCallback) {
    OCManager instance = getInstance();
    if (instance != null && onLoadContentSectionFinishedCallback != null) {
      instance.onLoadContentSectionFinishedCallback = onLoadContentSectionFinishedCallback;
    }
  }

  //endregion
  //todo readed articles

  public static void notifyOnLoadDataContentSectionFinished(UiMenu menuToNotify) {
    OCManager instance = getInstance();
    if (instance != null
        && instance.onLoadContentSectionFinishedCallback != null
        && instance.uiMenuToNotifyWhenSectionIsLoaded != null) {
      if (instance.uiMenuToNotifyWhenSectionIsLoaded.equals(menuToNotify)) {
        instance.onLoadContentSectionFinishedCallback.onLoadContentSectionFinished();
      }
    }
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

  private void initLifecyle(Application app) {
    app.registerActivityLifecycleCallbacks(ocmSdkLifecycle);
  }

  private void initOrchextra(Application app, String oxKey, String oxSecret,
      Class notificationActivityClass, String senderId) {
    initOrchextra(app, oxKey, oxSecret, notificationActivityClass, senderId, null);
  }

  private void initOrchextra(Application app, String oxKey, String oxSecret,
      Class notificationActivityClass, String senderId, ImageRecognition vuforia) {
    System.out.println("appOn6.6.1");
    OxManager.Config config = new OxManager.Config.Builder().setApiKey(oxKey)
        .setApiSecret(oxSecret)
        .setNotificationActivityClass(notificationActivityClass)
        .setSenderId(senderId)
        .setVuforia(vuforia)
        .setOrchextraCompletionCallback(mOrchextraCompletionCallback)
        .build();
    System.out.println("appOn6.6.2");
    instance.oxManager.init(app, config);
  }

  public ArrayList<String> readReadArticles() {

    ArrayList<String> lst =
        readSerializable(ocmContextProvider.getApplicationContext(), READ_ARTICLES_FILE);
    if (lst != null) {
      return lst;
    } else {
      return new ArrayList<>();
    }
  }

  public void writeReadArticles(ArrayList<String> readArticles) {
    saveSerializable(ocmContextProvider.getApplicationContext(), readArticles, READ_ARTICLES_FILE);
  }
}

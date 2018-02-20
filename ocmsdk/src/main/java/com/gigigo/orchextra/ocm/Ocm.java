package com.gigigo.orchextra.ocm;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.gigigo.orchextra.core.controller.model.home.ImageTransformReadArticle;
import com.gigigo.orchextra.core.domain.entities.menus.DataRequest;
import com.gigigo.orchextra.ocm.callbacks.OcmCredentialCallback;
import com.gigigo.orchextra.ocm.callbacks.OnChangedMenuCallback;
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver;
import com.gigigo.orchextra.ocm.callbacks.OnLoadContentSectionFinishedCallback;
import com.gigigo.orchextra.ocm.callbacks.OnRequiredLoginCallback;
import com.gigigo.orchextra.ocm.customProperties.OcmCustomBehaviourDelegate;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.ocm.dto.UiMenuData;
import com.gigigo.orchextra.ocm.views.UiDetailBaseContentData;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import com.gigigo.orchextra.ocm.views.UiSearchBaseContentData;
import com.gigigo.orchextra.wrapper.CrmUser;
import java.util.Map;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;

public final class Ocm {

  private static QueryStringGenerator queryStringGenerator;

  public static final String OCM_PREFERENCES = "OCMpreferencez";
  public static final String OCM_CHANGE_CREDENTIALS_DONE = "ChangeCredentialsDONE";

  public static void initialize(Application app) {

    OcmBuilder ocmBuilder = new OcmBuilder(app);
    String oxKey = "FAKE_KEY";
    String oxSecret = "FAKE_SECRET";
    Class notificationActivityClass = ocmBuilder.getNotificationActivityClass();

    //Initialization has to be done after setting callbacks because getting them could be null.
    OCManager.initSdk(ocmBuilder.getApp());

    OCManager.setContentLanguage(ocmBuilder.getContentLanguage());
    OCManager.setDoRequiredLoginCallback(ocmBuilder.getOnRequiredLoginCallback());
    OCManager.setEventCallback(ocmBuilder.getOnEventCallback());

    OCManager.setShowReadArticles(ocmBuilder.getShowReadArticles());
    if (ocmBuilder.getShowReadArticles() && ocmBuilder.getTransformReadArticleMode()
        .equals(ImageTransformReadArticle.BITMAP_TRANSFORM)) {
      if (ocmBuilder.getCustomBitmapTransformReadArticle() == null) {
        OCManager.setBitmapTransformReadArticles(
            new GrayscaleTransformation(app.getApplicationContext()));
      } else {
        OCManager.setBitmapTransformReadArticles(ocmBuilder.getCustomBitmapTransformReadArticle());
      }
    }

    SharedPreferences prefs =
        ocmBuilder.getApp().getSharedPreferences(OCM_PREFERENCES, Context.MODE_PRIVATE);
    boolean IsCredentialsChanged = prefs.getBoolean(OCM_CHANGE_CREDENTIALS_DONE, false);

    if (!IsCredentialsChanged) {
      OCManager.initOrchextra(oxKey, oxSecret, notificationActivityClass,
          ocmBuilder.getOxSenderId());
      start();
    }
  }

  public static void initializeWithChangeCredentials(OcmBuilder ocmBuilder) {
    String oxKey = "FAKE_KEY";
    String oxSecret = "FAKE_SECRET";

    Class notificationActivityClass = ocmBuilder.getNotificationActivityClass();

    OCManager.initSdk(ocmBuilder.getApp());
    OCManager.setContentLanguage(ocmBuilder.getContentLanguage());
    OCManager.setDoRequiredLoginCallback(ocmBuilder.getOnRequiredLoginCallback());
    OCManager.setEventCallback(ocmBuilder.getOnEventCallback());

    OCManager.setShowReadArticles(ocmBuilder.getShowReadArticles());
    if (ocmBuilder.getShowReadArticles() && ocmBuilder.getTransformReadArticleMode()
        .equals(ImageTransformReadArticle.BITMAP_TRANSFORM)) {
      if (ocmBuilder.getCustomBitmapTransformReadArticle() == null) {
        OCManager.setBitmapTransformReadArticles(
            new GrayscaleTransformation(ocmBuilder.getApp().getApplicationContext()));
      } else {
        OCManager.setBitmapTransformReadArticles(ocmBuilder.getCustomBitmapTransformReadArticle());
      }
    }
    if (ocmBuilder.getShowReadArticles()) {
      OCManager.setMaxReadArticles(ocmBuilder.getMaxReadArticles());
    }

    if (ocmBuilder.getVuforiaImpl() != null) {
      OCManager.initOrchextra(oxKey, oxSecret, notificationActivityClass,
          ocmBuilder.getOxSenderId(), ocmBuilder.getVuforiaImpl());
    } else {
      OCManager.initOrchextra(oxKey, oxSecret, notificationActivityClass,
          ocmBuilder.getOxSenderId());
    }

    Ocm.start();
  }

  /**
   * Initialize the sdk. This method must be initialized in the onCreate method of the Application
   * class
   */
  public static void initialize(OcmBuilder ocmBuilder) {
    System.out.println("appOn6.1");
    Application app = ocmBuilder.getApp();
    System.out.println("appOn6.1");
    String oxKey = ocmBuilder.getOxKey();
    String oxSecret = ocmBuilder.getOxSecret();
    Class notificationActivityClass = ocmBuilder.getNotificationActivityClass();
    System.out.println("appOn6.2");
    OCManager.setContentLanguage(ocmBuilder.getContentLanguage());
    OCManager.setDoRequiredLoginCallback(ocmBuilder.getOnRequiredLoginCallback());
    OCManager.setEventCallback(ocmBuilder.getOnEventCallback());
    System.out.println("appOn6.3");
    OCManager.initSdk(app);
    System.out.println("appOn6.4");
    OCManager.setShowReadArticles(ocmBuilder.getShowReadArticles());
    if (ocmBuilder.getShowReadArticles() && ocmBuilder.getTransformReadArticleMode()
        .equals(ImageTransformReadArticle.BITMAP_TRANSFORM)) {
      if (ocmBuilder.getCustomBitmapTransformReadArticle() == null) {
        OCManager.setBitmapTransformReadArticles(
            new GrayscaleTransformation(ocmBuilder.getApp().getApplicationContext()));
      } else {
        OCManager.setBitmapTransformReadArticles(ocmBuilder.getCustomBitmapTransformReadArticle());
      }
    }
    System.out.println("appOn6.5");
    if (ocmBuilder.getShowReadArticles()) {
      OCManager.setMaxReadArticles(ocmBuilder.getMaxReadArticles());
    }
    System.out.println("appOn6.6");
    if (ocmBuilder.getVuforiaImpl() != null) {
      OCManager.initOrchextra(oxKey, oxSecret, notificationActivityClass,
          ocmBuilder.getOxSenderId(), ocmBuilder.getVuforiaImpl());
    } else {
      OCManager.initOrchextra(oxKey, oxSecret, notificationActivityClass,
          ocmBuilder.getOxSenderId());
    }
    System.out.println("appOn6.7");
  }

  /**
   * Stylize the library Ui
   */
  public static void setStyleUi(OcmStyleUiBuilder ocmUiBuilder) {
    OCManager.setStyleUi(ocmUiBuilder);
  }

  /**
   * Set the language which the app content is showed
   */
  public static void setContentLanguage(String contentLanguage) {
    OCManager.setContentLanguage(contentLanguage);
  }

  /**
   * Get the app menus
   */
  public static void getMenus(DataRequest menuRequest, OcmCallbacks.Menus menusCallback) {
    OCManager.getMenus(menuRequest, new OCManagerCallbacks.Menus() {
      @Override public void onMenusLoaded(UiMenuData menus) {
        menusCallback.onMenusLoaded(menus);
      }

      @Override public void onMenusFails(Throwable e) {
        menusCallback.onMenusFails(e);
      }
    });
  }

  /**
   * Clear cached data
   *
   * @param clear callback
   */
  public static void clearData(boolean images, boolean data, final OCManagerCallbacks.Clear clear) {
    OCManager.clearData(images, data, new OCManagerCallbacks.Clear() {
      @Override public void onDataClearedSuccessfull() {
        clear.onDataClearedSuccessfull();
      }

      @Override public void onDataClearFails(Exception e) {
        clear.onDataClearFails(e);
      }
    });
  }

  /**
   * Return a fragment which you can add to your views.
   *
   * @param uiMenu It is the content url returned in the menus call.
   * @param filter To filter the content by a word
   * @param imagesToDownload Number of images that we can to download for caching
   * @param sectionCallbacks callback
   */
  public static void generateSectionView(UiMenu uiMenu, String filter, int imagesToDownload,
      OcmCallbacks.Section sectionCallbacks) {
    OCManager.generateSectionView(uiMenu, filter, imagesToDownload,
        new OCManagerCallbacks.Section() {
          @Override public void onSectionLoaded(UiGridBaseContentData uiGridBaseContentData) {
            sectionCallbacks.onSectionLoaded(uiGridBaseContentData);
          }

          @Override public void onSectionFails(Exception e) {
            sectionCallbacks.onSectionFails(e);
          }
        });
  }

  /**
   * Return a detail view which you have to add in your view. You have to specify the element url
   * to get the content.
   */
  public static UiDetailBaseContentData generateDetailView(String elementUrl) {
    return OCManager.generateDetailView(elementUrl);
  }

  /**
   * Return the search view which you have to add in your view.
   */
  public static UiSearchBaseContentData generateSearchView() {
    return OCManager.generateSearchView();
  }

  /**
   * The sdk does an action when deep link is provided and exists in dashboard
   */
  public static void processDeepLinks(String path) {
    OCManager.processDeepLinks(path);
  }

  /**
   * Set the local storage for webviews and login views
   */
  public static void setLocalStorage(Map<String, String> localStorage) {
    OCManager.setLocalStorage(localStorage);
  }

  /**
   * Provide when the user app is logged in.
   */
  public static void setUserIsAuthorizated(boolean isAuthorizated) {
    OCManager.setUserIsAuthorizated(isAuthorizated);
  }

  /**
   * Provide when the action requires the user to be logged.
   */
  public static void setLoggedAction(String elementUrl) {
    OCManager.setLoggedAction(elementUrl);
  }

  /**
   * Start or restart the sdk with a new credentials
   */
  public static void startWithCredentials(String apiKey, String apiSecret,
      OcmCredentialCallback onCredentialCallback) {
    OCManager.setNewOrchextraCredentials(apiKey, apiSecret, onCredentialCallback);
  }

  public static void start(OcmCredentialCallback onCredentialCallback) {
    OCManager.start(onCredentialCallback);
  }

  /**
   * Set a business unit
   */
  public static void setBusinessUnit(String businessUnit) {
    OCManager.setOrchextraBusinessUnit(businessUnit);
  }

  /**
   * Set a custom app user
   */
  public static void bindUser(CrmUser crmUser) {
    OCManager.bindUser(crmUser);
  }

  /**
   * Start the sdk with the last provided credentials.
   */
  public static void start() {
    OCManager.start();
  }

  public static void stop() {
    OCManager.stop();
  }

  public static void setOnCustomSchemeReceiver(OnCustomSchemeReceiver onCustomSchemeReceiver) {
    OCManager.setOnCustomSchemeReceiver(onCustomSchemeReceiver);
  }

  public static void closeDetailView() {
    OCManager.closeDetailView();
  }

  public static void setOnDoRequiredLoginCallback(
      OnRequiredLoginCallback onDoRequiredLoginCallback) {
    OCManager.setDoRequiredLoginCallback(onDoRequiredLoginCallback);
  }

  public static void setCustomBehaviourDelegate(
      OcmCustomBehaviourDelegate ocmCustomBehaviourDelegate) {
    OCManager.setCustomBehaviourDelegate(ocmCustomBehaviourDelegate);
  }

  public static void setQueryStringGenerator(QueryStringGenerator queryStringGenerator) {
    Ocm.queryStringGenerator = queryStringGenerator;
  }

  public static QueryStringGenerator getQueryStringGenerator() {
    return Ocm.queryStringGenerator;
  }

  public static void setOnChangedMenuCallback(OnChangedMenuCallback onChangedMenuCallback) {
    OCManager.setOnChangedMenuCallback(onChangedMenuCallback);
  }

  public static void setOnLoadDataContentSectionFinished(
      OnLoadContentSectionFinishedCallback onLoadContentSectionFinishedCallback) {
    OCManager.setOnLoadDataContentSectionFinished(onLoadContentSectionFinishedCallback);
  }
}

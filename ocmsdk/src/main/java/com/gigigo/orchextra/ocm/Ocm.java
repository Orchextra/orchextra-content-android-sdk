package com.gigigo.orchextra.ocm;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.gigigo.orchextra.CrmUser;
import com.gigigo.orchextra.Orchextra;
import com.gigigo.orchextra.ocm.callbacks.OcmCredentialCallback;
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.ocm.views.UiDetailBaseContentData;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import com.gigigo.orchextra.ocm.views.UiSearchBaseContentData;
import java.util.List;
import java.util.Map;

public final class Ocm {

  public static void initialize(Application app) {

     OcmBuilder ocmBuilder = new OcmBuilder(app);
    String oxKey = "45179249c53cda327421e242e1fa4830484c9a94";
    String oxSecret = "e8473653b5d479a173c53bf3831670275b3a0364";
    Class notificationActivityClass = ocmBuilder.getNotificationActivityClass();

    OCManager.initSdk(ocmBuilder.getApp());
    OCManager.setContentLanguage(ocmBuilder.getContentLanguage());
    OCManager.setDoRequiredLoginCallback(ocmBuilder.getOnRequiredLoginCallback());
    OCManager.setEventCallback(ocmBuilder.getOnEventCallback());

    SharedPreferences prefs =
        ocmBuilder.getApp().getSharedPreferences("OCMpreferencez", Context.MODE_PRIVATE);
    //asv la vaina sería meter este validator en el initiliaze, el put to true en el changecredentialas de orchextra y el get en el initialize
    Boolean IsCredentialsChanged = prefs.getBoolean("ChangeCredentialsDONE", false);

    if(!IsCredentialsChanged) {
      OCManager.initOrchextra(oxKey, oxSecret, notificationActivityClass, ocmBuilder.getOxSenderId());
      Orchextra.start();
    }
    else
    {
        String API_KEY = "7bb9fa0f9b7a02846383fd6284d3c74b8155644c";
        String API_SECRET = "3295dc8de90300e2977e6cec5b28b614fc644934";

      //probar a pillar las apikeyy ecret del preferences
    //  OCManager.initOrchextra(API_KEY, API_SECRET, notificationActivityClass, ocmBuilder.getOxSenderId());

    }
  }


  public static void initializeWithChangeCredentials(OcmBuilder ocmBuilder) {

   // OcmBuilder ocmBuilder = new OcmBuilder(app);
   // String oxKey = "45179249c53cda327421e242e1fa4830484c9a94";
   // String oxSecret = "e8473653b5d479a173c53bf3831670275b3a0364";

    String oxKey = "30a2d1032d623dadd345db6c7631fbaac704af45";
    String oxSecret = "329e98d088e0cfaac1a190ee9fafb44cbea92b59";

    Class notificationActivityClass = ocmBuilder.getNotificationActivityClass();

    OCManager.initSdk(ocmBuilder.getApp());
    OCManager.setContentLanguage(ocmBuilder.getContentLanguage());
    OCManager.setDoRequiredLoginCallback(ocmBuilder.getOnRequiredLoginCallback());
    OCManager.setEventCallback(ocmBuilder.getOnEventCallback());

    //SharedPreferences prefs =
    //    ocmBuilder.getApp().getSharedPreferences("OCMpreferencez", Context.MODE_PRIVATE);
    //asv la vaina sería meter este validator en el initiliaze, el put to true en el changecredentialas de orchextra y el get en el initialize
   // Boolean IsCredentialsChanged = prefs.getBoolean("ChangeCredentialsDONE", false);

    //if(!IsCredentialsChanged) {

      System.out.println("PRIMERA VEZ SOLO UNA VEZ");
      OCManager.initOrchextra(oxKey, oxSecret, notificationActivityClass, ocmBuilder.getOxSenderId());

    /*}
    else
    {
      System.out.println("PRIMERA ahora no debe hacer nada");
      System.out.println("**********************************************************************************************");
      System.out.println("\n\n\n\n\n\nINICIO DOS\n\n\n\n\n\n");
      System.out.println("**********************************************************************************************");
      System.out.println("**********************************************************************************************");
      System.out.println("**********************************************************************************************");
      System.out.println("**********************************************************************************************");

      String API_KEY = "7bb9fa0f9b7a02846383fd6284d3c74b8155644c";
      String API_SECRET = "3295dc8de90300e2977e6cec5b28b614fc644934";
      OCManager.initOrchextra(API_KEY, API_SECRET, notificationActivityClass, ocmBuilder.getOxSenderId());
      //Ocm.start();
    }*/

    Ocm.start();
  }

  /**
   * Initialize the sdk. This method must be initialized in the onCreate method of the Application
   * class
   */
  public static void initialize(OcmBuilder ocmBuilder) {

    Application app = ocmBuilder.getApp();

    String oxKey = ocmBuilder.getOxKey();
    String oxSecret = ocmBuilder.getOxSecret();
    Class notificationActivityClass = ocmBuilder.getNotificationActivityClass();

    OCManager.initSdk(app);
    OCManager.setContentLanguage(ocmBuilder.getContentLanguage());
    OCManager.setDoRequiredLoginCallback(ocmBuilder.getOnRequiredLoginCallback());
    OCManager.setEventCallback(ocmBuilder.getOnEventCallback());
    if (ocmBuilder.getVuforiaImpl() != null) {
      OCManager.initOrchextra(oxKey, oxSecret, notificationActivityClass,
          ocmBuilder.getOxSenderId(), ocmBuilder.getVuforiaImpl());
    } else {
      OCManager.initOrchextra(oxKey, oxSecret, notificationActivityClass,
          ocmBuilder.getOxSenderId());
    }
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
  public static void getMenus(OcmCallbacks.Menus menusCallback) {
    OCManager.getMenus(new OCManagerCallbacks.Menus() {
      @Override public void onMenusLoaded(List<UiMenu> menus) {
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
   * @param viewId It is the content url returned in the menus call.
   * @param filter To filter the content by a word
   * @param sectionCallbacks callback
   */
  public static void generateSectionView(String viewId, String filter,
      OcmCallbacks.Section sectionCallbacks) {
    OCManager.generateSectionView(viewId, filter, new OCManagerCallbacks.Section() {
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
   * Start or restart the sdk with a new credentials
   */
  public static void startWithCredentials(String apiKey, String apiSecret,
      OcmCredentialCallback onCredentialCallback) {
    OCManager.setNewOrchextraCredentials(apiKey, apiSecret, onCredentialCallback);
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
}

package com.gigigo.orchextra.ocm;

import android.app.Application;
import com.gigigo.orchextra.CrmUser;
import com.gigigo.orchextra.ocm.callbacks.OcmCredentialCallback;
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.ocm.views.UiDetailBaseContentData;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import com.gigigo.orchextra.ocm.views.UiSearchBaseContentData;
import java.util.List;
import java.util.Map;

public final class Ocm {

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
    OCManager.initOrchextra(oxKey, oxSecret, notificationActivityClass);
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
  public static List<UiMenu> getMenus() {
    return OCManager.getMenus();
  }

  /**
   * Return a fragment which you can add to your views.
   *
   * @param viewId It is the content url returned in the menus call.
   * @param filter To filter the content by a word
   */
  public static UiGridBaseContentData generateGridView(String viewId, String filter) {
    return OCManager.generateGridView(viewId, filter);
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
   * Clear the cache of the sdk content
   */
  public static void clearCache() {
    OCManager.clearCache();
  }

  /**
   * Start the sdk with the last provided credentials.
   */
  public static void start() {
    OCManager.start();
  }

  public static void setOnCustomSchemeReceiver(OnCustomSchemeReceiver onCustomSchemeReceiver) {
    OCManager.setOnCustomSchemeReceiver(onCustomSchemeReceiver);
  }
}

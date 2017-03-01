package com.gigigo.orchextra.ocm;

import android.app.Application;
import com.gigigo.orchextra.CrmUser;
import com.gigigo.orchextra.CustomSchemeReceiver;
import com.gigigo.orchextra.ocm.callbacks.OcmCredentialCallback;
import com.gigigo.orchextra.ocm.callbacks.OnRetrieveUiMenuListener;
import com.gigigo.orchextra.ocm.views.UiDetailBaseContentData;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import com.gigigo.orchextra.ocm.views.UiSearchBaseContentData;
import java.util.Map;

public final class Ocm {

  public static void initialize(OcmBuilder ocmBuilder) {

    Application app = ocmBuilder.getApp();

    String oxKey = ocmBuilder.getOxKey();
    String oxSecret = ocmBuilder.getOxSecret();
    Class notificationActivityClass = ocmBuilder.getNotificationActivityClass();
    CustomSchemeReceiver onCustomSchemeReceiver = ocmBuilder.getOnCustomSchemeReceiver();

    OCManager.initSdk(app);
    OCManager.setContentLanguage(ocmBuilder.getContentLanguage());
    OCManager.setDoRequiredLoginCallback(ocmBuilder.getOnRequiredLoginCallback());
    OCManager.setEventCallback(ocmBuilder.getOnEventCallback());
    OCManager.initOrchextra(oxKey, oxSecret, notificationActivityClass, onCustomSchemeReceiver);
  }

  public static void setStyleUi(OcmStyleUiBuilder ocmUiBuilder) {
    OCManager.setStyleUi(ocmUiBuilder);
  }

  public static void setContentLanguage(String contentLanguage) {
    OCManager.setContentLanguage(contentLanguage);
  }

  public static void getMenus(OnRetrieveUiMenuListener onRetrieveUiMenuListener) {
    OCManager.getMenus(onRetrieveUiMenuListener);
  }

  public static UiGridBaseContentData generateGridView(String viewId, String filter) {
    return OCManager.generateGridView(viewId, filter);
  }

  public static UiDetailBaseContentData generateDetailView(String elementUrl) {
    return OCManager.generateDetailView(elementUrl);
  }

  public static UiSearchBaseContentData generateSearchView() {
    return OCManager.generateSearchView();
  }

  public static void processDeepLinks(String path) {
    OCManager.processDeepLinks(path);
  }

  public static void setLocalStorage(Map<String, String> localStorage) {
    OCManager.setLocalStorage(localStorage);
  }

  public static void setUserIsAuthorizated(boolean isAuthorizated) {
    OCManager.setUserIsAuthorizated(isAuthorizated);
  }

  public static void setNewOrchextraCredentials(String apiKey, String apiSecret,
      OcmCredentialCallback onCredentialCallback) {
    OCManager.setNewOrchextraCredentials(apiKey, apiSecret, onCredentialCallback);
  }

  public static void setOrchextraBusinessUnit(String businessUnit) {
    OCManager.setOrchextraBusinessUnit(businessUnit);
  }

  public static void bindUser(CrmUser crmUser) {
    OCManager.bindUser(crmUser);
  }

  public static void clearCache() {
    OCManager.clearCache();
  }
}

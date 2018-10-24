package com.gigigo.orchextra.ocm;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import com.gigigo.orchextra.core.controller.model.home.ImageTransformReadArticle;
import com.gigigo.orchextra.core.domain.entities.menus.DataRequest;
import com.gigigo.orchextra.core.sdk.OcmSchemeHandler;
import com.gigigo.orchextra.ocm.callbacks.CustomUrlCallback;
import com.gigigo.orchextra.ocm.callbacks.OcmCredentialCallback;
import com.gigigo.orchextra.ocm.callbacks.OnChangedMenuCallback;
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver;
import com.gigigo.orchextra.ocm.callbacks.OnLoadContentSectionFinishedCallback;
import com.gigigo.orchextra.ocm.callbacks.OnRequiredLoginCallback;
import com.gigigo.orchextra.ocm.callbacks.ScanCodeListener;
import com.gigigo.orchextra.ocm.customProperties.OcmCustomBehaviourDelegate;
import com.gigigo.orchextra.ocm.customProperties.OcmCustomTranslationDelegate;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.ocm.dto.UiMenuData;
import com.gigigo.orchextra.ocm.views.UiDetailBaseContentData;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import com.gigigo.orchextra.ocm.views.UiSearchBaseContentData;
import com.gigigo.orchextra.wrapper.CrmUser;
import com.gigigo.orchextra.wrapper.OxManager;
import java.util.Map;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;

public final class Ocm {

  private static QueryStringGenerator queryStringGenerator;
  private static ExceptionListener exceptionListener;

  public static final String OCM_PREFERENCES = "OCMpreferencez";
  public static final String OCM_CHANGE_CREDENTIALS_DONE = "ChangeCredentialsDONE";

  /**
   * Initialize the sdk. This method must be initialized in the onCreate method of the Application
   * class
   */
  public static void initialize(@NonNull OcmBuilder ocmBuilder,
      @Nullable OcmCredentialCallback onCredentialCallback) {
    Application app = ocmBuilder.getApp();
    String oxKey = ocmBuilder.getOxKey();
    String oxSecret = ocmBuilder.getOxSecret();
    Class notificationActivityClass = ocmBuilder.getNotificationActivityClass();
    OCManager.setContentLanguage(ocmBuilder.getContentLanguage());
    OCManager.setDoRequiredLoginCallback(ocmBuilder.getOnRequiredLoginCallback());
    OCManager.setEventCallback(ocmBuilder.getOnEventCallback());
    OCManager.setVimeoAccessToken(ocmBuilder.getVimeoAccessToken());
    OCManager.initSdk(app);
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

    OCManager.initOrchextra(oxKey, oxSecret, notificationActivityClass,
        ocmBuilder.getFirebaseApiKey(), ocmBuilder.getFirebaseApplicationId(),
        ocmBuilder.getBusinessUnit(), onCredentialCallback, ocmBuilder.getTriggeringEnabled(),
        ocmBuilder.getAnonymous(), ocmBuilder.isProximityEnabled());
  }

  public static void getOxToken(final OcmCredentialCallback ocmCredentialCallback) {
    OCManager.getOxToken(ocmCredentialCallback);
  }

  public static void setErrorListener(final OxManager.ErrorListener errorListener) {
    OCManager.setErrorListener(errorListener);
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
  @Deprecated public static void getMenus(DataRequest menuRequest,
      OcmCallbacks.Menus menusCallback) {
    OCManager.getMenus(new OCManagerCallbacks.Menus() {
      @Override public void onMenusLoaded(UiMenuData menus) {
        menusCallback.onMenusLoaded(menus);
      }

      @Override public void onMenusFails(Throwable e) {
        menusCallback.onMenusFails(e);
      }
    });
  }

  /**
   * Get the app menus
   */
  public static void getMenus(OcmCallbacks.Menus menusCallback) {
    OCManager.getMenus(new OCManagerCallbacks.Menus() {
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
    OCManager.processRedirectElementUrl(path);
  }

  /**
   * The sdk does an action when deep link is provided and exists in dashboard
   */
  public static void processElementUrl(String elementUrl, ImageView imageViewToExpandInDetail,
      OcmSchemeHandler.ProcessElementCallback processElementCallback) {
    OCManager.processElementUrl(elementUrl, imageViewToExpandInDetail, processElementCallback);
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
   * Set a business unit
   */
  public static void setBusinessUnit(String businessUnit, OxManager.StatusListener statusListener) {
    OCManager.setOrchextraBusinessUnit(businessUnit, statusListener);
  }

  /**
   * Set a custom app user
   */
  public static void bindUser(CrmUser crmUser, OxManager.StatusListener statusListener) {
    OCManager.bindUser(crmUser, statusListener);
  }

  public static void unBindUser(OxManager.StatusListener statusListener) {
    OCManager.unBindUser(statusListener);
  }

  public static void setCustomFields(Map<String, String> customFields,
      OxManager.StatusListener statusListener) {
    OCManager.setCustomFields(customFields, statusListener);
  }

  public static void stop() {
    OCManager.stop();
    exceptionListener = null;
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

  public static void setCustomTranslationDelegate(
      OcmCustomTranslationDelegate customTranslationDelegate) {
    OCManager.setCustomTranslationDelegate(customTranslationDelegate);
  }

  public static void setCustomUrlCallback(CustomUrlCallback customUrlCallback) {
    OCManager.setCustomUrlCallback(customUrlCallback);
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

  public static void scanCode(ScanCodeListener scanCodeListener) {
    OCManager.scanCode(scanCodeListener);
  }

  public static void openScanner() {
    OCManager.openScanner();
  }

  public static void logException(Exception e) {
    if (Ocm.exceptionListener != null) {
      Ocm.exceptionListener.logException(e);
    }
  }

  public static void setExceptionListener(ExceptionListener exceptionListener) {
    Ocm.exceptionListener = exceptionListener;
  }
}

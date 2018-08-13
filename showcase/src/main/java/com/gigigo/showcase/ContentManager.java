package com.gigigo.showcase;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import com.gigigo.orchextra.CrmUser;
import com.gigigo.orchextra.Orchextra;
import com.gigigo.orchextra.core.controller.model.home.ImageTransformReadArticle;
import com.gigigo.orchextra.ocm.OCManagerCallbacks;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.OcmBuilder;
import com.gigigo.orchextra.ocm.OcmCallbacks;
import com.gigigo.orchextra.ocm.OcmEvent;
import com.gigigo.orchextra.ocm.OcmStyleUiBuilder;
import com.gigigo.orchextra.ocm.callbacks.OcmCredentialCallback;
import com.gigigo.orchextra.ocm.callbacks.OnEventCallback;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import com.gigigo.showcase.main.MainActivity;
import com.gigigo.showcase.settings.ProjectData;
import com.gigigo.vuforiaimplementation.ImageRecognitionVuforiaImpl;
import java.util.GregorianCalendar;
import java.util.Map;

public class ContentManager {

  private static final ContentManager instance = new ContentManager();
  private static final String COUNTRY = "it";
  private Handler handler;

  public static ContentManager getInstance() {
    return instance;
  }

  private ContentManager() {
    this.handler = new Handler(Looper.getMainLooper());
  }

  public void init(Application application) {

    OcmBuilder ocmBuilder =
        new OcmBuilder(application).setNotificationActivityClass(MainActivity.class)
            .setShowReadArticles(true)
            .setTransformReadArticleMode(ImageTransformReadArticle.BITMAP_TRANSFORM)
            .setMaxReadArticles(100)
            .setOrchextraCredentials(ProjectData.getDefaultApiKey(),
                ProjectData.getDefaultApiSecret())
            .setContentLanguage("EN")
            .setOxSenderId("327008883283")
            .setOnEventCallback(new OnEventCallback() {
              @Override public void doEvent(OcmEvent event, Object data) {
              }

              @Override public void doEvent(OcmEvent event) {
              }
            });

    Ocm.initialize(ocmBuilder);

    OcmStyleUiBuilder ocmStyleUiBuilder =
        new OcmStyleUiBuilder().setTitleToolbarEnabled(true).setEnabledStatusBar(true);

    Ocm.setStyleUi(ocmStyleUiBuilder);
    Ocm.setBusinessUnit(COUNTRY);
  }

  public void start(ContentManagerCallback<String> callback) {
    start(ProjectData.getDefaultApiKey(), ProjectData.getDefaultApiSecret(), callback);
  }

  public void start(String apiKey, String apiSecret,
      final ContentManagerCallback<String> callback) {

    Ocm.setBusinessUnit(COUNTRY);
    Ocm.startWithCredentials(apiKey, apiSecret, new OcmCredentialCallback() {
      @Override public void onCredentialReceiver(final String accessToken) {
        handler.post(new Runnable() {
          @Override public void run() {
            callback.onSuccess(accessToken);
          }
        });
      }

      @Override public void onCredentailError(final String code) {
        handler.post(new Runnable() {
          @Override public void run() {
            callback.onError(new Exception(code));
          }
        });
      }
    });
  }

  public void clear() {
    Ocm.clearData(true, true, new OCManagerCallbacks.Clear() {
      @Override public void onDataClearedSuccessfull() {

      }

      @Override public void onDataClearFails(Exception e) {

      }
    });
  }

  public void getContent(String section, int imagesToDownload,
      final ContentManagerCallback<UiGridBaseContentData> callback) {

    Ocm.generateSectionView(section, null, imagesToDownload, new OcmCallbacks.Section() {
      @Override public void onSectionLoaded(UiGridBaseContentData uiGridBaseContentData) {
        callback.onSuccess(uiGridBaseContentData);
      }

      @Override public void onSectionFails(Exception e) {
        callback.onError(e);
      }
    });
  }

  public Map<String, String> getCustomFields() {
    return Orchextra.getUserCustomFields();
  }

  public void setUserCustomFields(Map<String, String> customFields) {
    Orchextra.bindUser(getCrmUser("test2"));
    Orchextra.setUserCustomFields(customFields);
    Orchextra.commitConfiguration();
  }

  private CrmUser getCrmUser(String id) {
    return new CrmUser(id, new GregorianCalendar(1990, 10, 15), CrmUser.Gender.GenderFemale);
  }

  public interface ContentManagerCallback<T> {

    void onSuccess(T result);

    void onError(Exception exception);
  }
}
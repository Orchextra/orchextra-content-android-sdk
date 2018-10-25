package com.gigigo.orchextra.ocm.sample.ocm;

import android.app.Application;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.OcmBuilder;
import com.gigigo.orchextra.ocm.OcmEvent;
import com.gigigo.orchextra.ocm.OcmStyleUiBuilder;
import com.gigigo.orchextra.ocm.callbacks.OcmCredentialCallback;
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver;
import com.gigigo.orchextra.ocm.callbacks.OnEventCallback;
import com.gigigo.orchextra.ocm.sample.MainActivity;
import timber.log.Timber;

public class OcmWrapperImp implements OcmWrapper {

  private OnStartWithCredentialsCallback mOnStartWithCredentialsCallback;
  private boolean isOxLoaded = false;
  private final Application context;

  private static OcmWrapperImp instance;

  private OcmWrapperImp(Application context) {
    this.context = context;
  }

  public static OcmWrapperImp getInstance(Application application) {
    if (instance == null) {
      instance = new OcmWrapperImp(application);
    }

    return instance;
  }

  @Override public boolean isOcmInitialized() {
    return isOxLoaded;
  }

  @Override public void startWithCredentials(String apiKey, String apiSecret, String businessUnit,
      String vimeoAccessToken, final OnStartWithCredentialsCallback onStartWithCredentialsCallback) {
    mOnStartWithCredentialsCallback = onStartWithCredentialsCallback;
    isOxLoaded = false;

    Timber.d("startWithCredentials(apiKey: %s, apiSecret: %s)", apiKey, apiSecret);

    OcmBuilder ocmBuilder = new OcmBuilder(context).setOrchextraCredentials(apiKey, apiSecret)
        .setOnEventCallback(eventCallback)
        .setContentLanguage("EN")
        .setVimeoAccessToken(vimeoAccessToken)
        .setBusinessUnit(businessUnit)
        .setNotificationActivityClass(MainActivity.class)
        .setAnonymous(false)
        .setProximityEnabled(false)
        .setTriggeringEnabled(false);

    Ocm.initialize(ocmBuilder, new OcmCredentialCallback() {
      @Override public void onCredentialReceiver(String accessToken) {
        Timber.d("onCredentialReceiver");

        Ocm.setErrorListener(error -> {
          Timber.e("Ox error: %s", error);
          if (mOnStartWithCredentialsCallback != null) {
            mOnStartWithCredentialsCallback.onCredentailError();
          }
        });

        Ocm.setOnCustomSchemeReceiver(onCustomSchemeReceiver);

        OcmStyleUiBuilder ocmStyleUiBuilder =
            new OcmStyleUiBuilder().setTitleFont("fonts/Gotham-Ultra.ttf")
                .setNormalFont("fonts/Gotham-Book.ttf")
                .setMediumFont("fonts/Gotham-Medium.ttf")
                .setTitleToolbarEnabled(false)
                .disableThumbnailImages()
                .setEnabledStatusBar(false);
        Ocm.setStyleUi(ocmStyleUiBuilder);

        if (!isOxLoaded) {
          isOxLoaded = true;

          if (mOnStartWithCredentialsCallback != null) {
            mOnStartWithCredentialsCallback.onCredentialReceiver(accessToken);
          }
        }
      }

      @Override public void onCredentailError(String code) {
        Timber.e("Error on init Ox: %s", code);
        if (mOnStartWithCredentialsCallback != null) {
          mOnStartWithCredentialsCallback.onCredentailError();
        }
      }
    });
  }

  @Override public void setUserIsAuthorizated(boolean isUserLogged) {
    Ocm.setUserIsAuthorizated(isUserLogged);
  }

  @Override public void setContentLanguage(String languageCode) {
    Ocm.setContentLanguage(languageCode);
  }

  @Override public void scanCode(ScanCodeListener scanCodeListener) {
    Ocm.scanCode(scanCodeListener::onCodeScan);
  }

  private OnCustomSchemeReceiver onCustomSchemeReceiver =
      scheme -> Timber.i("OnCustomSchemeReceiver: %s", scheme);

  private OnEventCallback eventCallback = new OnEventCallback() {
    @Override public void doEvent(OcmEvent event, Object data) {
      Timber.i("doEvent(OcmEvent event, Object data)");
    }

    @Override public void doEvent(OcmEvent event) {
      Timber.i("doEvent(OcmEvent event)");
    }
  };
}

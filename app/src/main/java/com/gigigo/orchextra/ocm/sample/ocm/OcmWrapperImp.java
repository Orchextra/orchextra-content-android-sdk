package com.gigigo.orchextra.ocm.sample.ocm;

import android.app.Application;
import android.util.Log;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.OcmBuilder;
import com.gigigo.orchextra.ocm.OcmEvent;
import com.gigigo.orchextra.ocm.OcmStyleUiBuilder;
import com.gigigo.orchextra.ocm.callbacks.OcmCredentialCallback;
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver;
import com.gigigo.orchextra.ocm.callbacks.OnEventCallback;
import com.gigigo.orchextra.ocm.sample.MainActivity;

public class OcmWrapperImp implements OcmWrapper {

  private static final String TAG = "OcmWrapperImp";
  private OnStartWithCredentialsCallback mOnStartWithCredentialsCallback;
  private boolean isOxLoaded = false;
  private final Application context;

  public OcmWrapperImp(Application context) {
    this.context = context;
  }

  @Override public boolean isOcmInitialized() {
    return isOxLoaded;
  }

  @Override public void startWithCredentials(String apiKey, String apiSecret, String businessUnit,
      final OnStartWithCredentialsCallback onStartWithCredentialsCallback) {
    mOnStartWithCredentialsCallback = onStartWithCredentialsCallback;
    isOxLoaded = false;

    OcmBuilder ocmBuilder = new OcmBuilder(context).setOrchextraCredentials(apiKey, apiSecret)
        .setOnEventCallback(eventCallback)
        .setContentLanguage("EN")
        .setBusinessUnit(businessUnit)
        .setNotificationActivityClass(MainActivity.class)
        .setTriggeringEnabled(false);

    Ocm.initialize(ocmBuilder, new OcmCredentialCallback() {
      @Override public void onCredentialReceiver(String accessToken) {
        Log.d(TAG, "onCredentialReceiver");

        Ocm.setErrorListener(error -> {
          Log.e(TAG, "Ox error: " + error);
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
        Log.e(TAG, "Error on init Ox: " + code);
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
      scheme -> Log.i(TAG, "OnCustomSchemeReceiver: " + scheme);

  private OnEventCallback eventCallback = new OnEventCallback() {
    @Override public void doEvent(OcmEvent event, Object data) {
      Log.i(TAG, "doEvent(OcmEvent event, Object data)");
    }

    @Override public void doEvent(OcmEvent event) {
      Log.i(TAG, "doEvent(OcmEvent event)");
    }
  };
}

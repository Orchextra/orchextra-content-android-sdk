package com.gigigo.orchextra.wrapper;

import android.app.Application;
import com.gigigo.imagerecognitioninterface.ImageRecognitionCredentials;
import com.gigigo.orchextra.CustomSchemeReceiver;
import com.gigigo.orchextra.Orchextra;
import com.gigigo.orchextra.OrchextraBuilder;
import com.gigigo.orchextra.OrchextraLogLevel;
import com.gigigo.orchextra.device.bluetooth.beacons.BeaconBackgroundModeScan;
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import orchextra.javax.inject.Inject;

/**
 * Created by alex on 01/12/2017.
 */

public class OxManagerImpl implements OxManager {

  private OnCustomSchemeReceiver onCustomSchemeReceiver;
  private HashMap<CrmUser.Gender, com.gigigo.orchextra.CrmUser.Gender> genders;

  //cambio para el inicio selectivo, MEJORAR,
  //necesitamos un contexto para q la funcion setNewOrchextracredentials pueda comprobar las preferences
  //lo suyo es no guardarlo en las preferences, de momneto así y una mejora sencilla seria añadir el contexto a
  //la funcion de setNewOrchextracredentials, para no mantener el application cuando no es necesario
  private CustomSchemeReceiver onOxCustomSchemeReceiver =
      customScheme -> callOnCustomSchemeReceiver(customScheme);

  @Inject
  public OxManagerImpl() {
    genders = new HashMap<>();
    genders.put(CrmUser.Gender.GenderFemale, com.gigigo.orchextra.CrmUser.Gender.GenderFemale);
    genders.put(CrmUser.Gender.GenderMale, com.gigigo.orchextra.CrmUser.Gender.GenderMale);
    genders.put(CrmUser.Gender.GenderND, com.gigigo.orchextra.CrmUser.Gender.GenderND);
  }

  @Override public void callOnCustomSchemeReceiver(String customScheme) {
    if (onCustomSchemeReceiver != null) {
      onCustomSchemeReceiver.onReceive(customScheme);
    }
  }

  @Override public void startImageRecognition() {
    Orchextra.startImageRecognition();
  }

  @Override public void startScanner() {
    Orchextra.startScannerActivity();
  }

  @Override public void init(Application app, Config config) {

    OrchextraBuilder builder = new OrchextraBuilder(app);
    builder.setApiKeyAndSecret(config.getApiKey(), config.getApiSecret())
        .setLogLevel(OrchextraLogLevel.NETWORK)
        .setBackgroundBeaconScanMode(BeaconBackgroundModeScan.NORMAL)
        .setOrchextraCompletionCallback(new com.gigigo.orchextra.OrchextraCompletionCallback() {
          @Override public void onSuccess() {
            config.getOrchextraCompletionCallback().onSuccess();
          }

          @Override public void onError(String message) {
            config.getOrchextraCompletionCallback().onError(message);
          }

          @Override public void onInit(String s) {
            config.getOrchextraCompletionCallback().onInit(s);
          }

          @Override public void onConfigurationReceive(String s) {
            config.getOrchextraCompletionCallback().onConfigurationReceive(s);
          }
        });

    if (config.getNotificationActivityClass() != null) {
      builder.setNotificationActivityClass(config.getNotificationActivityClass().toString());
    }
    if (config.getSenderId() != null && !config.getSenderId().equals("")) {
      builder.setGcmSenderId(config.getSenderId());
    }
    if (config.getVuforia() != null) {
      builder.setImageRecognitionModule(
          new com.gigigo.imagerecognitioninterface.ImageRecognition() {
            @Override public <T> void setContextProvider(T contextProvider) {
              config.getVuforia().setContextProvider(contextProvider);
            }

            @Override
            public void startImageRecognition(
                ImageRecognitionCredentials imageRecognitionCredentials) {
              config.getVuforia().startImageRecognition(
                  new com.gigigo.orchextra.wrapper.ImageRecognitionCredentials() {
                    @Override public String getClientAccessKey() {
                      return imageRecognitionCredentials.getClientAccessKey();
                    }

                    @Override public String getLicensekey() {
                      return imageRecognitionCredentials.getLicensekey();
                    }

                    @Override public String getClientSecretKey() {
                      return imageRecognitionCredentials.getClientSecretKey();
                    }
                  });
            }
          });
    }

    Orchextra.initialize(builder);

    Orchextra.setCustomSchemeReceiver(onOxCustomSchemeReceiver);
  }

  @Override public void getToken() {

  }

  @Override public void bindUser(CrmUser crmUser) {
    com.gigigo.orchextra.CrmUser crmUserOx = new com.gigigo.orchextra.CrmUser(crmUser.getCrmId(),
        crmUser.getBirthdate(), genders.get(crmUser.getGender()));

    Orchextra.bindUser(crmUserOx);
    Orchextra.commitConfiguration();
  }

  @Override public void unBindUser() {

  }

  @Override public void bindDevice(String businessUnit) {
    List<String> bussinessUnits = new ArrayList();
    bussinessUnits.add(businessUnit);
    Orchextra.setDeviceBusinessUnits(bussinessUnits);
  }

  @Override public void unBindDevice() {

  }

  @Override public void setOnCustomSchemeReceiver(OnCustomSchemeReceiver onCustomSchemeReceiver) {
    this.onCustomSchemeReceiver = onCustomSchemeReceiver;
  }

  @Override public void start() {
    Orchextra.start();
  }

  @Override public void stop() {
    Orchextra.stop();
  }

  @Override
  public void updateSDKCredentials(String apiKey, String apiSecret, boolean forceCallback) {
    Orchextra.updateSDKCredentials(apiKey, apiSecret, forceCallback);
  }
}

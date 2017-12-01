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
  private CustomSchemeReceiver onOxCustomSchemeReceiver = new CustomSchemeReceiver() {
    @Override public void onReceive(String customScheme) {
      if (onCustomSchemeReceiver != null) {
        onCustomSchemeReceiver.onReceive(customScheme);
      }
    }
  };

  public OxManagerImpl(){
    genders = new HashMap<>();
    genders.put(CrmUser.Gender.GenderFemale,com.gigigo.orchextra.CrmUser.Gender.GenderFemale);
    genders.put(CrmUser.Gender.GenderMale,com.gigigo.orchextra.CrmUser.Gender.GenderMale);
    genders.put(CrmUser.Gender.GenderND,com.gigigo.orchextra.CrmUser.Gender.GenderND);
  }

  @Override public void startImageRecognition() {
    Orchextra.startImageRecognition();
  }

  @Override public void startScanner() {
    Orchextra.startScannerActivity();
  }

  @Override public void init(Application app,
      String oxKey,
      String oxSecret,
      Class notificationActivityClass,
      String senderId,
      ImageRecognition vuforia,
      OrchextraCompletionCallback orchextraCompletionCallback) {

    OrchextraBuilder builder = new OrchextraBuilder(app);
    builder.setApiKeyAndSecret(oxKey, oxSecret)
        .setLogLevel(OrchextraLogLevel.NETWORK)
        .setBackgroundBeaconScanMode(BeaconBackgroundModeScan.NORMAL)
        .setOrchextraCompletionCallback(new com.gigigo.orchextra.OrchextraCompletionCallback() {
          @Override public void onSuccess() {
            orchextraCompletionCallback.onSuccess();
          }

          @Override public void onError(String s) {
            orchextraCompletionCallback.onError(s);
          }

          @Override public void onInit(String s) {
            orchextraCompletionCallback.onInit(s);
          }

          @Override public void onConfigurationReceive(String s) {
            orchextraCompletionCallback.onConfigurationReceive(s);
          }
        });

    if (notificationActivityClass != null) {
      builder.setNotificationActivityClass(notificationActivityClass.toString());
    }
    if (senderId != null && senderId != "") {
      builder.setGcmSenderId(senderId);
    }
    if (vuforia != null) {
      builder.setImageRecognitionModule(
          new com.gigigo.imagerecognitioninterface.ImageRecognition() {
            @Override public <T> void setContextProvider(T contextProvider) {
              vuforia.setContextProvider(contextProvider);
            }

            @Override
            public void startImageRecognition(ImageRecognitionCredentials imageRecognitionCredentials) {
              vuforia.startImageRecognition(
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
        crmUser.getBirthdate(),genders.get(crmUser.getGender()));

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

  @Override public void setCustomSchemeReceiver(OnCustomSchemeReceiver onCustomSchemeReceiver) {
    this.onCustomSchemeReceiver = onCustomSchemeReceiver;
  }

  @Override public void start() {
    Orchextra.start();
  }

  @Override public void stop() {
    Orchextra.stop();
  }

  @Override public void updateSDKCredentials(String apiKey, String apiSecret, boolean forceCallback) {
    Orchextra.updateSDKCredentials(apiKey, apiSecret, forceCallback);
  }

}

package com.gigigo.orchextra.ocm.sample.ocm;

public interface OcmWrapper {

  boolean isOcmInitialized();

  void startWithCredentials(String apiKey, String apiSecret, String businessUnit,
      OnStartWithCredentialsCallback onStartWithCredentialsCallback);

  void setUserIsAuthorizated(boolean isUserLogged);

  void setContentLanguage(String languageCode);

  interface OnStartWithCredentialsCallback {
    void onCredentialReceiver(String accessToken);

    void onCredentailError();
  }

  interface StatusListener {
    void onSuccess();

    void onError(String error);
  }
}

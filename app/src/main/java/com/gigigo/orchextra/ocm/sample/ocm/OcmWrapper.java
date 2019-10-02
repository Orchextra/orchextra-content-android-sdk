package com.gigigo.orchextra.ocm.sample.ocm;

public interface OcmWrapper {

  boolean isOcmInitialized();

  void startWithCredentials(String apiKey, String apiSecret, String businessUnit,
      String vimeoAccessToken, OnStartWithCredentialsCallback onStartWithCredentialsCallback);

  void setUserIsAuthorizated(boolean isUserLogged);

  void setContentLanguage(String languageCode);

  void scanCode(ScanCodeListener scanCodeListener);

  void processDeepLink(String deepLink);

  interface OnStartWithCredentialsCallback {
    void onCredentialReceiver(String accessToken);

    void onCredentailError();
  }

  interface StatusListener {
    void onSuccess();

    void onError(String error);
  }

  interface ScanCodeListener {
    void onCodeScan(String code);
  }
}

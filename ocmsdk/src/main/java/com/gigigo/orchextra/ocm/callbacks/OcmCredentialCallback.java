package com.gigigo.orchextra.ocm.callbacks;


// Intermediate ocm callback to return ox access token to app
public interface OcmCredentialCallback {
  void onCredentialReceiver(String accessToken);
  void onCredentailError(String code);
}
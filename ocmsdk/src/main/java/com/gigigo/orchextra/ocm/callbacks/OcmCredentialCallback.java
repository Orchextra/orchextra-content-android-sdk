package com.gigigo.orchextra.ocm.callbacks;

/**
 * Created by rui.alonso on 17/1/17.
 */

// Intermediate ocm callback to return ox access token to app
public interface OcmCredentialCallback {
  void onCredentialReceiver(String accessToken);
}
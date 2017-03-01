package com.gigigo.orchextra.core.sdk;

import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import com.gigigo.orchextra.core.sdk.model.detail.DetailActivity;

public class OcmSchemeHandler {

  private final OcmContextProvider contextProvider;

  public OcmSchemeHandler(OcmContextProvider contextProvider) {
    this.contextProvider = contextProvider;
  }

  public void processScheme(String path) {
    DetailActivity.open(contextProvider.getCurrentActivity(), path, null, 0, 0, null);
  }
}

package com.gigigo.orchextra.ocm;

public interface QueryStringGenerator {

  void createQueryString(String siteName, OCManagerCallbacks.FederatedAuthorization callback);
}

package com.gigigo.orchextra.ocm;

import com.gigigo.orchextra.core.domain.entities.elementcache.FederatedAuthorization;

public interface QueryStringGenerator {

  void createQueryString(FederatedAuthorization federatedAuthorization, OCManagerCallbacks.QueryParams callback);
}

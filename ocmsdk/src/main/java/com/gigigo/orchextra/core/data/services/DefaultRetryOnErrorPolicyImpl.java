package com.gigigo.orchextra.core.data.services;

import com.gigigo.ggglib.network.defaultelements.RetryOnErrorPolicy;
import com.gigigo.ggglib.network.responses.HttpResponse;
import com.gigigo.orchextra.core.domain.interactor.errors.OcmBusinessErrors;
import com.gigigo.orchextra.core.data.dto.base.ApiErrorResponse;

public class DefaultRetryOnErrorPolicyImpl implements RetryOnErrorPolicy<ApiErrorResponse> {

  /**
   * The aim of this method is implement the desired policy and implement a switch case strategy
   * that depending on the error Type (Http / Business or whatever) could allow us to decide if
   * we want to retry the request or not
   *
   * @param tries time the request has been already retried
   * @param error error description
   * @param httpResponse full http response of error
   */
  @Override public boolean shouldRetryWithErrorAndTries(int tries, ApiErrorResponse error,
      HttpResponse httpResponse) {
    return retryPolicy(tries, error.getCode());
  }

  @Override public boolean shouldRetryOnException(int tries, Exception e) {
    return defaultRetryPolicy(tries);
  }

  private boolean retryPolicy(int tries, int errorCode) {
    if (errorCode == OcmBusinessErrors.NO_AUTH_EXPIRED.getValue()
        || errorCode == OcmBusinessErrors.NO_AUTH_CREDENTIALS.getValue()) {

      return false;
    } else {
      return defaultRetryPolicy(tries);
    }
  }

  private boolean defaultRetryPolicy(int tries) {
    return tries < 2;
  }
}

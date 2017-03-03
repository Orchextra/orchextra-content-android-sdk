
package com.gigigo.orchextra.core.domain.interactors.errors;

public enum OcmBusinessErrors {
  NO_AUTH_EXPIRED(401),
  NO_AUTH_CREDENTIALS(403),
  INTERNAL_SERVER_ERROR(500);

  private final int codeError;

  OcmBusinessErrors(int codeError) {
    this.codeError = codeError;
  }

  public static OcmBusinessErrors getEnumTypeFromInt(int errorCode) {
    for (OcmBusinessErrors error : OcmBusinessErrors.values()) {
      if (error.getValue() == errorCode) {
        return error;
      }
    }
    return NO_AUTH_EXPIRED;
  }

  public int getValue() {
    return codeError;
  }
}

package com.gigigo.orchextra.core.data.rxException;

/**
 * Exception throw by the application when a there is a network connection exception.
 */
public class ApiMenuNotFoundException extends Exception {

  public ApiMenuNotFoundException() {
    super();
  }

  public ApiMenuNotFoundException(final Throwable cause) {
    super(cause);
  }
}

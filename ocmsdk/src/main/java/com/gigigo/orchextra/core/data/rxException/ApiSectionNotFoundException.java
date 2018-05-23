package com.gigigo.orchextra.core.data.rxException;

/**
 * Exception throw by the application when a there is a api section exception.
 */
public class ApiSectionNotFoundException extends Exception {

  public ApiSectionNotFoundException() {
    super();
  }

  public ApiSectionNotFoundException(final Throwable cause) {
    super(cause);
  }

  public ApiSectionNotFoundException(String message) {
    super(message);
  }
}

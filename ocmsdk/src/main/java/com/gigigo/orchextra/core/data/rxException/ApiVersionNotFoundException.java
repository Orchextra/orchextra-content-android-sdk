package com.gigigo.orchextra.core.data.rxException;

/**
 * Created by alex on 17/11/2017.
 */

public class ApiVersionNotFoundException extends Exception{

  public ApiVersionNotFoundException() {
    super();
  }

  public ApiVersionNotFoundException(final Throwable cause) {
    super(cause);
  }

  public ApiVersionNotFoundException(String message) {
    super(message);
  }

}

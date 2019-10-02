package com.gigigo.orchextra.core.domain.utils;


public interface ConnectionUtils {

  boolean hasConnection();

  boolean isConnectedWifi();

  boolean isConnectedMobile();

  boolean isConnectedFast();
}

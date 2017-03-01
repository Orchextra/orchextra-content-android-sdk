package com.gigigo.orchextra.core.domain.utils;

public interface VersionChecker {
  boolean checkNewVersionVersionIsBigger(String currentVersionApp, String newVersion);
}

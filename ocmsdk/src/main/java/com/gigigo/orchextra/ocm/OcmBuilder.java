package com.gigigo.orchextra.ocm;

import android.app.Application;
import com.gigigo.orchextra.CustomSchemeReceiver;
import com.gigigo.orchextra.ocm.callbacks.OnEventCallback;
import com.gigigo.orchextra.ocm.callbacks.OnRequiredLoginCallback;

public final class OcmBuilder {

  private final Application app;

  private String oxKey;
  private String oxSecret;
  private Class notificationActivityClass;
  private String contentLanguage;
  private OnRequiredLoginCallback onRequiredLoginCallback;
  private OnEventCallback onEventCallback;
  private CustomSchemeReceiver onCustomSchemeReceiver;

  public OcmBuilder(Application app) {
    this.app = app;
  }

  public OcmBuilder setOrchextraCredentials(String oxKey, String oxSecret) {
    this.oxKey = oxKey;
    this.oxSecret = oxSecret;
    return this;
  }

  public OcmBuilder setDoRequiredLoginCallback(OnRequiredLoginCallback onRequiredLoginCallback) {
    this.onRequiredLoginCallback = onRequiredLoginCallback;
    return this;
  }

  public OcmBuilder setEventCallback(OnEventCallback onEventCallback) {
    this.onEventCallback = onEventCallback;
    return this;
  }

  public OcmBuilder setContentLanguage(String language) {
    this.contentLanguage = language;
    return this;
  }

  Application getApp() {
    return app;
  }

  String getOxKey() {
    return oxKey;
  }

  String getOxSecret() {
    return oxSecret;
  }

  OnRequiredLoginCallback getOnRequiredLoginCallback() {
    return onRequiredLoginCallback;
  }

  OnEventCallback getOnEventCallback() {
    return onEventCallback;
  }

  public String getContentLanguage() {
    return contentLanguage;
  }

  Class getNotificationActivityClass() {
    return notificationActivityClass;
  }

  public OcmBuilder setNotificationActivityClass(Class notificationActivityClass) {
    this.notificationActivityClass = notificationActivityClass;
    return this;
  }

  public OcmBuilder setCustomSchemeReceiver(CustomSchemeReceiver onCustomSchemeReceiver) {
    this.onCustomSchemeReceiver = onCustomSchemeReceiver;
    return this;
  }

  public CustomSchemeReceiver getOnCustomSchemeReceiver() {
    return onCustomSchemeReceiver;
  }

  public void setOnCustomSchemeReceiver(CustomSchemeReceiver onCustomSchemeReceiver) {
    this.onCustomSchemeReceiver = onCustomSchemeReceiver;
  }
}

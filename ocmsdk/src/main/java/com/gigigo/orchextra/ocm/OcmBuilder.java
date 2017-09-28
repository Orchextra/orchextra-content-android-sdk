package com.gigigo.orchextra.ocm;

import android.app.Application;
import com.gigigo.imagerecognitioninterface.ImageRecognition;
import com.gigigo.orchextra.core.controller.model.grid.ImageTransformReadArticle;
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

  private String oxSenderId;
  private ImageRecognition vuforiaImpl;
  private boolean showReadArticles = false;
  private ImageTransformReadArticle transformReadArticle=ImageTransformReadArticle.B_AND_W;
  /**
   * setter for do vuforia optional in ocm, setted from intetragion app
   */
  public OcmBuilder setVuforiaImpl(ImageRecognition vuforiaImpl) {
    this.vuforiaImpl = vuforiaImpl;
    return this;
  }

  public ImageRecognition getVuforiaImpl() {
    return vuforiaImpl;
  }

  public ImageTransformReadArticle getTransformReadArticle() {
    return transformReadArticle;
  }

  public OcmBuilder setTransformReadArticle(ImageTransformReadArticle transformReadArticle) {
    this.transformReadArticle = transformReadArticle;
    return this;
  }

  public OcmBuilder setShowReadArticles(boolean isShowReadArticles) {
    this.showReadArticles = isShowReadArticles;

    return this;
  }

  public boolean getShowReadArticles() {
    return showReadArticles;
  }

  /**
   * Initialize the sdk with the Application context
   */
  public OcmBuilder(Application app) {
    this.app = app;
  }

  public OcmBuilder setOrchextraCredentials(String oxKey, String oxSecret) {
    this.oxKey = oxKey;
    this.oxSecret = oxSecret;
    return this;
  }

  /**
   * Callback to know when the user need to be logged in the app.
   */
  @Deprecated public OcmBuilder setOnDoRequiredLoginCallback(
      OnRequiredLoginCallback onRequiredLoginCallback) {
    this.onRequiredLoginCallback = onRequiredLoginCallback;
    return this;
  }

  /**
   * Receive events which are produced when user do some actions
   */
  public OcmBuilder setOnEventCallback(OnEventCallback onEventCallback) {
    this.onEventCallback = onEventCallback;
    return this;
  }

  /**
   * Set the sdk language
   */
  public OcmBuilder setContentLanguage(String language) {
    this.contentLanguage = language;
    return this;
  }

  /**
   * Set the home/main activity which receives the actions of the sdk like to show notifications
   */
  public OcmBuilder setNotificationActivityClass(Class notificationActivityClass) {
    this.notificationActivityClass = notificationActivityClass;
    return this;
  }

  public String getOxSenderId() {
    return oxSenderId;
  }

  public OcmBuilder setOxSenderId(String oxSenderId) {
    this.oxSenderId = oxSenderId;
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

  @Deprecated OnRequiredLoginCallback getOnRequiredLoginCallback() {
    return onRequiredLoginCallback;
  }

  OnEventCallback getOnEventCallback() {
    return onEventCallback;
  }

  String getContentLanguage() {
    return contentLanguage;
  }

  Class getNotificationActivityClass() {
    return notificationActivityClass;
  }
}

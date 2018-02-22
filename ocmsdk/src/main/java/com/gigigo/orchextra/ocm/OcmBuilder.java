package com.gigigo.orchextra.ocm;

import android.app.Application;
import android.graphics.Bitmap;
import com.bumptech.glide.load.Transformation;
import com.gigigo.orchextra.core.controller.model.home.ImageTransformReadArticle;
import com.gigigo.orchextra.ocm.callbacks.OnEventCallback;
import com.gigigo.orchextra.wrapper.ImageRecognition;
import java.util.List;

public final class OcmBuilder {

  private final Application app;
  private String oxKey;
  private String oxSecret;

  private List<String> notificationActivityClass;
  private String contentLanguage;
  private OnEventCallback onEventCallback;

  private String oxSenderId;
  private ImageRecognition vuforiaImpl;
  private boolean showReadArticles = false;
  private ImageTransformReadArticle transformReadArticleMode = ImageTransformReadArticle.OVERLAY;
  private int maxReadArticles=100;


  public int getMaxReadArticles() {
    return maxReadArticles;
  }

  public OcmBuilder setMaxReadArticles(int maxReadArticles) {
    this.maxReadArticles = maxReadArticles;
    return this;
  }

  public Transformation<Bitmap> getCustomBitmapTransformReadArticle() {
    return customBitmapTransformReadArticle;
  }

  public OcmBuilder setCustomBitmapTransformReadArticle(
      Transformation<Bitmap> customBitmapTransformReadArticle) {
    this.customBitmapTransformReadArticle = customBitmapTransformReadArticle;
    return this;
  }

  private com.bumptech.glide.load.Transformation<Bitmap> customBitmapTransformReadArticle =null;

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

  public ImageTransformReadArticle getTransformReadArticleMode() {
    return transformReadArticleMode;
  }

  public OcmBuilder setTransformReadArticleMode(
      ImageTransformReadArticle transformReadArticleMode) {
    this.transformReadArticleMode = transformReadArticleMode;
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
  public OcmBuilder setNotificationActivityClass(List<String> notificationActivityClass) {
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


  OnEventCallback getOnEventCallback() {
    return onEventCallback;
  }

  String getContentLanguage() {
    return contentLanguage;
  }

  List<String> getNotificationActivityClass() {
    return notificationActivityClass;
  }
}

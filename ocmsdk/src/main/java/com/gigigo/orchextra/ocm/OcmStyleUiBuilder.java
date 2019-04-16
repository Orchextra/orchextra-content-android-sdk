package com.gigigo.orchextra.ocm;

import android.support.annotation.DrawableRes;

public final class OcmStyleUiBuilder {

  private String titleFontPath;
  private String normalFonPath;
  private String mediumFontPath;
  private String lightFontPath;
  private boolean titleToolbarEnabled = false;
  private boolean thumbnailEnabled = true;
  private boolean statusBarEnabled = true;
  private boolean animationViewEnabled = true;

  @DrawableRes private int detailBackground = -1;
  @DrawableRes private int detailToolbarBackground = -1;

  /**
   * Path to the font to apply in titles of the app
   */
  public OcmStyleUiBuilder setTitleFont(String titleFontPath) {
    this.titleFontPath = titleFontPath;
    return this;
  }

  /**
   * Path to the font to apply in general texts of the app
   */
  public OcmStyleUiBuilder setNormalFont(String normalFonPath) {
    this.normalFonPath = normalFonPath;
    return this;
  }

  /**
   * Path to the font to apply in text of buttons of the app
   */
  public OcmStyleUiBuilder setMediumFont(String mediumFontPath) {
    this.mediumFontPath = mediumFontPath;
    return this;
  }

  @Deprecated public OcmStyleUiBuilder setLightFont(String lightFontPath) {
    this.lightFontPath = lightFontPath;
    return this;
  }

  public OcmStyleUiBuilder setTitleToolbarEnabled(boolean enabled) {
    this.titleToolbarEnabled = enabled;
    return this;
  }

  public OcmStyleUiBuilder disableThumbnailImages() {
    this.thumbnailEnabled = false;
    return this;
  }

  public OcmStyleUiBuilder setEnabledStatusBar(boolean statusBarEnabled) {
    this.statusBarEnabled = statusBarEnabled;
    return this;
  }

  public OcmStyleUiBuilder disableAnimationView() {
    this.animationViewEnabled = false;
    return this;
  }
  public OcmStyleUiBuilder setDetailBackground(@DrawableRes int detailBackground) {
    this.detailBackground = detailBackground;
    return this;
  }

  public OcmStyleUiBuilder setDetaiToolbarlBackground(@DrawableRes int detailToolbarBackground) {
    this.detailToolbarBackground = detailToolbarBackground;
    return this;
  }

  public String getTitleFontPath() {
    return titleFontPath;
  }

  public String getNormalFonPath() {
    return normalFonPath;
  }

  public String getMediumFontPath() {
    return mediumFontPath;
  }

  @Deprecated public String getLightFontPath() {
    return lightFontPath;
  }

  public boolean isTitleToolbarEnabled() {
    return titleToolbarEnabled;
  }

  public boolean isThumbnailEnabled() {
    return thumbnailEnabled;
  }

  public boolean isStatusBarEnabled() {
    return statusBarEnabled;
  }
  public boolean isAnimationViewEnabled() {
    return animationViewEnabled;
  }

  public int getDetailBackground() {
    return detailBackground;
  }

  public int getDetailToolbarBackground() {
    return detailToolbarBackground;
  }
}

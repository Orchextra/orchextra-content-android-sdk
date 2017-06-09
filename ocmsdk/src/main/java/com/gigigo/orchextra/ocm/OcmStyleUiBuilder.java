package com.gigigo.orchextra.ocm;

public final class OcmStyleUiBuilder {

  private String titleFontPath;
  private String normalFonPath;
  private String mediumFontPath;
  private String lightFontPath;
  private boolean enabledTitleToolbarDetailView = false;
  private boolean thumbnailEnabled = true;

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

  @Deprecated
  public OcmStyleUiBuilder setLightFont(String lightFontPath) {
    this.lightFontPath = lightFontPath;
    return this;
  }

  public OcmStyleUiBuilder setEnabledTitleToolbarDetailView(boolean enabled) {
    this.enabledTitleToolbarDetailView = enabled;
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

  @Deprecated
  public String getLightFontPath() {
    return lightFontPath;
  }

  public boolean isEnabledTitleToolbarDetailView() {
    return enabledTitleToolbarDetailView;
  }

  public void setThumbnailEnabled(boolean thumbnailEnabled) {
    this.thumbnailEnabled = thumbnailEnabled;
  }

  public boolean isThumbnailEnabled() {
    return thumbnailEnabled;
  }
}

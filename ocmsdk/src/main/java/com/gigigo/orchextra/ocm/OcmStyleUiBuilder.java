package com.gigigo.orchextra.ocm;

public final class OcmStyleUiBuilder {

  private String titleFontPath;
  private String normalFonPath;
  private String mediumFontPath;
  private String lightFontPath;

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
}

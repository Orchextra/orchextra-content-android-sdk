package com.gigigo.orchextra.ocm;

public final class OcmStyleUiBuilder {

  private String titleFontPath;
  private String normalFonPath;
  private String mediumFontPath;
  private String lightFontPath;

  public OcmStyleUiBuilder setTitleFontAssetsPath(String titleFontPath) {
    this.titleFontPath = titleFontPath;
    return this;
  }

  public OcmStyleUiBuilder setNormalFont(String normalFonPath) {
    this.normalFonPath = normalFonPath;
    return this;
  }

  public OcmStyleUiBuilder setMediumFont(String mediumFontPath) {
    this.mediumFontPath = mediumFontPath;
    return this;
  }

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

  public String getLightFontPath() {
    return lightFontPath;
  }
}

package com.gigigo.orchextra.core.sdk;

import com.gigigo.orchextra.ocm.OcmStyleUiBuilder;

public class OcmStyleUiImp implements OcmStyleUi {

  private String titleFontPath;
  private String normalFonPath;
  private String mediumFontPath;
  private String lightFontPath;

  @Override public void setStyleUi(OcmStyleUiBuilder styleUi) {
    this.titleFontPath = styleUi.getTitleFontPath();
    this.normalFonPath = styleUi.getNormalFonPath();
    this.mediumFontPath = styleUi.getMediumFontPath();
    this.lightFontPath = styleUi.getLightFontPath();
  }

  @Override
  public String getTitleFontPath() {
    return titleFontPath;
  }

  @Override
  public String getNormalFonPath() {
    return normalFonPath;
  }

  @Override
  public String getMediumFontPath() {
    return mediumFontPath;
  }

  @Override
  public String getLightFontPath() {
    return lightFontPath;
  }
}

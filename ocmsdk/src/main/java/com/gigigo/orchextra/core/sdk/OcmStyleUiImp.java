package com.gigigo.orchextra.core.sdk;

import com.gigigo.orchextra.ocm.OcmStyleUiBuilder;

public class OcmStyleUiImp implements OcmStyleUi {

  private String titleFontPath;
  private String normalFonPath;
  private String mediumFontPath;
  private String lightFontPath;
  private boolean isTitleToolbarEnabled;
  private boolean isThumbnailEnabled;
  private boolean isStatusBarEnabled;

  @Override public void setStyleUi(OcmStyleUiBuilder styleUi) {
    this.titleFontPath = styleUi.getTitleFontPath();
    this.normalFonPath = styleUi.getNormalFonPath();
    this.mediumFontPath = styleUi.getMediumFontPath();
    this.lightFontPath = styleUi.getLightFontPath();
    this.isTitleToolbarEnabled = styleUi.isTitleToolbarEnabled();
    this.isThumbnailEnabled = styleUi.isThumbnailEnabled();
    this.isStatusBarEnabled = styleUi.isStatusBarEnabled();
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

  @Override
  public boolean isTitleToolbarEnabled() {
    return isTitleToolbarEnabled;
  }

  @Override public boolean isThumbnailEnabled() {
    return isThumbnailEnabled;
  }

  @Override public boolean isStatusBarEnabled() {
    return isStatusBarEnabled;
  }
}

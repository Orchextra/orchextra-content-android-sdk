package com.gigigo.orchextra.core.sdk;

import androidx.annotation.DrawableRes;
import com.gigigo.orchextra.ocm.OcmStyleUiBuilder;

public class OcmStyleUiImp implements OcmStyleUi {

  private String titleFontPath;
  private String normalFonPath;
  private String mediumFontPath;
  private String lightFontPath;
  private boolean isTitleToolbarEnabled;
  private boolean isThumbnailEnabled;
  private boolean isStatusBarEnabled;
  private boolean isAnimationViewEnabled;
  @DrawableRes private int detailBackground;
  @DrawableRes private int detailToolbarBackground;

  @Override public void setStyleUi(OcmStyleUiBuilder styleUi) {
    this.titleFontPath = styleUi.getTitleFontPath();
    this.normalFonPath = styleUi.getNormalFonPath();
    this.mediumFontPath = styleUi.getMediumFontPath();
    this.lightFontPath = styleUi.getLightFontPath();
    this.isTitleToolbarEnabled = styleUi.isTitleToolbarEnabled();
    this.isThumbnailEnabled = styleUi.isThumbnailEnabled();
    this.isStatusBarEnabled = styleUi.isStatusBarEnabled();
    this.isAnimationViewEnabled = styleUi.isAnimationViewEnabled();
    this.detailBackground = styleUi.getDetailBackground();
    this.detailToolbarBackground = styleUi.getDetailToolbarBackground();
  }

  @Override public String getTitleFontPath() {
    return titleFontPath;
  }

  @Override public String getNormalFonPath() {
    return normalFonPath;
  }

  @Override public String getMediumFontPath() {
    return mediumFontPath;
  }

  @Override public String getLightFontPath() {
    return lightFontPath;
  }

  @Override public boolean isTitleToolbarEnabled() {
    return isTitleToolbarEnabled;
  }

  @Override public boolean isThumbnailEnabled() {
    return isThumbnailEnabled;
  }

  @Override public boolean isAnimationViewEnabled() {
    return isAnimationViewEnabled;
  }

  @Override public int getDetailBackground() {
    return detailBackground;
  }

  @Override public boolean isStatusBarEnabled() {
    return isStatusBarEnabled;
  }

  @Override public int getDetailToolbarBackground() {
    return detailToolbarBackground;
  }
}

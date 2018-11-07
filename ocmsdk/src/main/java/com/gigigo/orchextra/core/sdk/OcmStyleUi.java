package com.gigigo.orchextra.core.sdk;

import android.support.annotation.DrawableRes;
import com.gigigo.orchextra.ocm.OcmStyleUiBuilder;

public interface OcmStyleUi {
  void setStyleUi(OcmStyleUiBuilder styleUi);

  String getTitleFontPath();

  String getNormalFonPath();

  String getMediumFontPath();

  String getLightFontPath();

  boolean isTitleToolbarEnabled();

  boolean isStatusBarEnabled();

  boolean isThumbnailEnabled();

  @DrawableRes int getDetailBackground();

  @DrawableRes int getDetailToolbarBackground();
}

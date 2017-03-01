package com.gigigo.orchextra.core.sdk;

import com.gigigo.orchextra.ocm.OcmStyleUiBuilder;

public interface OcmStyleUi {
  void setStyleUi(OcmStyleUiBuilder styleUi);

  String getTitleFontPath();

  String getNormalFonPath();

  String getMediumFontPath();

  String getLightFontPath();
}

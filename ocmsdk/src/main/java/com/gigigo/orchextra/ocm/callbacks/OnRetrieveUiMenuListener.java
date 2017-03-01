package com.gigigo.orchextra.ocm.callbacks;

import com.gigigo.orchextra.ocm.dto.UiMenu;
import java.util.List;

public interface OnRetrieveUiMenuListener {

  void onResult(List<UiMenu> uiMenu);

  void onNoNetworkConnectionError();

  void onResponseDataError();
}

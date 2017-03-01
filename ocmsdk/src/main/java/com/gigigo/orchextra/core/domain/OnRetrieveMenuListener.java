package com.gigigo.orchextra.core.domain;

import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;

public interface OnRetrieveMenuListener {

  void onResult(MenuContentData savedMenuContentData);

  void onNoNetworkConnectionError();

  void onResponseDataError();
}

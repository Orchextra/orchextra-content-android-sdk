package com.gigigo.orchextra.core.domain.data;

import com.gigigo.interactorexecutor.responses.BusinessObject;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;

public interface MenuNetworkDataSource {
  BusinessObject<MenuContentData> getMenuContentData();
}

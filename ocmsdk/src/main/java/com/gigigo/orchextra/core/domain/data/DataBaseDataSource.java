package com.gigigo.orchextra.core.domain.data;

import com.gigigo.interactorexecutor.responses.BusinessObject;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;

public interface DataBaseDataSource {
  BusinessObject<MenuContentData> retrieveMenu();

  void saveMenu(MenuContentData data);

  void clearMenu();
}

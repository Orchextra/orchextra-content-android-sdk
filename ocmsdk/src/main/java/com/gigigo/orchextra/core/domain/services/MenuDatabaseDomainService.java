package com.gigigo.orchextra.core.domain.services;

import com.gigigo.interactorexecutor.responses.BusinessObject;
import com.gigigo.orchextra.core.domain.data.DataBaseDataSource;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;

public class MenuDatabaseDomainService implements DomainService {

  private final DataBaseDataSource menuDataBaseDataSource;

  public MenuDatabaseDomainService(DataBaseDataSource menuDataBaseDataSource) {
    this.menuDataBaseDataSource = menuDataBaseDataSource;
  }

  public BusinessObject<MenuContentData> retrieveMenuDataFromDatabase() {
    return menuDataBaseDataSource.retrieveMenu();
  }

  public void saveMenuInDatabase(MenuContentData data) {
    menuDataBaseDataSource.saveMenu(data);
  }
}

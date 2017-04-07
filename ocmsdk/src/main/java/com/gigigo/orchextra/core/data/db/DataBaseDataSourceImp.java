package com.gigigo.orchextra.core.data.db;

import com.gigigo.interactorexecutor.responses.BusinessError;
import com.gigigo.interactorexecutor.responses.BusinessObject;
import com.gigigo.orchextra.core.domain.data.DataBaseDataSource;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;

public class DataBaseDataSourceImp implements DataBaseDataSource {

  private MenuContentData savedMenuContentData;

  public DataBaseDataSourceImp() {
  }

  @Override public BusinessObject<MenuContentData> retrieveMenu() {
    return new BusinessObject<>(savedMenuContentData, BusinessError.createOKInstance());
  }

  @Override public void saveMenu(MenuContentData data) {
    savedMenuContentData = data;
  }

  @Override public void clearMenu() {
    savedMenuContentData = null;
  }
}

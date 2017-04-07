package com.gigigo.orchextra.core.domain.services;

import com.gigigo.interactorexecutor.responses.BusinessError;
import com.gigigo.interactorexecutor.responses.BusinessObject;
import com.gigigo.orchextra.core.domain.data.MenuNetworkDataSource;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import com.gigigo.orchextra.core.domain.interactors.errors.NoNetworkConnectionError;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;

public class MenuNetworkDomainService implements DomainService {

  private final ConnectionUtils connectionUtils;
  private final MenuNetworkDataSource menuNetworkDataSource;

  public MenuNetworkDomainService(ConnectionUtils connectionUtils,
      MenuNetworkDataSource menuNetworkDataSource) {
    this.connectionUtils = connectionUtils;
    this.menuNetworkDataSource = menuNetworkDataSource;
  }

  public BusinessObject<MenuContentData> retrieveMenuDataFromNetwork() {
    if (connectionUtils.hasConnection()) {
      BusinessObject<MenuContentData> boHomeData = menuNetworkDataSource.getMenuContentData();

      if (boHomeData.isSuccess()) {
        return new BusinessObject<>(boHomeData.getData(), BusinessError.createOKInstance());
      } else {
        return new BusinessObject<>(null, boHomeData.getBusinessError());
      }
    } else {
      return new BusinessObject(new NoNetworkConnectionError(), BusinessError.createKOInstance(""));
    }
  }
}

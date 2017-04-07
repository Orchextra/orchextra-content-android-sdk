package com.gigigo.orchextra.core.domain.interactors.home;

import com.gigigo.interactorexecutor.interactors.Interactor;
import com.gigigo.interactorexecutor.interactors.InteractorResponse;
import com.gigigo.interactorexecutor.responses.BusinessObject;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import com.gigigo.orchextra.core.domain.interactors.errors.GenericResponseDataError;
import com.gigigo.orchextra.core.domain.services.MenuDatabaseDomainService;
import com.gigigo.orchextra.core.domain.services.MenuNetworkDomainService;

public class GetMenuDataInteractor implements Interactor<InteractorResponse<MenuContentData>> {

  private final MenuNetworkDomainService menuNetworkDomainService;
  private final MenuDatabaseDomainService menuDatabaseDomainService;
  private boolean useCache = true;

  public GetMenuDataInteractor(MenuDatabaseDomainService menuDatabaseDomainService,
      MenuNetworkDomainService menuNetworkDomainService) {
    this.menuDatabaseDomainService = menuDatabaseDomainService;
    this.menuNetworkDomainService = menuNetworkDomainService;
  }

  @Override public InteractorResponse<MenuContentData> call() throws Exception {

    if (useCache) {
      BusinessObject<MenuContentData> boMenuContentFromDatabase =
          menuDatabaseDomainService.retrieveMenuDataFromDatabase();

      if (boMenuContentFromDatabase.isSuccess() && boMenuContentFromDatabase.getData() != null) {
        return new InteractorResponse<>(boMenuContentFromDatabase.getData());
      }
    }

    BusinessObject<MenuContentData> boMenuContentFromNetwork =
        menuNetworkDomainService.retrieveMenuDataFromNetwork();

    if (boMenuContentFromNetwork.isSuccess() && boMenuContentFromNetwork.getData() != null) {
      menuDatabaseDomainService.saveMenuInDatabase(boMenuContentFromNetwork.getData());
      return new InteractorResponse<>(boMenuContentFromNetwork.getData());
    } else {
      return new InteractorResponse<>(
          new GenericResponseDataError(boMenuContentFromNetwork.getBusinessError()));
    }
  }

  public void setUseCache(boolean useCache) {
    this.useCache = useCache;
  }
}
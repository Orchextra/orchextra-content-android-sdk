package com.gigigo.orchextra.core.domain.interactor.home;


import com.gigigo.interactorexecutor.interactors.Interactor;
import com.gigigo.interactorexecutor.interactors.InteractorResponse;
import com.gigigo.interactorexecutor.responses.BusinessObject;
import com.gigigo.orchextra.core.domain.data.MenuNetworkDataSource;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import com.gigigo.orchextra.core.domain.interactor.errors.GenericResponseDataError;
import com.gigigo.orchextra.core.domain.interactor.errors.NoNetworkConnectionError;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;

/**
 * Created by rui.alonso on 20/9/16.
 */
public class GetMenuDataInteractor implements Interactor<InteractorResponse<MenuContentData>> {

  private final ConnectionUtils connectionUtils;
  private final MenuNetworkDataSource menuNetworkDataSource;

  public GetMenuDataInteractor(ConnectionUtils connectionUtils,
      MenuNetworkDataSource menuNetworkDataSource) {
    this.connectionUtils = connectionUtils;
    this.menuNetworkDataSource = menuNetworkDataSource;
  }

  @Override public InteractorResponse<MenuContentData> call() throws Exception {
    if (connectionUtils.hasConnection()) {

      BusinessObject<MenuContentData> boHomeData = menuNetworkDataSource.getMenuContentData();

      if (boHomeData.isSuccess()) {
        return new InteractorResponse<>(boHomeData.getData());
      } else {
        return new InteractorResponse<>(
            new GenericResponseDataError(boHomeData.getBusinessError()));
      }
    } else {
      return new InteractorResponse(new NoNetworkConnectionError());
    }
  }
}

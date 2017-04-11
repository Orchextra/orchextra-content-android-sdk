package com.gigigo.orchextra.core.data.api;

import com.gigigo.ggglib.network.executors.ApiServiceExecutor;
import com.gigigo.ggglib.network.mappers.ApiGenericResponseMapper;
import com.gigigo.ggglib.network.responses.ApiGenericResponse;
import com.gigigo.interactorexecutor.responses.BusinessObject;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentData;
import com.gigigo.orchextra.core.data.api.mappers.BusinessObjectMapper;
import com.gigigo.orchextra.core.data.api.services.OcmApiService;
import com.gigigo.orchextra.core.domain.data.MenuNetworkDataSource;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;

public class MenuNetworkDataSourceImp implements MenuNetworkDataSource {
  private final ApiServiceExecutor serviceExecutor;
  private final OcmApiService apiService;
  private final ApiGenericResponseMapper responseMapper;

  public MenuNetworkDataSourceImp(ApiServiceExecutor serviceExecutor, OcmApiService apiService,
      ApiGenericResponseMapper responseMapper) {

    this.serviceExecutor = serviceExecutor;
    this.apiService = apiService;
    this.responseMapper = responseMapper;
  }

  @Override public BusinessObject<MenuContentData> getMenuContentData() {
    ApiGenericResponse apiGenericResponse =
        serviceExecutor.executeNetworkServiceConnection(ApiMenuContentData.class,
            apiService.getMenuData());

    BusinessObjectMapper mapper = new BusinessObjectMapper();
    return mapper.mapToBusinessObjectFromGggLibToInteractorExecutor(
        responseMapper.mapApiGenericResponseToBusiness(apiGenericResponse));
  }
}

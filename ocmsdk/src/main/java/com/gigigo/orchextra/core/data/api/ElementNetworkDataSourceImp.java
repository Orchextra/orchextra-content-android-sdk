package com.gigigo.orchextra.core.data.api;

import com.gigigo.ggglib.network.executors.ApiServiceExecutor;
import com.gigigo.ggglib.network.mappers.ApiGenericResponseMapper;
import com.gigigo.ggglib.network.responses.ApiGenericResponse;
import com.gigigo.interactorexecutor.responses.BusinessObject;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementData;
import com.gigigo.orchextra.core.data.api.mappers.BusinessObjectMapper;
import com.gigigo.orchextra.core.data.api.services.OcmApiService;
import com.gigigo.orchextra.core.domain.data.ElementNetworkDataSource;
import com.gigigo.orchextra.core.domain.entities.elements.ElementData;

public class ElementNetworkDataSourceImp implements ElementNetworkDataSource {

  private final ApiServiceExecutor serviceExecutor;
  private final OcmApiService apiService;
  private final ApiGenericResponseMapper responseMapper;

  public ElementNetworkDataSourceImp(ApiServiceExecutor serviceExecutor, OcmApiService apiService,
      ApiGenericResponseMapper responseMapper) {

    this.serviceExecutor = serviceExecutor;
    this.apiService = apiService;
    this.responseMapper = responseMapper;
  }

  @Override public BusinessObject<ElementData> getElementById(String elementId) {
    ApiGenericResponse apiGenericResponse =
        serviceExecutor.executeNetworkServiceConnection(ApiElementData.class,
            apiService.getElementById(elementId));

    BusinessObjectMapper mapper = new BusinessObjectMapper();
    return mapper.mapToBusinessObjectFromGggLibToInteractorExecutor(
        responseMapper.mapApiGenericResponseToBusiness(apiGenericResponse));
  }
}

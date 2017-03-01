package com.gigigo.orchextra.core.data;

import com.gigigo.ggglib.network.executors.ApiServiceExecutor;
import com.gigigo.ggglib.network.mappers.ApiGenericResponseMapper;
import com.gigigo.ggglib.network.responses.ApiGenericResponse;
import com.gigigo.interactorexecutor.responses.BusinessError;
import com.gigigo.interactorexecutor.responses.BusinessObject;
import com.gigigo.orchextra.core.data.services.OcmApiService;
import com.gigigo.orchextra.core.domain.data.SectionNetworkDataSource;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.data.dto.content.ApiSectionContentData;

public class HomeNetworkDataSourceImp implements SectionNetworkDataSource {
  private final ApiServiceExecutor serviceExecutor;
  private final OcmApiService apiService;
  private final ApiGenericResponseMapper responseMapper;

  public HomeNetworkDataSourceImp(ApiServiceExecutor serviceExecutor, OcmApiService apiService,
      ApiGenericResponseMapper responseMapper) {

    this.serviceExecutor = serviceExecutor;
    this.apiService = apiService;
    this.responseMapper = responseMapper;
  }

  @Override public BusinessObject<ContentData> getSectionData(String section) {
    ApiGenericResponse apiGenericResponse =
        serviceExecutor.executeNetworkServiceConnection(ApiSectionContentData.class,
            apiService.getSectionData(section));

    BusinessObjectMapper mapper = new BusinessObjectMapper();
    return mapper.mapToBusinessObjectFromGggLibToInteractorExecutor(
        responseMapper.mapApiGenericResponseToBusiness(apiGenericResponse));
  }
}

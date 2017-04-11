package com.gigigo.orchextra.core.data.api;

import com.gigigo.ggglib.network.executors.ApiServiceExecutor;
import com.gigigo.ggglib.network.mappers.ApiGenericResponseMapper;
import com.gigigo.ggglib.network.responses.ApiGenericResponse;
import com.gigigo.interactorexecutor.responses.BusinessObject;
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentData;
import com.gigigo.orchextra.core.data.api.mappers.BusinessObjectMapper;
import com.gigigo.orchextra.core.data.api.services.OcmApiService;
import com.gigigo.orchextra.core.domain.data.SearcherNetworkDataSource;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;

public class SearcherNetworkDataSourceImp implements SearcherNetworkDataSource {

  private final ApiServiceExecutor serviceExecutor;
  private final OcmApiService apiService;
  private final ApiGenericResponseMapper responseMapper;

  public SearcherNetworkDataSourceImp(ApiServiceExecutor serviceExecutor, OcmApiService apiService,
      ApiGenericResponseMapper responseMapper) {
    this.serviceExecutor = serviceExecutor;
    this.apiService = apiService;
    this.responseMapper = responseMapper;
  }

  @Override public BusinessObject<ContentData> doSearch(String textToSearch) {
    ApiGenericResponse apiGenericResponse =
        serviceExecutor.executeNetworkServiceConnection(ApiSectionContentData.class,
            apiService.search(textToSearch));

    BusinessObjectMapper mapper = new BusinessObjectMapper();
    return mapper.mapToBusinessObjectFromGggLibToInteractorExecutor(
        responseMapper.mapApiGenericResponseToBusiness(apiGenericResponse));
  }
}

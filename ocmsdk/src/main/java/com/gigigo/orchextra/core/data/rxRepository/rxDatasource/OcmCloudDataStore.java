package com.gigigo.orchextra.core.data.rxRepository.rxDatasource;

import android.support.annotation.NonNull;
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentData;
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentDataResponse;
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementDataResponse;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementData;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentData;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentDataResponse;
import com.gigigo.orchextra.core.data.api.services.OcmApiService;
import com.gigigo.orchextra.core.data.rxCache.OcmCache;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import io.reactivex.Observable;

import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;

/**
 * Created by francisco.hernandez on 23/5/17.
 */

@Singleton public class OcmCloudDataStore implements OcmDataStore {

  private final OcmApiService ocmApiService;
  private final OcmCache ocmCache;

  @Inject
  public OcmCloudDataStore(@NonNull OcmApiService ocmApiService, @NonNull OcmCache ocmCache) {
    this.ocmApiService = ocmApiService;
    this.ocmCache = ocmCache;
  }

  @Override public Observable<ApiMenuContentData> getMenuEntity() {
    return ocmApiService.getMenuDataRx().map(dataResponse -> dataResponse.getResult())
        .doOnNext(apiMenuContentDataResponse -> ocmCache.putMenus(
            apiMenuContentDataResponse));
  }

  @Override public Observable<ApiSectionContentData> getSectionEntity(String elementUrl) {
    return ocmApiService.getSectionDataRx(elementUrl).map(dataResponse -> dataResponse.getResult())
        .doOnNext(apiSectionContentData -> ocmCache.putSection(apiSectionContentData));
  }

  @Override public Observable<ApiSectionContentData> searchByText(String section) {
    return ocmApiService.searchRx(section).map(dataResponse -> dataResponse.getResult());
  }

  @Override public Observable<ApiElementData> getElementById(String section) {
    return ocmApiService.getElementByIdRx(section).map(dataResponse -> dataResponse.getResult());
  }
}

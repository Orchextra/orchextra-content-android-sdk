package com.gigigo.orchextra.core.data.rxRepository.rxDatasource;

import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentDataResponse;
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementDataResponse;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentDataResponse;
import com.gigigo.orchextra.core.data.rxCache.OcmCache;
import io.reactivex.Observable;
import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;

/**
 * Created by francisco.hernandez on 23/5/17.
 */

@Singleton public class OcmDiskDataStore implements OcmDataStore {
  private final OcmCache ocmCache;

  @Inject public OcmDiskDataStore(OcmCache ocmCache) {
    this.ocmCache = ocmCache;
  }

  @Override public Observable<ApiMenuContentDataResponse> getMenuEntity() {
    return null;
  }

  @Override public Observable<ApiSectionContentDataResponse> getSectionEntity(String elementUrl) {
    return null;
  }

  @Override public Observable<ApiSectionContentDataResponse> searchByText(String section) {
    return null;
  }

  @Override public Observable<ApiElementDataResponse> getElementById(String section) {
    return null;
  }
}

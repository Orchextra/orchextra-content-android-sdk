package com.gigigo.orchextra.core.data.rxRepository.rxDatasource;

import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentData;
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentDataResponse;
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementDataResponse;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementData;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentData;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentDataResponse;
import com.gigigo.orchextra.core.data.rxCache.OcmCache;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
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

  @Override public Observable<ApiMenuContentData> getMenuEntity() {
    return ocmCache.getMenus();
  }

  @Override public Observable<ApiSectionContentData> getSectionEntity(String elementUrl) {
    return null;
  }

  @Override public Observable<ApiSectionContentData> searchByText(String section) {
    return null;
  }

  @Override public Observable<ApiElementData> getElementById(String section) {
    return null;
  }

  public OcmCache getOcmCache() {
    return ocmCache;
  }
}

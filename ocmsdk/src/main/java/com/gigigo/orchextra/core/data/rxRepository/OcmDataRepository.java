package com.gigigo.orchextra.core.data.rxRepository;

import com.gigigo.orchextra.core.data.api.mappers.menus.ApiMenuContentListResponseMapper;
import com.gigigo.orchextra.core.data.rxRepository.rxDatasource.OcmDataStore;
import com.gigigo.orchextra.core.data.rxRepository.rxDatasource.OcmDataStoreFactory;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItem;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import com.gigigo.orchextra.core.domain.rxRepository.OcmRepository;
import io.reactivex.Observable;
import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;

/**
 * Created by francisco.hernandez on 23/5/17.
 */

@Singleton
public class OcmDataRepository implements OcmRepository {
  private final OcmDataStoreFactory ocmDataStoreFactory;
  private final ApiMenuContentListResponseMapper apiMenuContentListResponseMapper;

  @Inject
  public OcmDataRepository(OcmDataStoreFactory ocmDataStoreFactory,
      ApiMenuContentListResponseMapper apiMenuContentListResponseMapper) {
    this.ocmDataStoreFactory = ocmDataStoreFactory;
    this.apiMenuContentListResponseMapper = apiMenuContentListResponseMapper;
  }

  @Override public Observable<MenuContentData> getMenu(boolean forceReload) {
    OcmDataStore ocmDataStore = ocmDataStoreFactory.getCloudDataStore();
    return ocmDataStore.getMenuEntity().map(apiMenuContentListResponseMapper::externalClassToModel);
  }

  @Override public Observable<ElementCache> getCachedElement(String elementUrl) {
    return null;
  }

  @Override public Observable<ElementCache> getElementCacheBySection(String section) {
    return null;
  }

  @Override public Observable<String> getContentUrlBySection(String section) {
    return null;
  }

  @Override public Observable<ContentItem> getSectionContentById(String section) {
    return null;
  }
}

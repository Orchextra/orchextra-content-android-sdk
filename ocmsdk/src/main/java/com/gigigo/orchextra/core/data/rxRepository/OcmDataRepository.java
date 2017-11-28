package com.gigigo.orchextra.core.data.rxRepository;

import com.gigigo.orchextra.core.data.api.dto.versioning.ApiVersionKache;
import com.gigigo.orchextra.core.data.api.mappers.contentdata.ApiContentDataResponseMapper;
import com.gigigo.orchextra.core.data.api.mappers.elements.ApiElementDataMapper;
import com.gigigo.orchextra.core.data.api.mappers.menus.ApiMenuContentListResponseMapper;
import com.gigigo.orchextra.core.data.api.mappers.version.ApiVersionMapper;
import com.gigigo.orchextra.core.data.rxRepository.rxDatasource.OcmDataStore;
import com.gigigo.orchextra.core.data.rxRepository.rxDatasource.OcmDataStoreFactory;
import com.gigigo.orchextra.core.data.rxRepository.rxDatasource.OcmDiskDataStore;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.elements.ElementData;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import com.gigigo.orchextra.core.domain.entities.version.VersionData;
import com.gigigo.orchextra.core.domain.rxRepository.OcmRepository;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import java.util.Map;
import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;

/**
 * Created by francisco.hernandez on 23/5/17.
 */

@Singleton public class OcmDataRepository implements OcmRepository {
  private final OcmDataStoreFactory ocmDataStoreFactory;
  private final ApiMenuContentListResponseMapper apiMenuContentListResponseMapper;
  private final ApiContentDataResponseMapper apiContentDataResponseMapper;
  private final ApiElementDataMapper apiElementDataMapper;
  private final ApiVersionMapper apiVersionMapper;

  @Inject public OcmDataRepository(OcmDataStoreFactory ocmDataStoreFactory,
      ApiMenuContentListResponseMapper apiMenuContentListResponseMapper,
      ApiContentDataResponseMapper apiContentDataResponseMapper,
      ApiElementDataMapper apiElementDataMapper,
      ApiVersionMapper apiVersionMapper) {
    this.ocmDataStoreFactory = ocmDataStoreFactory;
    this.apiMenuContentListResponseMapper = apiMenuContentListResponseMapper;
    this.apiContentDataResponseMapper = apiContentDataResponseMapper;
    this.apiElementDataMapper = apiElementDataMapper;
    this.apiVersionMapper = apiVersionMapper;
  }

  @Override public Observable<VersionData> getVersion() {
    OcmDataStore ocmDataStore = ocmDataStoreFactory.getDataStoreForVersion();
    return ocmDataStore.getVersion()
        .map(apiVersionMapper::externalClassToModel);
  }

  @Override public Observable<MenuContentData> getMenu(boolean forceReload) {
    OcmDataStore ocmDataStore = ocmDataStoreFactory.getDataStoreForMenus(forceReload);
    return ocmDataStore.getMenuEntity().map(apiMenuContentListResponseMapper::externalClassToModel);
  }

  @Override
  public Observable<ContentData> getSectionElements(boolean forceReload, String contentUrl,
      int numberOfElementsToDownload) {
    OcmDataStore ocmDataStore =
        ocmDataStoreFactory.getDataStoreForSections(forceReload, contentUrl);
    Observable<ContentData> contentDataObservable =
        ocmDataStore.getSectionEntity(contentUrl, numberOfElementsToDownload)
            .map(apiContentDataResponseMapper::externalClassToModel);

    if (!ocmDataStore.isFromCloud()) {
      final boolean[] hasTobeUpdated = { false };

      Observable<ContentData> forcedContentDataObservable =
          contentDataObservable.map(ContentData::getElementsCache)
              .map(Map::entrySet)
              .flatMapIterable(entries -> entries)
              .map(Map.Entry::getValue)
              .map(ElementCache::getUpdateAt)
              .filter(this::checkDate)
              .take(1)
              .flatMap(aLong -> {
                hasTobeUpdated[0] = true;
                return getSectionElements(true, contentUrl, numberOfElementsToDownload);
              });

      if (hasTobeUpdated[0]) {
        contentDataObservable = forcedContentDataObservable;
      }
    }

    return contentDataObservable;
  }

  private boolean checkDate(Long updateAt) {
    //TODO Change to expiredAt
    long current = System.currentTimeMillis();
    return updateAt > current;
  }

  @Override public Observable<ElementData> getDetail(boolean forceReload, String elementUrl) {
    OcmDataStore ocmDataStore = ocmDataStoreFactory.getDataStoreForDetail(forceReload, elementUrl);
    return ocmDataStore.getElementById(elementUrl).map(apiElementDataMapper::externalClassToModel);
  }

  @Override public Observable<ContentData> doSearch(String textToSearch) {
    OcmDataStore ocmDataStore = ocmDataStoreFactory.getCloudDataStore();
    return ocmDataStore.searchByText(textToSearch)
        .map(apiContentDataResponseMapper::externalClassToModel);
  }

  @Override public Observable<Void> clear(boolean images, boolean data) {
    OcmDiskDataStore ocmDataStore = (OcmDiskDataStore) ocmDataStoreFactory.getDiskDataStore();
    ocmDataStore.getOcmCache().evictAll(images, data);
    return Observable.empty();
  }
}

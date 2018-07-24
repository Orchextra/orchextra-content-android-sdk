package com.gigigo.orchextra.core.data.rxRepository;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import com.gigigo.orchextra.core.data.DateUtilsKt;
import com.gigigo.orchextra.core.data.OcmDbDataSource;
import com.gigigo.orchextra.core.data.OcmNetworkDataSource;
import com.gigigo.orchextra.core.data.rxRepository.rxDatasource.OcmDataStore;
import com.gigigo.orchextra.core.data.rxRepository.rxDatasource.OcmDataStoreFactory;
import com.gigigo.orchextra.core.data.rxRepository.rxDatasource.OcmDiskDataStore;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.elements.Element;
import com.gigigo.orchextra.core.domain.entities.elements.ElementData;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContent;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import com.gigigo.orchextra.core.domain.rxRepository.OcmRepository;
import gigigo.com.vimeolibs.VimeoInfo;
import io.reactivex.Observable;
import java.util.Map;
import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;
import timber.log.Timber;

@Singleton public class OcmDataRepository implements OcmRepository {
  private final OcmDataStoreFactory ocmDataStoreFactory;
  private final OcmDbDataSource ocmDbDataSource;
  private final OcmNetworkDataSource ocmNetworkDataSource;

  @Inject
  public OcmDataRepository(OcmDataStoreFactory ocmDataStoreFactory, OcmDbDataSource ocmDbDataSource,
      OcmNetworkDataSource ocmNetworkDataSource) {
    this.ocmDataStoreFactory = ocmDataStoreFactory;
    this.ocmDbDataSource = ocmDbDataSource;
    this.ocmNetworkDataSource = ocmNetworkDataSource;
  }

  @Override public Observable<MenuContentData> getMenus(boolean forceReload) {

    return Observable.create(emitter -> {
      MenuContentData cacheMenuContentData = ocmDbDataSource.getMenus();
      MenuContentData networkMenuContentData = ocmNetworkDataSource.getMenus();

      emitter.onNext(getUpdatedMenuContentData(cacheMenuContentData, networkMenuContentData));
      emitter.onComplete();
    });
  }

  @Override
  public Observable<ContentData> getSectionElements(boolean forceReload, String contentUrl,
      int numberOfElementsToDownload) {
    OcmDataStore ocmDataStore = ocmDataStoreFactory.getDataStoreForSection(forceReload, contentUrl);
    Observable<ContentData> contentDataObservable =
        ocmDataStore.getSection(contentUrl, numberOfElementsToDownload);

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
    long current = DateUtilsKt.getToday();
    return updateAt > current;
  }

  @Override public Observable<ContentData> doSearch(String textToSearch) {
    OcmDataStore ocmDataStore = ocmDataStoreFactory.getCloudDataStore();
    return ocmDataStore.searchByText(textToSearch);
  }

  @Override public Observable<ElementData> getDetail(boolean forceReload, String elementUrl) {
    OcmDataStore ocmDataStore = ocmDataStoreFactory.getDataStoreForDetail(forceReload, elementUrl);
    return ocmDataStore.getElementById(elementUrl);
  }

  @Override
  public Observable<VimeoInfo> getVideo(Context context, boolean forceReload, String videoId,
      boolean isWifiConnection, boolean isFastConnection) {
    OcmDataStore ocmDataStore = ocmDataStoreFactory.getDataStoreForVideo(forceReload, videoId);
    return ocmDataStore.getVideoById(context, videoId, isWifiConnection, isFastConnection);
  }

  @WorkerThread @Override public Observable<Void> clear(boolean images, boolean data) {
    OcmDiskDataStore ocmDataStore = ocmDataStoreFactory.getDiskDataStore();
    ocmDataStore.getOcmCache().evictAll(images, data);
    return Observable.empty();
  }

  private MenuContentData getUpdatedMenuContentData(@NonNull MenuContentData cacheMenuContentData,
      @NonNull MenuContentData networkMenuContentData) {

    if (cacheMenuContentData.getMenuContentList().isEmpty()) {
      Timber.i("Data from cloud");
      return networkMenuContentData;
    }

    MenuContentData updatedMenuContentData = new MenuContentData();
    updatedMenuContentData.setElementsCache(networkMenuContentData.getElementsCache());
    updatedMenuContentData.setMenuContentList(networkMenuContentData.getMenuContentList());

    for (MenuContent menuContent : networkMenuContentData.getMenuContentList()) {
      for (Element element : menuContent.getElements()) {

        Boolean updated = checkContentVersion(element, cacheMenuContentData);
        Timber.d("Element %s; Updated %s", element.getSlug(), updated);
        element.setHasNewVersion(updated);
      }
    }

    return updatedMenuContentData;
  }

  private Boolean checkContentVersion(@NonNull Element element,
      @NonNull MenuContentData cacheMenuContentData) {
    for (MenuContent menuContent : cacheMenuContentData.getMenuContentList()) {
      for (Element cacheElement : menuContent.getElements()) {
        if (cacheElement.getSlug().equals(element.getSlug())) {
          return cacheElement.getContentVersion() == null || !cacheElement.getContentVersion()
              .equals(element.getContentVersion());
        }
      }
    }
    return false;
  }
}

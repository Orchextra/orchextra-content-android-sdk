package com.gigigo.orchextra.core.data.rxRepository.rxDatasource;

import android.content.Context;
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentData;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementData;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentData;
import com.gigigo.orchextra.core.data.api.dto.versioning.ApiVersionKache;
import com.gigigo.orchextra.core.data.api.dto.video.ApiVideoData;
import com.gigigo.orchextra.core.data.rxCache.OcmCache;
import io.reactivex.Observable;
import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;

@Singleton public class OcmDiskDataStore implements OcmDataStore {
  private final OcmCache ocmCache;

  @Inject public OcmDiskDataStore(OcmCache ocmCache) {
    this.ocmCache = ocmCache;
  }

  @Override public Observable<ApiMenuContentData> getMenuEntity() {
    return ocmCache.getMenus().doOnNext(apiMenuContentData -> {
      apiMenuContentData.setFromCloud(false);
    });
  }

  @Override public Observable<ApiSectionContentData> getSectionEntity(String elementUrl,
      int numberOfElementsToDownload) {
    return ocmCache.getSection(elementUrl).doOnNext(apiSectionContentData -> {
      apiSectionContentData.setFromCloud(false);
    });
  }

  @Override public Observable<ApiSectionContentData> searchByText(String section) {
    return null;
  }

  @Override public Observable<ApiElementData> getElementById(String slug) {
    return ocmCache.getDetail(slug);
  }

  @Override public Observable<ApiVideoData> getVideoById(Context context, String videoId,
      boolean isWifiConnection, boolean isFastConnection) {
    return ocmCache.getVideo(videoId);
  }

  @Override public Observable<ApiVersionKache> getVersion() {
    return ocmCache.getVersion();
  }

  @Override public boolean isFromCloud() {
    return false;
  }

  public OcmCache getOcmCache() {
    return ocmCache;
  }
}

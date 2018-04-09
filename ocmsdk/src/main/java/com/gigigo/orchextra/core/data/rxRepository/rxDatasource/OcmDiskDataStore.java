package com.gigigo.orchextra.core.data.rxRepository.rxDatasource;

import android.content.Context;
import com.gigigo.orchextra.core.data.mappers.DbMappersKt;
import com.gigigo.orchextra.core.data.rxCache.OcmCache;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.elements.ElementData;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import com.gigigo.orchextra.core.domain.entities.version.VersionData;
import gigigo.com.vimeolibs.VimeoInfo;
import io.reactivex.Observable;
import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;

@Singleton public class OcmDiskDataStore implements OcmDataStore {
  private final OcmCache ocmCache;

  @Inject public OcmDiskDataStore(OcmCache ocmCache) {
    this.ocmCache = ocmCache;
  }

  @Override public Observable<VersionData> getVersion() {
    return ocmCache.getVersion().map(dbVersionData -> DbMappersKt.toVersionData(dbVersionData));
  }

  @Override public Observable<MenuContentData> getMenus() {
    return ocmCache.getMenus()
        .map(dbMenuContentData -> DbMappersKt.toMenuContentData(dbMenuContentData));
  }

  @Override
  public Observable<ContentData> getSection(String elementUrl, int numberOfElementsToDownload) {
    return ocmCache.getSection(elementUrl)
        .map(dbSectionContentData -> DbMappersKt.toContentData(dbSectionContentData));
  }

  @Override public Observable<ContentData> searchByText(String section) {
    return null;
  }

  @Override public Observable<ElementData> getElementById(String slug) {
    return ocmCache.getDetail(slug);
  }

  @Override public Observable<VimeoInfo> getVideoById(Context context, String videoId,
      boolean isWifiConnection, boolean isFastConnection) {
    return ocmCache.getVideo(videoId).map(dbVideoData -> DbMappersKt.toVimeoInfo(dbVideoData));
  }

  @Override public boolean isFromCloud() {
    return false;
  }

  public OcmCache getOcmCache() {
    return ocmCache;
  }
}
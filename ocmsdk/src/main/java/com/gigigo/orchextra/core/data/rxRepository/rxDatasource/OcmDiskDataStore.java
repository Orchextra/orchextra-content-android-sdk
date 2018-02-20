package com.gigigo.orchextra.core.data.rxRepository.rxDatasource;

import android.content.Context;
import android.util.Log;
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentData;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementData;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentData;
import com.gigigo.orchextra.core.data.api.dto.versioning.ApiVersionKache;
import com.gigigo.orchextra.core.data.api.dto.video.ApiVideoData;
import com.gigigo.orchextra.core.data.rxCache.OcmCache;
import gigigo.com.vimeolibs.VimeoInfo;
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
    final long time = System.currentTimeMillis();

    return ocmCache.getMenus().doOnNext(apiMenuContentData -> {
      apiMenuContentData.setFromCloud(false);
      Log.v("TT - DISK - Menus", (System.currentTimeMillis() - time) / 1000 + "");
    });
}

  @Override public Observable<ApiSectionContentData> getSectionEntity(String elementUrl, int numberOfElementsToDownload) {
    final long time = System.currentTimeMillis();

    return ocmCache.getSection(elementUrl).doOnNext(apiSectionContentData -> {
      apiSectionContentData.setFromCloud(false);

      Log.v("TT - DISK - Sections", (System.currentTimeMillis() - time) / 1000 + "");
    });
  }

  @Override public Observable<ApiSectionContentData> searchByText(String section) {
    return null;
  }

  @Override public Observable<ApiElementData> getElementById(String slug) {
    final long time = System.currentTimeMillis();

    return ocmCache.getDetail(slug).doOnNext(apiElementData ->
        Log.v("TT - DISK - Details", (System.currentTimeMillis() - time) / 1000 + ""));
  }

  @Override public Observable<VimeoInfo> getVideoById(Context context, String videoId, boolean isWifiConnection,
      boolean isFastConnection) {
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

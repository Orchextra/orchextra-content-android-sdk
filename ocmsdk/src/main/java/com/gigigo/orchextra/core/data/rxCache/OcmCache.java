package com.gigigo.orchextra.core.data.rxCache;

import android.content.Context;
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentData;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementData;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentData;
import com.gigigo.orchextra.core.data.api.dto.versioning.ApiVersionKache;
import com.gigigo.orchextra.core.data.api.dto.video.ApiVideoData;
import io.reactivex.Observable;

public interface OcmCache {

  Observable<ApiMenuContentData> getMenus();

  void putMenus(ApiMenuContentData apiMenuContentData);

  boolean isMenuCached();

  boolean isMenuExpired();

  Observable<ApiSectionContentData> getSection(String elementUrl);

  void putSection(ApiSectionContentData apiSectionContentData);

  boolean isSectionCached(String elementUrl);

  boolean isSectionExpired(String elementUrl);

  Observable<ApiElementData> getDetail(String slug);

  void putDetail(ApiElementData apiElementData);

  boolean isDetailCached(String slug);

  boolean isDetailExpired(String slug);

  Observable<ApiVideoData> getVideo(String videoId);

  void putVideo(ApiVideoData videoData);

  boolean isVideoCached(String videoId);

  boolean isVideoExpired(String videoId);

  void evictAll(boolean images, boolean data);

  Context getContext();

  void putVersion(ApiVersionKache apiVersionKache);

  Observable<ApiVersionKache> getVersion();

  boolean isVersionCached();

  boolean isVersionExpired();
}

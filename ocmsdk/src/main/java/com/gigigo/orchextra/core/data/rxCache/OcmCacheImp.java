package com.gigigo.orchextra.core.data.rxCache;

import android.content.Context;
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentData;
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentDataResponse;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentData;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentDataResponse;
import com.gigigo.orchextra.core.data.rxException.ApiMenuNotFoundException;
import com.gigigo.orchextra.core.data.rxException.ApiSectionNotFoundException;
import com.gigigo.orchextra.core.sdk.di.qualifiers.CacheDir;
import com.mskn73.kache.Kache;
import io.reactivex.Observable;
import javax.inject.Named;
import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;

/**
 * Created by francisco.hernandez on 23/5/17.
 */

@Singleton public class OcmCacheImp implements OcmCache {

  private final Kache kache;

  public static final String MENU_KEY = "MENU_KEY";

  @Inject public OcmCacheImp(Context context, @CacheDir String cacheDir) {
    this.kache = new Kache(context, cacheDir);
  }

  @Override public Observable<ApiMenuContentData> getMenus() {
    return Observable.create(emitter -> {
      ApiMenuContentData apiMenuContentData =
          (ApiMenuContentData) kache.get(ApiMenuContentData.class, MENU_KEY);

      if (apiMenuContentData != null) {
        emitter.onNext(apiMenuContentData);
        emitter.onComplete();
      } else {
        emitter.onError(new ApiMenuNotFoundException());
      }
    });
  }

  @Override public void putMenus(ApiMenuContentData apiMenuContentData) {
    if (apiMenuContentData != null) kache.put(apiMenuContentData);
  }

  @Override public boolean isMenuCached() {
    return kache.isCached(MENU_KEY);
  }

  @Override public boolean isMenuExpired() {
    return kache.isExpired(MENU_KEY, ApiMenuContentDataResponse.class);
  }

  @Override public Observable<ApiSectionContentData> getSection(final String elementUrl) {
    return Observable.create(emitter -> {
      ApiSectionContentData apiSectionContentData =
          (ApiSectionContentData) kache.get(ApiSectionContentData.class, elementUrl);

      if (apiSectionContentData != null) {
        emitter.onNext(apiSectionContentData);
        emitter.onComplete();
      } else {
        emitter.onError(new ApiSectionNotFoundException());
      }
    });
  }

  @Override public void putSection(ApiSectionContentData apiSectionContentData) {
    if (apiSectionContentData != null) kache.put(apiSectionContentData);
  }

  @Override public boolean isSectionCached(String elementUrl) {
    return kache.isCached(elementUrl);
  }

  @Override public boolean isSectionExpired(String elementUrl) {
    return kache.isExpired(elementUrl, ApiSectionContentData.class);
  }
}

package com.gigigo.orchextra.core.data.rxCache;

import android.content.Context;
import com.gigigo.ggglogger.GGGLogImpl;
import com.gigigo.ggglogger.LogLevel;
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentData;
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentDataResponse;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementData;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentData;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentDataResponse;
import com.gigigo.orchextra.core.data.rxException.ApiMenuNotFoundException;
import com.gigigo.orchextra.core.data.rxException.ApiSectionNotFoundException;
import com.gigigo.orchextra.core.sdk.di.qualifiers.CacheDir;
import com.mskn73.kache.Kache;
import io.reactivex.Observable;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import javax.inject.Named;
import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;

/**
 * Created by francisco.hernandez on 23/5/17.
 */

@Singleton public class OcmCacheImp implements OcmCache {

  private final Kache kache;
  private final Context mContext;

  public static final String MENU_KEY = "MENU_KEY";

  @Inject public OcmCacheImp(Context context, @CacheDir String cacheDir) {
    this.kache = new Kache(context, cacheDir);
    this.mContext = context;
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
    if (apiMenuContentData != null) {
      kache.evict(MENU_KEY);
      kache.put(apiMenuContentData);
    }
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
    if (apiSectionContentData != null) {
      kache.evict(apiSectionContentData.getKey());
      kache.put(apiSectionContentData);
    }
  }

  @Override public boolean isSectionCached(String elementUrl) {
    return kache.isCached(elementUrl);
  }

  @Override public boolean isSectionExpired(String elementUrl) {
    return kache.isExpired(elementUrl, ApiSectionContentData.class);
  }

  @Override public Observable<ApiElementData> getDetail(String slug) {
    return Observable.create(emitter -> {
      ApiElementData apiElementData = (ApiElementData) kache.get(ApiElementData.class, slug);

      if (apiElementData != null) {
        emitter.onNext(apiElementData);
        emitter.onComplete();
      } else {
        emitter.onError(new ApiSectionNotFoundException());
      }
    });
  }

  @Override public void putDetail(ApiElementData apiElementData) {
    if (apiElementData != null && apiElementData.getKey() != null) {
      kache.evict(apiElementData.getKey());
      kache.put(apiElementData);
    }
  }

  @Override public boolean isDetailCached(String slug) {
    return kache.isCached(slug);
  }

  @Override public boolean isDetailExpired(String slug) {
    return kache.isExpired(slug, ApiElementData.class);
  }

  @Override public void evictAll(boolean images, boolean data) {
    if (data) trimCache(mContext, "kache");
    if (images) trimCache(mContext, "images");
  }

  private void trimCache(Context context, String folder) {
    try {
      File dir = new File(context.getCacheDir().getPath() + "/" + folder);
      if (dir != null && dir.isDirectory()) {
        deleteDir(dir);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public boolean deleteDir(File dir) {
    if (dir != null && dir.isDirectory()) {
      String[] children = dir.list();
      for (int i = 0; i < children.length; i++) {
        boolean success = deleteDir(new File(dir, children[i]));
        if (!success) {
          return false;
        }
      }
    }

    // The directory is now empty so delete it

    boolean deleted = dir.delete();
    GGGLogImpl.log((deleted + " -> ").toUpperCase() + dir.getPath(), LogLevel.INFO, "CLEAR-CACHE");
    return deleted;
  }

  @Override public Context getContext() {
    return mContext;
  }
}

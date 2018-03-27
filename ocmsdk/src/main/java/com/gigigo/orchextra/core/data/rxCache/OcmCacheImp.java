package com.gigigo.orchextra.core.data.rxCache;

import android.content.Context;
import com.gigigo.ggglogger.GGGLogImpl;
import com.gigigo.ggglogger.LogLevel;
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentData;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementData;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentData;
import com.gigigo.orchextra.core.data.api.dto.versioning.ApiVersionData;
import com.gigigo.orchextra.core.data.api.dto.video.ApiVideoData;
import com.gigigo.orchextra.core.data.database.OcmDatabase;
import com.gigigo.orchextra.core.data.database.entities.DbElementCache;
import com.gigigo.orchextra.core.data.database.entities.DbMenuContent;
import com.gigigo.orchextra.core.data.database.entities.DbMenuContentData;
import com.gigigo.orchextra.core.data.database.entities.DbVersionData;
import com.gigigo.orchextra.core.data.rxException.ApiMenuNotFoundException;
import com.gigigo.orchextra.core.data.rxException.ApiSectionNotFoundException;
import com.gigigo.orchextra.core.data.rxRepository.DbMappersKt;
import com.gigigo.orchextra.core.sdk.di.qualifiers.CacheDir;
import com.mskn73.kache.Kache;
import io.reactivex.Observable;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;

@Singleton public class OcmCacheImp implements OcmCache {
  private final OcmDatabase ocmDatabase;
  private final Kache kache;
  private final Context mContext;

  @Inject public OcmCacheImp(Context context, @CacheDir String cacheDir) {
    this.ocmDatabase = OcmDatabase.Companion.create(context);
    this.kache = new Kache(context, cacheDir);
    this.mContext = context;
  }

  //region VERSION
  @Override public Observable<DbVersionData> getVersion() {
    return Observable.create(emitter -> {
      DbVersionData versionData = ocmDatabase.versionDao().fetchVersion(DbVersionData.VERSION_KEY);

      if (versionData != null) {
        emitter.onNext(versionData);
        emitter.onComplete();
      } else {
        emitter.onNext(null);
      }
    });
  }

  @Override public boolean isVersionCached() {
    int versionDataCount = ocmDatabase.versionDao().hasVersion(DbVersionData.VERSION_KEY);
    return (versionDataCount == 1);
  }

  @Override public boolean isVersionExpired() {
    return false;
  }

  @Override public void putVersion(ApiVersionData apiVersionData) {
    DbVersionData versionData = DbMappersKt.toDbVersionData(apiVersionData);
    ocmDatabase.versionDao().insertVersion(versionData);
  }
  //endregion

  //region MENUS
  @Override public Observable<DbMenuContentData> getMenus() {
    return Observable.create(emitter -> {
      DbMenuContentData menuContentData = new DbMenuContentData();

      List<DbMenuContent> contentList = ocmDatabase.menuDao().fetchMenus();
      menuContentData.setMenuContentList(contentList);

      List<DbElementCache> elementsCacheList = ocmDatabase.elementCacheDao().fetchElementsCache();
      Map<String, DbElementCache> elementsCache = new HashMap<>();
      for (DbElementCache element : elementsCacheList) {
        elementsCache.put(element.getSlug(), element);
      }
      menuContentData.setElementsCache(elementsCache);

      if (menuContentData != null) {
        emitter.onNext(menuContentData);
        emitter.onComplete();
      } else {
        emitter.onError(new ApiMenuNotFoundException());
      }
    });
  }

  @Override public boolean isMenuCached() {
    int menuDataCount = ocmDatabase.menuDao().hasMenus("");
    return (menuDataCount == 1);
  }

  @Override public boolean isMenuExpired() {
    return false;
  }

  @Override public void putMenus(ApiMenuContentData apiMenuContentData) {
    if (apiMenuContentData != null) {
      DbMenuContentData menuContentData = DbMappersKt.toDbMenuContentData(apiMenuContentData);

      for (DbMenuContent menu : menuContentData.getMenuContentList()) {
        ocmDatabase.menuDao().insertMenu(menu);
      }

      Iterator<Map.Entry<String, DbElementCache>> iterator = menuContentData.getElementsCache().entrySet().iterator();
      while (iterator.hasNext()) {
        Map.Entry<String, DbElementCache> element = iterator.next();
        ocmDatabase.elementCacheDao().insertElementCache(element.getValue());
      }

      //TODO: insert "menus" "elements" and join table
    }
  }
  //endregion

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

  @Override public Observable<ApiVideoData> getVideo(String videoId) {
    return Observable.create(emitter -> {
      ApiVideoData videoData = (ApiVideoData) kache.get(ApiVideoData.class, videoId);

      if (videoData != null) {
        emitter.onNext(videoData);
        emitter.onComplete();
      } else {
        emitter.onError(new ApiSectionNotFoundException());
      }
    });
  }

  @Override public void putVideo(ApiVideoData videoData) {
    if (videoData != null && videoData.getKey() != null) {
      kache.evict(videoData.getKey());
      kache.put(videoData);
    }
  }

  @Override public boolean isVideoCached(String videoId) {
    return kache.isCached(videoId);
  }

  @Override public boolean isVideoExpired(String videoId) {
    return kache.isExpired(videoId, ApiVideoData.class);
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

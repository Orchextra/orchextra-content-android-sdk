package com.gigigo.orchextra.core.data.rxCache;

import android.content.Context;
import com.gigigo.ggglogger.GGGLogImpl;
import com.gigigo.ggglogger.LogLevel;
import com.gigigo.orchextra.core.data.DateUtilsKt;
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentData;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentData;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentDataResponse;
import com.gigigo.orchextra.core.data.database.OcmDatabase;
import com.gigigo.orchextra.core.data.database.entities.DbElementCache;
import com.gigigo.orchextra.core.data.database.entities.DbVersionData;
import com.gigigo.orchextra.core.data.database.entities.DbVideoData;
import com.gigigo.orchextra.core.data.mappers.DbMappersKt;
import com.gigigo.orchextra.core.data.rxException.ApiMenuNotFoundException;
import com.gigigo.orchextra.core.data.rxException.ApiSectionNotFoundException;
import com.gigigo.orchextra.core.domain.entities.elements.ElementData;
import com.gigigo.orchextra.core.domain.entities.version.VersionData;
import com.gigigo.orchextra.core.sdk.di.qualifiers.CacheDir;
import com.mskn73.kache.Kache;
import gigigo.com.vimeolibs.VimeoInfo;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;

@Singleton public class OcmCacheImp implements OcmCache {
  public static final String MENU_KEY = "MENU_KEY";
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

  @Override public void putVersion(VersionData versionData) {
    DbVersionData dbVersionData = DbMappersKt.toDbVersionData(versionData);
    ocmDatabase.versionDao().insertVersion(dbVersionData);
  }
  //endregion

  //region MENU
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
  //endregion

  //region ELEMENT
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
  //endregion

  //region ELEMENT
  @Override public Observable<ElementData> getDetail(String slug) {
    return Observable.create(emitter -> {
      DbElementCache elementCacheData = ocmDatabase.elementCacheDao().fetchElementCache(slug);

      ElementData elementData = new ElementData();
      elementData.setElement(DbMappersKt.toElementCache(elementCacheData));

      if (elementData != null) {
        emitter.onNext(elementData);
        emitter.onComplete();
      } else {
        emitter.onError(new ApiSectionNotFoundException());
      }
    });
  }

  @Override public boolean isDetailCached(String slug) {
    int detailDataCount = ocmDatabase.elementCacheDao().hasElementCache(slug);
    return (detailDataCount == 1);
  }

  @Override public boolean isDetailExpired(String slug) {
    return false;
  }

  @Override public void putDetail(ElementData elementData) {
    DbElementCache elementCacheData = DbMappersKt.toDbElementCache(elementData.getElement());
    ocmDatabase.elementCacheDao().insertElementCache(elementCacheData);
  }
  //endregion

  //region VIDEO
  @Override public Observable<DbVideoData> getVideo(String videoId) {
    return Observable.create(emitter -> {
      DbVideoData videoData = ocmDatabase.videoDao().fetchVideo(videoId);

      if (videoData != null) {
        emitter.onNext(videoData);
        emitter.onComplete();
      } else {
        emitter.onError(new ApiSectionNotFoundException());
      }
    });
  }

  @Override public void putVideo(VimeoInfo vimeoInfo) {
    Observable.create(emitter -> {
      DbVideoData dbVideoData = DbMappersKt.toDbVideoData(vimeoInfo);
      Long maxExpiredTime = DateUtilsKt.getTodayPlusDays(1);
      dbVideoData.setExpireAt(maxExpiredTime);
      ocmDatabase.videoDao().insertVideo(dbVideoData);
      emitter.onComplete();
    }).subscribeOn(Schedulers.io()).subscribe();
  }

  @Override public boolean isVideoCached(String videoId) {
    int videoDataCount = ocmDatabase.videoDao().hasVideo(videoId);
    return (videoDataCount == 1);
  }

  @Override public boolean isVideoExpired(String videoId) {
    int videoDataCount = ocmDatabase.videoDao().hasExpiredVideo(videoId, DateUtilsKt.getToday());
    return (videoDataCount == 1);
  }
  //endregion

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

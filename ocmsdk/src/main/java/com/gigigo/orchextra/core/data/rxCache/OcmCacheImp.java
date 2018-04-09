package com.gigigo.orchextra.core.data.rxCache;

import android.content.Context;
import com.gigigo.ggglogger.GGGLogImpl;
import com.gigigo.ggglogger.LogLevel;
import com.gigigo.orchextra.core.data.DateUtilsKt;
import com.gigigo.orchextra.core.data.api.dto.content.ApiContentItem;
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentData;
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCache;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElement;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementData;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContent;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentData;
import com.gigigo.orchextra.core.data.api.dto.versioning.ApiVersionData;
import com.gigigo.orchextra.core.data.database.OcmDatabase;
import com.gigigo.orchextra.core.data.database.entities.DbElement;
import com.gigigo.orchextra.core.data.database.entities.DbElementCache;
import com.gigigo.orchextra.core.data.database.entities.DbMenuContent;
import com.gigigo.orchextra.core.data.database.entities.DbMenuContentData;
import com.gigigo.orchextra.core.data.database.entities.DbMenuElementJoin;
import com.gigigo.orchextra.core.data.database.entities.DbScheduleDates;
import com.gigigo.orchextra.core.data.database.entities.DbSectionContentData;
import com.gigigo.orchextra.core.data.database.entities.DbSectionElementJoin;
import com.gigigo.orchextra.core.data.database.entities.DbVersionData;
import com.gigigo.orchextra.core.data.database.entities.DbVideoData;
import com.gigigo.orchextra.core.data.mappers.DbMappersKt;
import com.gigigo.orchextra.core.data.rxException.ApiMenuNotFoundException;
import com.gigigo.orchextra.core.data.rxException.ApiSectionNotFoundException;
import com.gigigo.orchextra.core.domain.entities.elements.ElementData;
import com.gigigo.orchextra.core.sdk.di.qualifiers.CacheDir;
import gigigo.com.vimeolibs.VimeoInfo;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;

@Singleton public class OcmCacheImp implements OcmCache {
  private final OcmDatabase ocmDatabase;
  private final Context mContext;

  @Inject public OcmCacheImp(Context context, @CacheDir String cacheDir) {
    this.ocmDatabase = OcmDatabase.Companion.create(context);
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
    DbVersionData dbVersionData = DbMappersKt.toDbVersionData(apiVersionData);
    ocmDatabase.versionDao().insertVersion(dbVersionData);
  }
  //endregion

  //region MENU
  @Override public Observable<DbMenuContentData> getMenus() {
    return Observable.create(emitter -> {
      List<DbMenuContent> dbMenuContentList = ocmDatabase.menuDao().fetchMenus();
      List<DbElementCache> dbElementCacheList = ocmDatabase.elementCacheDao().fetchElementsCache();

      for (DbMenuContent dbMenuContent : dbMenuContentList) {
        //Long today = DateUtilsKt.getToday();
        //List<DbElement> dbElementList = ocmDatabase.elementDao().fetchMenuElementsOnTime(dbMenuContent.getSlug(), today);
        List<DbElement> dbElementList = ocmDatabase.elementDao().fetchMenuElements(dbMenuContent.getSlug());
        dbMenuContent.setElements(dbElementList);
        for (DbElement dbElement : dbElementList) {
          List<DbScheduleDates> dbScheduleDatesList = ocmDatabase.scheduleDatesDao().fetchSchedule(dbElement.getSlug());
          dbElement.setDates(dbScheduleDatesList);
        }
      }

      DbMenuContentData dbMenuContentData = new DbMenuContentData();
      dbMenuContentData.setMenuContentList(dbMenuContentList);

      Map<String, DbElementCache> dbElementCacheMap = new HashMap<>(dbElementCacheList.size());
      for (DbElementCache dbElementCache : dbElementCacheList) {
        dbElementCacheMap.put(dbElementCache.getKey(), dbElementCache);
      }
      dbMenuContentData.setElementsCache(dbElementCacheMap);

      if (dbMenuContentData != null) {
        emitter.onNext(dbMenuContentData);
        emitter.onComplete();
      } else {
        emitter.onError(new ApiMenuNotFoundException());
      }
    });
  }

  @Override public void putMenus(ApiMenuContentData apiMenuContentData) {
    for (ApiMenuContent apiMenuContent : apiMenuContentData.getMenuContentList()) {
      DbMenuContent dbMenuContent = DbMappersKt.toDbMenuContent(apiMenuContent);
      ocmDatabase.menuDao().insertMenu(dbMenuContent);

      for (DbElement dbElement : dbMenuContent.getElements()) {
        ocmDatabase.elementDao().insertElement(dbElement);

        for (DbScheduleDates scheduleDate : dbElement.getDates()) {
          scheduleDate.setSlug(dbElement.getSlug());
          ocmDatabase.scheduleDatesDao().insertSchedule(scheduleDate);
        }

        DbMenuElementJoin dbMenuElementJoin =
            new DbMenuElementJoin(dbMenuContent.getSlug(), dbElement.getSlug());
        ocmDatabase.elementDao().insertMenuElement(dbMenuElementJoin);
      }
    }

    Iterator<Map.Entry<String, ApiElementCache>> iterator =
        apiMenuContentData.getElementsCache().entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<String, ApiElementCache> next = iterator.next();
      ApiElementData apiElementData = new ApiElementData(next.getValue());
      String key = next.getKey();
      putDetail(apiElementData, key);
    }
  }

  @Override public boolean hasMenusCached() {
    int versionDataCount = ocmDatabase.menuDao().hasMenus();
    return (versionDataCount == 1);
  }
  //endregion

  //region SECTION
  @Override public Observable<DbSectionContentData> getSection(final String elementUrl) {
    return Observable.create(emitter -> {
      DbSectionContentData dbSectionContentData = ocmDatabase.sectionDao().fetchSectionContentData(elementUrl);

      List<DbElement> dbSectionContentDataElementList = ocmDatabase.sectionDao().fetchSectionElements(dbSectionContentData.getContent().getSlug());
      dbSectionContentData.getContent().setElements(dbSectionContentDataElementList);

      Map<String, DbElementCache> elementCaches = new HashMap<>();
      for (DbElement element : dbSectionContentDataElementList) {
        DbElementCache elementCache = ocmDatabase.elementCacheDao().fetchElementCache(element.getSlug());
        elementCaches.put(element.getElementUrl(), elementCache);
      }
      dbSectionContentData.setElementsCache(elementCaches);
      
      if (dbSectionContentData != null) {
        emitter.onNext(dbSectionContentData);
        emitter.onComplete();
      } else {
        emitter.onError(new ApiSectionNotFoundException());
      }
    });
  }

  @Override public void putSection(ApiSectionContentData apiSectionContentData, String key) {
    DbSectionContentData dbSectionContentData = DbMappersKt.toDbSectionContentData(apiSectionContentData, key);
    ocmDatabase.sectionDao().insertSectionContentData(dbSectionContentData);

    ApiContentItem sectionContentItem = apiSectionContentData.getContent();
    if(sectionContentItem != null) {
      for (ApiElement element : sectionContentItem.getElements()) {
      DbSectionElementJoin dbSectionElementJoin = new DbSectionElementJoin(sectionContentItem.getSlug(), element.getSlug());
      ocmDatabase.sectionDao().insertSectionElement((dbSectionElementJoin));
      }
    }

    Map<String, ApiElementCache> elementsCache = apiSectionContentData.getElementsCache();
    if(elementsCache != null) {
      Iterator<Map.Entry<String, ApiElementCache>> iterator = elementsCache.entrySet().iterator();
      while (iterator.hasNext()) {
        Map.Entry<String, ApiElementCache> next = iterator.next();
        ApiElementData apiElementData = new ApiElementData(next.getValue());
        putDetail(apiElementData, next.getKey());
      }
    }
  }

  @Override public boolean isSectionCached(String elementUrl) {
    int sectionDataCount = ocmDatabase.sectionDao().hasSectionContentData(elementUrl);
    return (sectionDataCount == 1);
  }

  @Override public boolean isSectionExpired(String elementUrl) {
    return false;
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

  @Override public void putDetail(ApiElementData apiElementData, String key) {
    DbElementCache elementCacheData =
        DbMappersKt.toDbElementCache(apiElementData.getElement(), key);
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
    if (data) {
      ocmDatabase.deleteAll();
    }
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

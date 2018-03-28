package com.gigigo.orchextra.core.data.rxRepository.rxDatasource;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import com.gigigo.orchextra.core.data.api.dto.article.ApiArticleElement;
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentData;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElement;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementData;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementSectionView;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContent;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentData;
import com.gigigo.orchextra.core.data.api.dto.versioning.ApiVersionData;
import com.gigigo.orchextra.core.data.api.dto.video.ApiVideoData;
import com.gigigo.orchextra.core.data.api.mappers.contentdata.ApiContentDataResponseMapper;
import com.gigigo.orchextra.core.data.api.mappers.elements.ApiElementDataMapper;
import com.gigigo.orchextra.core.data.api.mappers.menus.ApiMenuContentListResponseMapper;
import com.gigigo.orchextra.core.data.api.mappers.version.ApiVersionMapper;
import com.gigigo.orchextra.core.data.api.services.OcmApiService;
import com.gigigo.orchextra.core.data.rxCache.OcmCache;
import com.gigigo.orchextra.core.data.rxCache.imageCache.ImageData;
import com.gigigo.orchextra.core.data.rxCache.imageCache.ImagesService;
import com.gigigo.orchextra.core.data.rxCache.imageCache.OcmImageCache;
import com.gigigo.orchextra.core.data.rxRepository.DbMappersKt;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.elements.ElementData;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import com.gigigo.orchextra.core.domain.entities.version.VersionData;
import com.gigigo.orchextra.core.receiver.WifiReceiver;
import com.gigigo.orchextra.core.sdk.di.injector.Injector;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocmsdk.BuildConfig;
import gigigo.com.vimeolibs.VimeoBuilder;
import gigigo.com.vimeolibs.VimeoCallback;
import gigigo.com.vimeolibs.VimeoInfo;
import gigigo.com.vimeolibs.VimeoManager;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.Iterator;
import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;

/**
 * Created by francisco.hernandez on 23/5/17.
 */

@Singleton public class OcmCloudDataStore implements OcmDataStore {

  private static final int MAX_ARTICLES = Integer.MAX_VALUE;
  private final OcmApiService ocmApiService;
  private final OcmCache ocmCache;
  private final OcmImageCache ocmImageCache;

  private final ApiVersionMapper apiVersionMapper;
  private final ApiMenuContentListResponseMapper apiMenuContentListResponseMapper;
  private final ApiContentDataResponseMapper apiContentDataResponseMapper;
  private final ApiElementDataMapper apiElementDataMapper;

  private Integer withThumbnails = null; //For default, thumbnails are enabled

  @Inject public OcmCloudDataStore(@NonNull OcmApiService ocmApiService, @NonNull OcmCache ocmCache,
      @NonNull OcmImageCache ocmImageCache, ApiVersionMapper apiVersionMapper,
      ApiMenuContentListResponseMapper apiMenuContentListResponseMapper,
      ApiContentDataResponseMapper apiContentDataResponseMapper,
      ApiElementDataMapper apiElementDataMapper) {
    this.ocmApiService = ocmApiService;
    this.ocmCache = ocmCache;
    this.ocmImageCache = ocmImageCache;
    this.apiVersionMapper = apiVersionMapper;
    this.apiMenuContentListResponseMapper = apiMenuContentListResponseMapper;
    this.apiContentDataResponseMapper = apiContentDataResponseMapper;
    this.apiElementDataMapper = apiElementDataMapper;

    Injector injector = OCManager.getInjector();
    if (injector != null) {
      boolean thumbnailEnabled = injector.provideOcmStyleUi().isThumbnailEnabled();
      this.withThumbnails = thumbnailEnabled ? 0 : null;
    }
  }

  @Override public Observable<VersionData> getVersion() {
    return ocmApiService.getVersionDataRx()
        .map(apiVersionResponse -> new ApiVersionData(apiVersionResponse.getData()))
        .filter(apiVersionData -> apiVersionData != null)
        .doOnNext(ocmCache::putVersion)
        .map(apiVersionMapper::externalClassToModel);
  }

  @Override public Observable<MenuContentData> getMenuEntity() {

    final long time = System.currentTimeMillis();

    return ocmApiService.getMenuDataRx()
        .map(dataResponse -> dataResponse.getResult())
        .doOnNext(ocmCache::putMenus)
        .doOnNext(apiMenuContentData -> addSectionsToCache(apiMenuContentData))
        .doOnNext(apiMenuContentData -> {
          apiMenuContentData.setFromCloud(true);
        })
        .map(apiMenuContentListResponseMapper::externalClassToModel);
  }

  @Override public Observable<ContentData> getSectionEntity(String contentUrl,
      final int numberOfElementsToDownload) {

    final long time = System.currentTimeMillis();

    return ocmApiService.getSectionDataRx(contentUrl, withThumbnails)
        .map(dataResponse -> dataResponse.getResult())
        .doOnNext(apiSectionContentData -> apiSectionContentData.setKey(contentUrl))
        .doOnNext(ocmCache::putSection)
        .doOnNext(apiSectionContentData -> addSectionsImagesToCache(apiSectionContentData,
            numberOfElementsToDownload))
        .doOnNext(apiSectionContentData -> {
          apiSectionContentData.setFromCloud(true);
          Log.v("TT - SectionEntity", (System.currentTimeMillis() - time) / 1000 + "");
        })
        .map(apiContentDataResponseMapper::externalClassToModel);
  }

  private void addSectionsToCache(ApiMenuContentData apiMenuContentData) {
    Iterator<ApiMenuContent> menuContentIterator =
        apiMenuContentData.getMenuContentList().iterator();
    while (menuContentIterator.hasNext()) {
      Iterator<ApiElement> elementIterator = menuContentIterator.next().getElements().iterator();
      while (elementIterator.hasNext()) {
        ApiElement apiElement = elementIterator.next();
        if (apiMenuContentData.getElementsCache().containsKey(apiElement.getElementUrl())) {
          ApiSectionContentData contentData = new ApiSectionContentData();
          contentData.setKey(apiElement.getElementUrl());
          ocmCache.putSection(contentData);
        }
      }
    }
  }

  private void addSectionsImagesToCache(ApiSectionContentData apiSectionContentData,
      int imagestodownload) {
    Iterator<ApiElement> iterator = apiSectionContentData.getContent().getElements().iterator();
    int i = 0;
    while (iterator.hasNext() && i < MAX_ARTICLES) {
      ApiElement apiElement = iterator.next();
      addImageToQueue(apiElement.getSectionView());
      if (apiSectionContentData.getElementsCache().containsKey(apiElement.getElementUrl())) {
        ApiElementData apiElementData = new ApiElementData(
            apiSectionContentData.getElementsCache().get(apiElement.getElementUrl()));
        if (i < imagestodownload) addImageToQueue(apiElementData);
        ocmCache.putDetail(apiElementData);
      }
      i++;
    }

    Intent intent = new Intent(ocmCache.getContext(), ImagesService.class);
    WifiReceiver.intentService = intent;
    ocmCache.getContext().startService(intent);
  }

  private void addImageToQueue(ApiElementSectionView apiElementSectionView) {
    if (apiElementSectionView != null) {
      if (apiElementSectionView.getImageUrl() != null) {
        ocmImageCache.add(new ImageData(apiElementSectionView.getImageUrl(), 9));
      }
    }
  }

  private void addImageToQueue(ApiElementData apiElementData) {

    if (apiElementData.getElement() != null) {
      //Preview
      if (apiElementData.getElement().getPreview() != null) {
        ocmImageCache.add(new ImageData(apiElementData.getElement().getPreview().getImageUrl(), 0));
      }
      //Render
      if (apiElementData.getElement().getRender() != null
          && apiElementData.getElement().getRender().getElements() != null) {
        Iterator<ApiArticleElement> elementsIterator =
            apiElementData.getElement().getRender().getElements().iterator();
        while (elementsIterator.hasNext()) {
          ApiArticleElement element = elementsIterator.next();
          if (element.getRender() != null && element.getRender().getImageUrl() != null) {
            ocmImageCache.add(new ImageData(element.getRender().getImageUrl(), 0));
          }
        }
      }
    }
  }

  @Override public Observable<ContentData> searchByText(String section) {
    return ocmApiService.searchRx(section)
        .map(dataResponse -> dataResponse.getResult())
        .map(apiContentDataResponseMapper::externalClassToModel);
  }

  @Override public Observable<ElementData> getElementById(String slug) {
    return ocmApiService.getElementByIdRx(slug, withThumbnails)
        .map(dataResponse -> dataResponse.getResult())
        .doOnNext(ocmCache::putDetail)
        .map(apiElementDataMapper::externalClassToModel);
  }

  @Override public Observable<VimeoInfo> getVideoById(Context context, String videoId,
      boolean isWifiConnection, boolean isFastConnection) {
    Observable<ApiVideoData> videoObservable =
        Observable.create(new ObservableOnSubscribe<ApiVideoData>() {
          @Override public void subscribe(ObservableEmitter<ApiVideoData> emitter)
              throws Exception {
            VimeoBuilder builder = new VimeoBuilder(BuildConfig.VIMEO_ACCESS_TOKEN);
            VimeoManager videoManager = new VimeoManager(builder);

            videoManager.getVideoVimeoInfo(context, videoId, isWifiConnection, isFastConnection,
                new VimeoCallback() {
                  @Override public void onSuccess(VimeoInfo vimeoInfo) {
                    ApiVideoData videoData = new ApiVideoData(vimeoInfo);
                    ocmCache.putVideo(videoData);
                    emitter.onNext(videoData);
                  }

                  @Override public void onError(Exception exception) {
                    emitter.onError(exception);
                  }
                });
          }
        });

    videoObservable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe();

    return videoObservable.map(apiVideoData -> DbMappersKt.toVimeoInfo(apiVideoData));
  }

  @Override public boolean isFromCloud() {
    return true;
  }
}

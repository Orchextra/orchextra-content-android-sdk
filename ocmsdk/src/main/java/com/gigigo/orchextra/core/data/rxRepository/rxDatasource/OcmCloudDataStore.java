package com.gigigo.orchextra.core.data.rxRepository.rxDatasource;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.gigigo.orchextra.core.data.api.dto.article.ApiArticleElement;
import com.gigigo.orchextra.core.data.api.dto.base.BaseApiResponse;
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentData;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElement;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementData;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementSectionView;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContent;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentData;
import com.gigigo.orchextra.core.data.api.services.OcmApiService;
import com.gigigo.orchextra.core.data.mappers.DbMappersKt;
import com.gigigo.orchextra.core.data.rxCache.OcmCache;
import com.gigigo.orchextra.core.data.rxCache.imageCache.ImageData;
import com.gigigo.orchextra.core.data.rxCache.imageCache.ImagesService;
import com.gigigo.orchextra.core.data.rxCache.imageCache.OcmImageCache;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.elements.ElementData;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
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
import org.jetbrains.annotations.NotNull;

@Singleton public class OcmCloudDataStore implements OcmDataStore {

  private static final int MAX_ARTICLES = Integer.MAX_VALUE;
  private final OcmApiService ocmApiService;
  private final OcmCache ocmCache;
  private final OcmImageCache ocmImageCache;

  private Integer withThumbnails = null; //For default, thumbnails are enabled

  @Inject public OcmCloudDataStore(@NonNull OcmApiService ocmApiService, @NonNull OcmCache ocmCache,
      @NonNull OcmImageCache ocmImageCache) {
    this.ocmApiService = ocmApiService;
    this.ocmCache = ocmCache;
    this.ocmImageCache = ocmImageCache;

    Injector injector = OCManager.getInjector();
    if (injector != null) {
      boolean thumbnailEnabled = injector.provideOcmStyleUi().isThumbnailEnabled();
      this.withThumbnails = thumbnailEnabled ? 0 : null;
    }
  }

  @Override public Observable<MenuContentData> getMenus() {
    return ocmApiService.getMenuDataRx()
        .map(dataResponse -> dataResponse.getResult())
        .doOnNext(ocmCache::putMenus)
        //.doOnNext(apiMenuContentData -> saveSections(apiMenuContentData))
        .map(apiMenuContentData -> DbMappersKt.toMenuContentData(apiMenuContentData));
  }

  @Override public Observable<ContentData> getSection(String contentUrl,
      final int numberOfElementsToDownload) {
    return ocmApiService.getSectionDataRx(contentUrl, withThumbnails)
        .map(BaseApiResponse::getResult)
        .doOnNext(apiSectionContentData -> ocmCache.putSection(apiSectionContentData, contentUrl))
        .doOnNext(apiSectionContentData -> addSectionsImagesToCache(apiSectionContentData,
            numberOfElementsToDownload))
        .map(DbMappersKt::toContentData);
  }

  private void saveSections(ApiMenuContentData apiMenuContentData) {
    for (ApiMenuContent apiMenuContent : apiMenuContentData.getMenuContentList()) {
      for (ApiElement apiElement : apiMenuContent.getElements()) {
        ApiSectionContentData contentData = new ApiSectionContentData();
        ocmCache.putSection(contentData, apiElement.getElementUrl());
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

        ocmCache.putDetail(apiElementData, apiElement.getElementUrl());
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
        .map(apiSectionContentData -> DbMappersKt.toContentData(apiSectionContentData));
  }

  @Override public Observable<ElementData> getElementById(String slug) {
    return ocmApiService.getElementByIdRx(slug, withThumbnails)
        .map(dataResponse -> dataResponse.getResult())
        .doOnNext(apiElementData -> {
          ocmCache.putDetail(apiElementData, apiElementData.getElement().getSlug());
        })
        .map(apiElementData -> {
          ElementData elementData = DbMappersKt.toElementData(apiElementData);
          return elementData;
        });
  }

  @Override public Observable<VimeoInfo> getVideoById(Context context, String videoId,
      boolean isWifiConnection, boolean isFastConnection) {
    Observable<VimeoInfo> videoObservable =
        Observable.create(new ObservableOnSubscribe<VimeoInfo>() {
          @Override public void subscribe(ObservableEmitter<VimeoInfo> emitter) throws Exception {
            VimeoBuilder builder = new VimeoBuilder(BuildConfig.VIMEO_ACCESS_TOKEN);
            VimeoManager videoManager = new VimeoManager(builder);

            videoManager.getVideoVimeoInfo(context, videoId, isWifiConnection, isFastConnection,
                new VimeoCallback() {
                  @Override public void onError(@NotNull Throwable e) {
                    emitter.onError(e);
                  }

                  @Override public void onSuccess(VimeoInfo vimeoInfo) {
                    ocmCache.putVideo(vimeoInfo);
                    emitter.onNext(vimeoInfo);
                  }
                });
          }
        });

    videoObservable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe();

    return videoObservable;
  }

  @Override public boolean isFromCloud() {
    return true;
  }
}

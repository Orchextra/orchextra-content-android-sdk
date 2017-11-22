package com.gigigo.orchextra.core.data.rxRepository.rxDatasource;

import android.content.Intent;
import android.support.annotation.NonNull;
import com.gigigo.orchextra.core.data.api.dto.article.ApiArticleElement;
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentData;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElement;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementData;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementSectionView;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContent;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentData;
import com.gigigo.orchextra.core.data.api.services.OcmApiService;
import com.gigigo.orchextra.core.data.rxCache.OcmCache;
import com.gigigo.orchextra.core.data.rxCache.imageCache.ImageData;
import com.gigigo.orchextra.core.data.rxCache.imageCache.ImagesService;
import com.gigigo.orchextra.core.data.rxCache.imageCache.OcmImageCache;
import com.gigigo.orchextra.core.receiver.WifiReceiver;
import io.reactivex.Observable;
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

  @Inject public OcmCloudDataStore(@NonNull OcmApiService ocmApiService, @NonNull OcmCache ocmCache,
      @NonNull OcmImageCache ocmImageCache) {
    this.ocmApiService = ocmApiService;
    this.ocmCache = ocmCache;
    this.ocmImageCache = ocmImageCache;
  }

  @Override public Observable<ApiMenuContentData> getMenuEntity() {
    return ocmApiService.getMenuDataRx()
        .map(dataResponse -> dataResponse.getResult())
        .doOnNext(ocmCache::putMenus)
        .doOnNext(apiMenuContentData -> addSectionsToCache(apiMenuContentData));
  }

  @Override public Observable<ApiSectionContentData> getSectionEntity(String contentUrl,
      final int numberOfElementsToDownload) {
    return ocmApiService.getSectionDataRx(contentUrl)
        .map(dataResponse -> dataResponse.getResult())
        .doOnNext(apiSectionContentData -> apiSectionContentData.setKey(contentUrl))
        .doOnNext(ocmCache::putSection)
        .doOnNext(apiSectionContentData -> {
          addSectionsImagesToCache(apiSectionContentData, numberOfElementsToDownload);
        });
  }

  private void addSectionsToCache(ApiMenuContentData apiMenuContentData) {
    Iterator<ApiMenuContent> menuContentIterator = apiMenuContentData.getMenuContentList().iterator();
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

  @Override public Observable<ApiSectionContentData> searchByText(String section) {
    return ocmApiService.searchRx(section).map(dataResponse -> dataResponse.getResult());
  }

  @Override public Observable<ApiElementData> getElementById(String slug) {
    return ocmApiService.getElementByIdRx(slug)
        .map(dataResponse -> dataResponse.getResult())
        .doOnNext(ocmCache::putDetail);
  }
}

package com.gigigo.orchextra.core.data.rxRepository.rxDatasource;

import android.support.annotation.NonNull;
import android.util.Log;
import com.gigigo.orchextra.core.data.api.dto.article.ApiArticleElement;
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentData;
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentDataResponse;
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCache;
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementDataResponse;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElement;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementData;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentData;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentDataResponse;
import com.gigigo.orchextra.core.data.api.services.OcmApiService;
import com.gigigo.orchextra.core.data.rxCache.OcmCache;
import com.gigigo.orchextra.core.data.rxCache.imageCache.OcmImageCache;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import io.reactivex.Observable;

import io.reactivex.functions.Consumer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;

/**
 * Created by francisco.hernandez on 23/5/17.
 */

@Singleton public class OcmCloudDataStore implements OcmDataStore {

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
        .doOnNext(ocmCache::putMenus);
  }

  @Override public Observable<ApiSectionContentData> getSectionEntity(String elementUrl) {
    return ocmApiService.getSectionDataRx(elementUrl)
        .map(dataResponse -> dataResponse.getResult())
        .doOnNext(apiSectionContentData -> apiSectionContentData.setKey(elementUrl))
        .doOnNext(ocmCache::putSection)
        .doOnNext(apiSectionContentData -> {
          Iterator<ApiElement> iterator =
              apiSectionContentData.getContent().getElements().iterator();
          while (iterator.hasNext()) {
            ApiElement apiElement = iterator.next();
            if (apiSectionContentData.getElementsCache().containsKey(apiElement.getElementUrl())) {
              ApiElementData apiElementData = new ApiElementData(
                  apiSectionContentData.getElementsCache().get(apiElement.getElementUrl()));
              addImageToQueue(apiElementData);
              ocmCache.putDetail(apiElementData);
            }
          }

          ocmImageCache.start();
        });
  }

  private void addImageToQueue(ApiElementData apiElementData) {

    if (apiElementData.getElement() != null) {
      //Preview
      if (apiElementData.getElement().getPreview() != null) {
        ocmImageCache.add(apiElementData.getElement().getPreview().getImageUrl());
      }
      //Render
      if (apiElementData.getElement().getRender() != null
          && apiElementData.getElement().getRender().getElements() != null) {
        Iterator<ApiArticleElement> elementsIterator =
            apiElementData.getElement().getRender().getElements().iterator();
        while (elementsIterator.hasNext()) {
          ApiArticleElement element = elementsIterator.next();
          if (element.getRender() != null && element.getRender().getImageUrl() != null) {
            ocmImageCache.add(element.getRender().getImageUrl());
          }
        }
      }
    }
  }

  @Override public Observable<ApiSectionContentData> searchByText(String section) {
    return ocmApiService.searchRx(section).map(dataResponse -> dataResponse.getResult());
  }

  @Override public Observable<ApiElementData> getElementById(String section) {
    return ocmApiService.getElementByIdRx(section)
        .map(dataResponse -> dataResponse.getResult())
        .doOnNext(ocmCache::putDetail);
  }
}

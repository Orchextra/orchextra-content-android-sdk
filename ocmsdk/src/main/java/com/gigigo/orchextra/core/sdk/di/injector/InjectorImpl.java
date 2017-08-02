/*
 * Created by Orchextra
 *
 * Copyright (C) 2016 Gigigo Mobile Services SL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gigigo.orchextra.core.sdk.di.injector;

import com.gigigo.orchextra.core.data.rxCache.imageCache.ImagesService;
import com.gigigo.orchextra.core.receiver.DaggerImagesServiceComponent;
import com.gigigo.orchextra.core.receiver.ImagesServiceComponent;
import com.gigigo.orchextra.core.sdk.OcmStyleUi;
import com.gigigo.orchextra.core.sdk.di.components.OcmComponent;
import com.gigigo.orchextra.core.sdk.model.detail.DetailActivity;
import com.gigigo.orchextra.core.sdk.model.detail.DetailActivityComponent;
import com.gigigo.orchextra.core.sdk.model.detail.layouts.DaggerDetailContentDataComponent;
import com.gigigo.orchextra.core.sdk.model.detail.layouts.DetailContentDataComponent;
import com.gigigo.orchextra.core.sdk.model.detail.layouts.DetailParentContentData;
import com.gigigo.orchextra.core.sdk.model.grid.ContentGridLayoutViewComponent;
import com.gigigo.orchextra.core.sdk.model.searcher.SearcherLayoutView;
import com.gigigo.orchextra.core.sdk.model.searcher.SearcherLayoutViewComponent;
import com.gigigo.orchextra.core.sdk.model.detail.DaggerDetailActivityComponent;
import com.gigigo.orchextra.core.sdk.model.grid.ContentGridLayoutView;
import com.gigigo.orchextra.core.sdk.model.grid.DaggerContentGridLayoutViewComponent;
import com.gigigo.orchextra.core.sdk.model.searcher.DaggerSearcherLayoutViewComponent;
import com.gigigo.orchextra.core.receiver.WifiReceiver;

public class InjectorImpl implements Injector {

  private final OcmComponent ocmComponent;

  public InjectorImpl(OcmComponent ocmComponent) {
    this.ocmComponent = ocmComponent;
  }

  @Override public void injectContentGridLayoutView(
      ContentGridLayoutView contentGridLayoutView) {
    ContentGridLayoutViewComponent contentGridLayoutViewComponent =
        DaggerContentGridLayoutViewComponent.builder().ocmComponent(ocmComponent).build();

    contentGridLayoutViewComponent.injectContentGridLayoutView(contentGridLayoutView);
  }

  @Override
  public void injectDetailActivity(DetailActivity detailActivity) {
    DetailActivityComponent detailActivityComponent =
        DaggerDetailActivityComponent.builder().ocmComponent(ocmComponent).build();

    detailActivityComponent.injectDetailActivity(detailActivity);
  }

  @Override public void injectSearcherLayoutView(SearcherLayoutView searcherLayoutView) {
    SearcherLayoutViewComponent searcherLayoutViewComponent =
        DaggerSearcherLayoutViewComponent.builder().ocmComponent(ocmComponent).build();

    searcherLayoutViewComponent.injectSearcherLayoutView(searcherLayoutView);
  }

  @Override
  public OcmStyleUi provideOcmStyleUi() {
    return ocmComponent.provideOcmStyleUi();
  }

  @Override public void injectDetailContentData(DetailParentContentData detailParentContentData) {
    DetailContentDataComponent detailContentDataComponent =
        DaggerDetailContentDataComponent.builder().ocmComponent(ocmComponent).build();

    detailContentDataComponent.injectDetailContentData(detailParentContentData);
  }

  @Override public void injectImagesService(ImagesService imagesService) {
    ImagesServiceComponent
        imagesServiceComponent = DaggerImagesServiceComponent.builder().ocmComponent(ocmComponent).build();
    imagesServiceComponent.injectImagesService(imagesService);
  }
}

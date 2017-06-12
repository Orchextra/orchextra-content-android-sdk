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

import com.gigigo.orchextra.core.sdk.model.detail.layouts.DetailParentContentData;
import com.gigigo.orchextra.core.sdk.model.searcher.SearcherLayoutView;
import com.gigigo.orchextra.core.sdk.OcmStyleUi;
import com.gigigo.orchextra.core.sdk.model.detail.DetailActivity;
import com.gigigo.orchextra.core.sdk.model.grid.ContentGridLayoutView;
import com.gigigo.orchextra.core.receiver.WifiReceiver;

public interface Injector {

  void injectContentGridLayoutView(ContentGridLayoutView contentGridLayoutView);

  void injectDetailActivity(DetailActivity detailActivity);

  void injectSearcherLayoutView(SearcherLayoutView searcherLayoutView);

  void injectDetailContentData(DetailParentContentData detailParentContentData);

  void injectWifiBroadcastReceiver(WifiReceiver wifiReceiver);

  OcmStyleUi provideOcmStyleUi();
}

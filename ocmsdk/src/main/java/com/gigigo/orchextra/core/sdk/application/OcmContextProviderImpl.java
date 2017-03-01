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

package com.gigigo.orchextra.core.sdk.application;

import android.app.Activity;
import android.content.Context;

public class OcmContextProviderImpl implements OcmContextProvider {

  private final Context context;
  private OcmSdkLifecycle ocmSdkLifecycle;

  public OcmContextProviderImpl(Context context) {
    this.context = context;
  }

  @Override public void setOcmActivityLifecycle(OcmSdkLifecycle ocmSdkLifecycle) {
    this.ocmSdkLifecycle = ocmSdkLifecycle;
  }

  //region context provider interface
  @Override public Activity getCurrentActivity() {
    if (ocmSdkLifecycle == null) {
      return null;
    }
    return ocmSdkLifecycle.getCurrentActivity();
  }

  @Override public boolean isActivityContextAvailable() {
    if (ocmSdkLifecycle == null) {
      return false;
    }
    //this implementation gives context of paused and stop activities
    return ocmSdkLifecycle.isActivityContextAvailable();
  }

  @Override public Context getApplicationContext() {
    return context;
  }

  @Override public boolean isApplicationContextAvailable() {
    return (context != null);
  }

  //endregion
}

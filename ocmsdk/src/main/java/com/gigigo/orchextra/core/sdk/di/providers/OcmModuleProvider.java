package com.gigigo.orchextra.core.sdk.di.providers;

import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.core.sdk.OcmStyleUi;
import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import com.gigigo.orchextra.core.sdk.application.OcmSdkLifecycle;
import com.gigigo.orchextra.core.sdk.utils.LeanplumSdk;
import com.gigigo.ui.imageloader.ImageLoader;

public interface OcmModuleProvider extends ControllerModuleProvider, PresentationModuleProvider {

  OcmContextProvider provideOcmContextProvider();

  OcmSdkLifecycle provideOcmSdkLifecycle();

  ImageLoader provideImageLoader();

  Authoritation provideAuthoritation();

  OcmStyleUi provideOcmStyleUi();

  LeanplumSdk provideLeanplumSdk();
}

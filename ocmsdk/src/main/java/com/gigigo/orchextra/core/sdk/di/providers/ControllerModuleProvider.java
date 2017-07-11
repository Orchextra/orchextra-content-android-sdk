package com.gigigo.orchextra.core.sdk.di.providers;

import com.gigigo.orchextra.core.data.rxCache.imageCache.OcmImageCache;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.rxInteractor.GetMenus;

public interface ControllerModuleProvider extends DomainModuleProvider, InteractorModuleProvider {

  OcmController provideOcmController();

  OcmImageCache provideOcmImageCache();
}

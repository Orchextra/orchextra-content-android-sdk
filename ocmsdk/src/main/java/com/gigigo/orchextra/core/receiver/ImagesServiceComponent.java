package com.gigigo.orchextra.core.receiver;

import com.gigigo.orchextra.core.data.rxCache.imageCache.ImagesService;
import com.gigigo.orchextra.core.sdk.di.components.OcmComponent;
import com.gigigo.orchextra.core.sdk.di.providers.OcmModuleProvider;
import com.gigigo.orchextra.core.sdk.di.scopes.PerSection;
import orchextra.dagger.Component;

@PerSection @Component(dependencies = OcmComponent.class, modules = ImagesServiceModule.class)
public interface ImagesServiceComponent extends OcmModuleProvider {
  void injectImagesService(ImagesService imagesService);
}

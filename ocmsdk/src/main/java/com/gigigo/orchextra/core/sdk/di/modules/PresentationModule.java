package com.gigigo.orchextra.core.sdk.di.modules;

import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.sdk.di.qualifiers.UiThread;
import com.gigigo.orchextra.core.controller.OcmViewGenerator;
import com.gigigo.orchextra.core.controller.model.detail.DetailElementsViewPresenter;

import orchextra.dagger.Module;
import orchextra.dagger.Provides;
import orchextra.javax.inject.Singleton;

@Module public class PresentationModule {

  @Singleton @Provides DetailElementsViewPresenter provideDetailElementsViewPresenter(
      OcmController ocmController,
      OcmViewGenerator ocmViewGenerator) {
    return new DetailElementsViewPresenter(ocmController, ocmViewGenerator);
  }
}

package com.gigigo.orchextra.core.sdk.di.modules;

import com.gigigo.orchextra.core.controller.OcmViewGenerator;
import com.gigigo.orchextra.core.controller.model.detail.DetailElementsViewPresenter;
import com.gigigo.orchextra.core.domain.OcmController;

import dagger.Module;
import dagger.Provides;

@Module
public class PresentationModule {

    @Provides
    DetailElementsViewPresenter provideDetailElementsViewPresenter(
            OcmController ocmController,
            OcmViewGenerator ocmViewGenerator) {
        return new DetailElementsViewPresenter(ocmController, ocmViewGenerator);
    }
}

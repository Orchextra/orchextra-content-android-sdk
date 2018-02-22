package com.gigigo.orchextra.core.sdk.model.grid;

import com.gigigo.orchextra.core.controller.model.home.grid.ContentViewPresenter;
import com.gigigo.orchextra.core.domain.OcmController;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;

@Module public class ContentGridLayoutViewModule {

  @Provides ContentViewPresenter provideContentViewPresenter(OcmController ocmController) {
    return new ContentViewPresenter(ocmController);
  }
}

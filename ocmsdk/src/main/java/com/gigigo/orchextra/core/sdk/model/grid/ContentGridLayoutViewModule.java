package com.gigigo.orchextra.core.sdk.model.grid;

import com.gigigo.orchextra.core.controller.model.grid.ContentViewPresenter;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;

@Module public class ContentGridLayoutViewModule {

  @Provides ContentViewPresenter provideContentViewPresenter(
      OcmController ocmController, Authoritation authoritation) {
    return new ContentViewPresenter(ocmController, authoritation);
  }
}

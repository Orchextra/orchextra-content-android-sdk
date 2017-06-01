package com.gigigo.orchextra.core.sdk.model.grid;

import com.gigigo.orchextra.core.controller.model.grid.ContentViewPresenter;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import com.gigigo.orchextra.core.sdk.di.scopes.PerSection;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;

@Module public class ContentGridLayoutViewModule {

  @Provides @PerSection ContentViewPresenter provideContentViewPresenter(
      OcmController ocmController, Authoritation authoritation) {
    return new ContentViewPresenter(ocmController, authoritation);
  }
}

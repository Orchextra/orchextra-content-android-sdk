package com.gigigo.orchextra.core.sdk.model.grid;

import com.gigigo.interactorexecutor.base.invoker.InteractorInvoker;
import com.gigigo.interactorexecutor.base.viewinjector.GenericViewInjector;
import com.gigigo.orchextra.core.controller.model.grid.ContentViewPresenter;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.data.SectionNetworkDataSource;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.core.domain.interactor.home.GetSectionDataInteractor;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import com.gigigo.orchextra.core.sdk.di.scopes.PerSection;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;

@Module public class ContentGridLayoutViewModule {

  @Provides @PerSection GetSectionDataInteractor provideGetHomeDataInteractor(
      ConnectionUtils connectionUtils, SectionNetworkDataSource homeNetworkDataSource,
      OcmController ocmController) {
    return new GetSectionDataInteractor(connectionUtils, homeNetworkDataSource, ocmController);
  }

  @Provides @PerSection ContentViewPresenter provideContentViewPresenter(
      GenericViewInjector genericViewInjector, OcmController ocmController, InteractorInvoker interactorInvoker,
      GetSectionDataInteractor getHomeDataInteractor, Authoritation authoritation) {
    return new ContentViewPresenter(genericViewInjector, ocmController, interactorInvoker, getHomeDataInteractor,
        authoritation);
  }
}

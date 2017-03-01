package com.gigigo.orchextra.core.sdk.model.searcher;

import com.gigigo.interactorexecutor.base.invoker.InteractorInvoker;
import com.gigigo.interactorexecutor.base.viewinjector.GenericViewInjector;
import com.gigigo.orchextra.core.controller.model.searcher.SearcherLayoutPresenter;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.data.SearcherNetworkDataSource;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.core.domain.interactor.searcher.SearchTextInteractor;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import com.gigigo.orchextra.core.sdk.di.scopes.PerSection;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;

@Module public class SearcherLayoutViewModule {

  @PerSection @Provides SearchTextInteractor provideSearchTextInteractor(
      ConnectionUtils connectionUtils, SearcherNetworkDataSource searcherNetworkDataSource) {
    return new SearchTextInteractor(connectionUtils, searcherNetworkDataSource);
  }

  @PerSection @Provides SearcherLayoutPresenter provideSearcherLayoutPresenter(
      GenericViewInjector genericViewInjector, OcmController ocmController,
      InteractorInvoker interactorInvoker, SearchTextInteractor searchTextInteractor,
      Authoritation authoritation) {
    return new SearcherLayoutPresenter(genericViewInjector, ocmController, interactorInvoker,
        searchTextInteractor, authoritation);
  }
}

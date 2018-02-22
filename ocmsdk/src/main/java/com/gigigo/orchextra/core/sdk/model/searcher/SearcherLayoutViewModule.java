package com.gigigo.orchextra.core.sdk.model.searcher;

import com.gigigo.orchextra.core.controller.model.searcher.SearcherLayoutPresenter;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.sdk.di.scopes.PerSection;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;

@Module public class SearcherLayoutViewModule {

  @PerSection @Provides SearcherLayoutPresenter provideSearcherLayoutPresenter(
      OcmController ocmController) {
    return new SearcherLayoutPresenter(ocmController);
  }
}

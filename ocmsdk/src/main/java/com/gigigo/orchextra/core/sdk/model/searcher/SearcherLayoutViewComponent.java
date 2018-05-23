package com.gigigo.orchextra.core.sdk.model.searcher;

import com.gigigo.orchextra.core.sdk.di.components.OcmComponent;
import com.gigigo.orchextra.core.sdk.di.providers.OcmModuleProvider;
import com.gigigo.orchextra.core.sdk.di.scopes.PerSection;
import orchextra.dagger.Component;

@PerSection @Component(dependencies = OcmComponent.class, modules = SearcherLayoutViewModule.class)
public interface SearcherLayoutViewComponent extends OcmModuleProvider {
  void injectSearcherLayoutView(SearcherLayoutView searcherLayoutView);
}

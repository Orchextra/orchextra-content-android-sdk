package com.gigigo.orchextra.core.sdk.model.grid;

import com.gigigo.orchextra.core.controller.model.grid.ContentViewPresenter;
import com.gigigo.orchextra.core.domain.interactor.home.GetSectionDataInteractor;
import com.gigigo.orchextra.core.sdk.di.components.OcmComponent;
import com.gigigo.orchextra.core.sdk.di.providers.OcmModuleProvider;
import com.gigigo.orchextra.core.sdk.di.scopes.PerSection;
import orchextra.dagger.Component;

@PerSection
@Component(dependencies = OcmComponent.class, modules = ContentGridLayoutViewModule.class)
public interface ContentGridLayoutViewComponent extends OcmModuleProvider {
  void injectContentGridLayoutView(ContentGridLayoutView contentGridLayoutView);

  GetSectionDataInteractor provideGetHomeDataInteractor();

  ContentViewPresenter provideContentViewPresenter();
}

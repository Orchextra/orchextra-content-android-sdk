package com.gigigo.orchextra.core.sdk.model.grid;

import com.gigigo.orchextra.core.controller.model.home.grid.ContentViewPresenter;
import com.gigigo.orchextra.core.sdk.di.components.OcmComponent;
import com.gigigo.orchextra.core.sdk.di.providers.OcmModuleProvider;
import com.gigigo.orchextra.core.sdk.di.scopes.PerSection;

import dagger.Component;

@PerSection
@Component(dependencies = OcmComponent.class, modules = ContentGridLayoutViewModule.class)
public interface ContentGridLayoutViewComponent extends OcmModuleProvider {
    void injectContentGridLayoutView(ContentGridLayoutView contentGridLayoutView);

    ContentViewPresenter provideContentViewPresenter();
}

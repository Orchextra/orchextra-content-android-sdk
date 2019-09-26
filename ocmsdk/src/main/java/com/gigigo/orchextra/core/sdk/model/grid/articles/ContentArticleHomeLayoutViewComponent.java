package com.gigigo.orchextra.core.sdk.model.grid.articles;

import com.gigigo.orchextra.core.controller.model.home.articles.ContentArticleHomeLayoutViewPresenter;
import com.gigigo.orchextra.core.sdk.di.components.OcmComponent;
import com.gigigo.orchextra.core.sdk.di.providers.OcmModuleProvider;
import com.gigigo.orchextra.core.sdk.di.scopes.PerSection;

import dagger.Component;

@PerSection
@Component(dependencies = OcmComponent.class, modules = ContentArticleHomeLayoutViewModule.class)
public interface ContentArticleHomeLayoutViewComponent extends OcmModuleProvider {

    void injectContentArticleHomeLayoutView(
            ContentArticleHomeLayoutView contentArticleHomeLayoutView);

    ContentArticleHomeLayoutViewPresenter provideContentArticleHomeLayoutViewPresenter();
}

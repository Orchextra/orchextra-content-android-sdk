package com.gigigo.orchextra.core.sdk.model.grid.articles;

import com.gigigo.orchextra.core.controller.model.home.articles.ContentArticleHomeLayoutViewPresenter;
import com.gigigo.orchextra.core.domain.OcmController;

import dagger.Module;
import dagger.Provides;

@Module
public class ContentArticleHomeLayoutViewModule {

    @Provides
    ContentArticleHomeLayoutViewPresenter provideContentArticleHomeLayoutViewPresenter(
            OcmController ocmController) {
        return new ContentArticleHomeLayoutViewPresenter(ocmController);
    }
}

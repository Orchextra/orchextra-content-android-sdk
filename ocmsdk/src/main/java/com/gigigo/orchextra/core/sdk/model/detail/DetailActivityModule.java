package com.gigigo.orchextra.core.sdk.model.detail;

import com.gigigo.orchextra.core.controller.model.detail.DetailPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class DetailActivityModule {

    @Provides
    DetailPresenter provideDetailPresenter() {
        return new DetailPresenter();
    }
}

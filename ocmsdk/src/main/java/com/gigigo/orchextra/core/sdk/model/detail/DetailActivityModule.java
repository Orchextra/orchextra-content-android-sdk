package com.gigigo.orchextra.core.sdk.model.detail;

import com.gigigo.orchextra.core.controller.model.detail.DetailPresenter;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;

@Module public class DetailActivityModule {

  @Provides DetailPresenter provideDetailPresenter() {
    return new DetailPresenter();
  }
}

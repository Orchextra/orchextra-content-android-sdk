package com.gigigo.orchextra.core.sdk.model.detail;

import com.gigigo.interactorexecutor.base.viewinjector.GenericViewInjector;
import com.gigigo.orchextra.core.controller.model.detail.DetailPresenter;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;

@Module public class DetailActivityModule {

  @Provides DetailPresenter provideDetailPresenter(GenericViewInjector genericViewInjector) {
    return new DetailPresenter(genericViewInjector);
  }
}

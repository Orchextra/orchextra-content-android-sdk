package com.gigigo.orchextra.core.sdk.di.modules;

import com.gigigo.interactorexecutor.base.viewinjector.GenericViewInjector;
import com.gigigo.interactorexecutor.base.viewinjector.ThreadViewInjector;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.outputs.MainThreadSpec;
import com.gigigo.orchextra.core.sdk.di.qualifiers.UiThread;
import com.gigigo.orchextra.core.controller.OcmViewGenerator;
import com.gigigo.orchextra.core.controller.model.detail.DetailElementsViewPresenter;

import com.gigigo.threaddecoratedview.views.ThreadSpec;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;
import orchextra.javax.inject.Singleton;

@Module public class PresentationModule {

  @Provides @Singleton @UiThread ThreadSpec provideMainThreadSpec() {
    return new MainThreadSpec();
  }

  //@Provides @Singleton @SameThread ThreadSpec provideSameThreadSpec() {
  //  return new SameThreadSpec();
  //}
  //
  //@Provides @Singleton @BackThread ThreadSpec provideBackThreadSpec() {
  //  return new BackThreadSpec();
  //}

  @Provides @Singleton GenericViewInjector provideGenericViewInjector(
      @UiThread ThreadSpec threadSpec) {
    return new ThreadViewInjector(threadSpec);
  }

  @Singleton @Provides DetailElementsViewPresenter provideDetailElementsViewPresenter(
      GenericViewInjector viewInjector, OcmController ocmController,
      OcmViewGenerator ocmViewGenerator) {
    return new DetailElementsViewPresenter(viewInjector, ocmController, ocmViewGenerator);
  }
}

package com.gigigo.orchextra.core.sdk.di.modules;

import android.app.Application;
import com.gigigo.orchextra.core.sdk.OcmStyleUi;
import com.gigigo.orchextra.core.sdk.OcmStyleUiImp;
import com.gigigo.orchextra.core.controller.OcmViewGenerator;
import com.gigigo.orchextra.core.controller.model.detail.DetailElementsViewPresenter;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.core.sdk.OcmSchemeHandler;
import com.gigigo.orchextra.core.sdk.OcmViewGeneratorImp;
import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import com.gigigo.orchextra.core.sdk.application.OcmContextProviderImpl;
import com.gigigo.orchextra.core.sdk.application.OcmSdkLifecycle;
import com.gigigo.ui.imageloader.ImageLoader;
import com.gigigo.ui.imageloader.glide.GlideImageLoaderImp;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;
import orchextra.javax.inject.Provider;
import orchextra.javax.inject.Singleton;

@Module(includes = { ControllerModule.class, PresentationModule.class }) public class OcmModule {

  private final Application app;

  public OcmModule(Application app) {
    this.app = app;
  }

  @Singleton @Provides OcmContextProvider provideOcmContextProvider() {
    return new OcmContextProviderImpl(app.getApplicationContext());
  }

  @Singleton @Provides OcmSdkLifecycle provideOcmSdkLifecycle(
      OcmContextProvider ocmContextProvider) {
    OcmSdkLifecycle ocmSdkLifecycle = new OcmSdkLifecycle();

    ocmContextProvider.setOcmActivityLifecycle(ocmSdkLifecycle);

    return ocmSdkLifecycle;
  }

  @Singleton @Provides ImageLoader provideImageLoader() {
    return new GlideImageLoaderImp(app.getApplicationContext());
  }

  @Singleton @Provides OcmViewGenerator provideOcmViewGenerator(OcmController ocmController,
      Provider<DetailElementsViewPresenter> detailElementsViewPresenterProvides,
      ImageLoader imageLoader) {
    return new OcmViewGeneratorImp(ocmController, detailElementsViewPresenterProvides, imageLoader);
  }

  @Singleton @Provides Authoritation provideAuthoritation() {
    return new Authoritation();
  }

  @Singleton @Provides OcmSchemeHandler provideOcmSchemeHandler(OcmContextProvider contextProvider) {
    return new OcmSchemeHandler(contextProvider);
  }

  @Singleton @Provides OcmStyleUi provideOcmStyleUi() {
    return new OcmStyleUiImp();
  }
}

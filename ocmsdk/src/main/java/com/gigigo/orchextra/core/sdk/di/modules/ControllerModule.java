package com.gigigo.orchextra.core.sdk.di.modules;

import com.gigigo.interactorexecutor.base.invoker.InteractorInvoker;
import com.gigigo.orchextra.core.domain.interactors.elements.GetElementByIdInteractor;
import com.gigigo.orchextra.core.domain.interactors.home.GetMenuDataInteractor;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.sdk.OcmControllerImp;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;
import orchextra.javax.inject.Singleton;

@Module(includes = {DomainModule.class, InteractorModule.class}) public class ControllerModule {

  @Singleton @Provides OcmController provideOcmController(InteractorInvoker interactorInvoker,
      GetMenuDataInteractor getSectionsDataInteractor, GetElementByIdInteractor getElementByIdInteractor) {
    return new OcmControllerImp(interactorInvoker, getSectionsDataInteractor,
        getElementByIdInteractor);
  }
}

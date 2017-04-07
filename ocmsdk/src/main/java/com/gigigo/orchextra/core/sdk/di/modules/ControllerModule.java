package com.gigigo.orchextra.core.sdk.di.modules;

import com.gigigo.ggglib.ContextProvider;
import com.gigigo.interactorexecutor.base.invoker.InteractorInvoker;
import com.gigigo.orchextra.core.controller.OcmControllerImp;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.interactors.elements.GetElementByIdInteractor;
import com.gigigo.orchextra.core.domain.interactors.home.ClearMenuDataInteractor;
import com.gigigo.orchextra.core.domain.interactors.home.GetMenuDataInteractor;
import com.gigigo.orchextra.core.domain.invocators.MenuInteractorInvocator;
import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import com.gigigo.orchextra.core.sdk.di.qualifiers.UiThread;
import com.gigigo.threaddecoratedview.views.ThreadSpec;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;
import orchextra.javax.inject.Singleton;

@Module(includes = { DomainModule.class, InteractorModule.class }) public class ControllerModule {

  @Singleton @Provides MenuInteractorInvocator provideMenuInteractorInvocator(OcmContextProvider contextProvider,
      @UiThread ThreadSpec threadSpec, InteractorInvoker interactorInvoker,
      GetMenuDataInteractor getSectionsDataInteractor,
      ClearMenuDataInteractor clearMenuDataInteractor) {
    return new MenuInteractorInvocator(contextProvider, threadSpec, interactorInvoker, getSectionsDataInteractor,
        clearMenuDataInteractor);
  }

  @Singleton @Provides OcmController provideOcmController(InteractorInvoker interactorInvoker,
      GetElementByIdInteractor getElementByIdInteractor,
      MenuInteractorInvocator menuInteractorInvocator) {
    return new OcmControllerImp(interactorInvoker, getElementByIdInteractor,
        menuInteractorInvocator);
  }
}

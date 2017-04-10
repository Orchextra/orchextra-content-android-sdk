package com.gigigo.orchextra.core.sdk.di.modules;

import com.gigigo.interactorexecutor.base.invoker.InteractorInvoker;
import com.gigigo.orchextra.core.controller.OcmControllerImp;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.data.DataBaseDataSource;
import com.gigigo.orchextra.core.domain.interactors.detailcontentelements.GetElementContentInteractor;
import com.gigigo.orchextra.core.domain.interactors.elements.ClearElementsInteractor;
import com.gigigo.orchextra.core.domain.interactors.elements.GetElementByIdInteractor;
import com.gigigo.orchextra.core.domain.interactors.elements.SaveElementByIdInteractor;
import com.gigigo.orchextra.core.domain.interactors.home.ClearMenuDataInteractor;
import com.gigigo.orchextra.core.domain.interactors.home.GetMenuDataInteractor;
import com.gigigo.orchextra.core.domain.invocators.DetailContentElementInteractorInvocator;
import com.gigigo.orchextra.core.domain.invocators.GridElementsInteractorInvocator;
import com.gigigo.orchextra.core.domain.invocators.MenuInteractorInvocator;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;
import orchextra.javax.inject.Singleton;

@Module(includes = { DomainModule.class, InteractorModule.class }) public class ControllerModule {

  @Singleton @Provides MenuInteractorInvocator provideMenuInteractorInvocator(
      InteractorInvoker interactorInvoker, GetMenuDataInteractor getSectionsDataInteractor,
      ClearMenuDataInteractor clearMenuDataInteractor) {
    return new MenuInteractorInvocator(interactorInvoker, getSectionsDataInteractor,
        clearMenuDataInteractor);
  }

  @Singleton @Provides GridElementsInteractorInvocator provideGridElementsInteractorInvocator(
      InteractorInvoker interactorInvoker, GetElementByIdInteractor getElementByIdInteractor,
      SaveElementByIdInteractor saveElementByIdInteractor,
      ClearElementsInteractor clearElementsInteractor) {
    return new GridElementsInteractorInvocator(interactorInvoker, getElementByIdInteractor,
        saveElementByIdInteractor, clearElementsInteractor);
  }

  @Singleton @Provides
  DetailContentElementInteractorInvocator provideDetailContentElementInteractorInvocator(
      InteractorInvoker interactorInvoker, GetElementContentInteractor getElementContentInteractor,
      DataBaseDataSource dataBaseDataSource) {
    return new DetailContentElementInteractorInvocator(interactorInvoker,
        getElementContentInteractor, dataBaseDataSource);
  }

  @Singleton @Provides OcmController provideOcmController(
      MenuInteractorInvocator menuInteractorInvocator,
      GridElementsInteractorInvocator gridElementsInteractorInvocator,
      DetailContentElementInteractorInvocator detailContentElementInteractorInvocator) {

    return new OcmControllerImp(menuInteractorInvocator, gridElementsInteractorInvocator,
        detailContentElementInteractorInvocator);
  }
}

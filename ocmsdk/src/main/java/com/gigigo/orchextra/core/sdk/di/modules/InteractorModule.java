package com.gigigo.orchextra.core.sdk.di.modules;

import com.gigigo.orchextra.core.domain.data.DataBaseDataSource;
import com.gigigo.orchextra.core.domain.data.ElementNetworkDataSource;
import com.gigigo.orchextra.core.domain.data.MenuNetworkDataSource;
import com.gigigo.orchextra.core.domain.interactors.detailcontentelements.GetElementContentInteractor;
import com.gigigo.orchextra.core.domain.interactors.elements.ClearElementsInteractor;
import com.gigigo.orchextra.core.domain.interactors.elements.GetElementByIdInteractor;
import com.gigigo.orchextra.core.domain.interactors.elements.SaveElementByIdInteractor;
import com.gigigo.orchextra.core.domain.interactors.home.ClearMenuDataInteractor;
import com.gigigo.orchextra.core.domain.interactors.home.GetMenuDataInteractor;
import com.gigigo.orchextra.core.domain.services.MenuNetworkDomainService;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;
import orchextra.javax.inject.Singleton;

@Module(includes = { NetworkModule.class, DbModule.class }) public class InteractorModule {

  @Singleton @Provides MenuNetworkDomainService provideMenuNetworkDomainService(
      ConnectionUtils connectionUtils, MenuNetworkDataSource menuNetworkDataSource) {
    return new MenuNetworkDomainService(connectionUtils, menuNetworkDataSource);
  }

  @Singleton @Provides GetElementByIdInteractor provideGetElementByIdInteractor(
      ConnectionUtils connectionUtils, ElementNetworkDataSource elementNetworkDataSource,
      DataBaseDataSource dataBaseDataSource) {
    return new GetElementByIdInteractor(connectionUtils, elementNetworkDataSource,
        dataBaseDataSource);
  }

  @Singleton @Provides GetMenuDataInteractor provideGetSectionsDataInteractor(
      DataBaseDataSource dataBaseDataSource, MenuNetworkDomainService menuNetworkDomainService) {
    return new GetMenuDataInteractor(dataBaseDataSource, menuNetworkDomainService);
  }

  @Singleton @Provides ClearMenuDataInteractor provideClearMenuDataInteractor(
      DataBaseDataSource dataBaseDataSource) {
    return new ClearMenuDataInteractor(dataBaseDataSource);
  }

  @Singleton @Provides SaveElementByIdInteractor provideSaveElementByIdInteractor(DataBaseDataSource dataBaseDataSource) {
    return new SaveElementByIdInteractor(dataBaseDataSource);
  }

  @Singleton @Provides ClearElementsInteractor provideClearElementsInteractor(DataBaseDataSource dataBaseDataSource) {
    return new ClearElementsInteractor(dataBaseDataSource);
  }

  @Singleton @Provides GetElementContentInteractor provideGetElementContentInteractor(DataBaseDataSource dataBaseDataSource) {
    return new GetElementContentInteractor(dataBaseDataSource);
  }
}

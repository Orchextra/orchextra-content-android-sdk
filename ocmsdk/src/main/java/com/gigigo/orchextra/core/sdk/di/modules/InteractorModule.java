package com.gigigo.orchextra.core.sdk.di.modules;

import com.gigigo.orchextra.core.domain.data.DataBaseDataSource;
import com.gigigo.orchextra.core.domain.data.ElementNetworkDataSource;
import com.gigigo.orchextra.core.domain.data.MenuNetworkDataSource;
import com.gigigo.orchextra.core.domain.interactors.elements.GetElementByIdInteractor;
import com.gigigo.orchextra.core.domain.interactors.home.ClearMenuDataInteractor;
import com.gigigo.orchextra.core.domain.interactors.home.GetMenuDataInteractor;
import com.gigigo.orchextra.core.domain.services.MenuDatabaseDomainService;
import com.gigigo.orchextra.core.domain.services.MenuNetworkDomainService;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;
import orchextra.javax.inject.Singleton;

@Module(includes = { NetworkModule.class, DbModule.class }) public class InteractorModule {

  @Singleton @Provides MenuDatabaseDomainService provideMenuDatabaseDomainService(
      DataBaseDataSource dataBaseDataSource) {
    return new MenuDatabaseDomainService(dataBaseDataSource);
  }

  @Singleton @Provides MenuNetworkDomainService provideMenuNetworkDomainService(
      ConnectionUtils connectionUtils, MenuNetworkDataSource menuNetworkDataSource) {
    return new MenuNetworkDomainService(connectionUtils, menuNetworkDataSource);
  }

  @Singleton @Provides GetElementByIdInteractor provideGetElementByIdInteractor(
      ConnectionUtils connectionUtils, ElementNetworkDataSource elementNetworkDataSource) {
    return new GetElementByIdInteractor(connectionUtils, elementNetworkDataSource);
  }

  @Singleton @Provides GetMenuDataInteractor provideGetSectionsDataInteractor(
      MenuDatabaseDomainService menuDatabaseDomainService,
      MenuNetworkDomainService menuNetworkDomainService) {
    return new GetMenuDataInteractor(menuDatabaseDomainService, menuNetworkDomainService);
  }

  @Singleton @Provides ClearMenuDataInteractor provideClearMenuDataInteractor(
      DataBaseDataSource dataBaseDataSource) {
    return new ClearMenuDataInteractor(dataBaseDataSource);
  }
}

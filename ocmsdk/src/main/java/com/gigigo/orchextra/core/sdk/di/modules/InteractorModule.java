package com.gigigo.orchextra.core.sdk.di.modules;

import com.gigigo.orchextra.core.domain.data.ElementNetworkDataSource;
import com.gigigo.orchextra.core.domain.interactor.elements.GetElementByIdInteractor;
import com.gigigo.orchextra.core.domain.interactor.home.GetMenuDataInteractor;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import com.gigigo.orchextra.core.domain.data.MenuNetworkDataSource;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;
import orchextra.javax.inject.Singleton;

@Module(includes = NetworkModule.class) public class InteractorModule {

  @Singleton @Provides GetElementByIdInteractor provideGetElementByIdInteractor(
      ConnectionUtils connectionUtils, ElementNetworkDataSource elementNetworkDataSource) {
    return new GetElementByIdInteractor(connectionUtils, elementNetworkDataSource);
  }

  @Singleton @Provides GetMenuDataInteractor provideGetSectionsDataInteractor(
      ConnectionUtils connectionUtils, MenuNetworkDataSource menuNetworkDataSource) {
    return new GetMenuDataInteractor(connectionUtils, menuNetworkDataSource);
  }
}

package com.gigigo.orchextra.core.sdk.di.modules;

import com.gigigo.ggglib.network.executors.ApiServiceExecutor;
import com.gigigo.ggglib.network.mappers.ApiGenericResponseMapper;
import com.gigigo.orchextra.core.data.api.ElementNetworkDataSourceImp;
import com.gigigo.orchextra.core.data.api.SearcherNetworkDataSourceImp;
import com.gigigo.orchextra.core.data.api.HomeNetworkDataSourceImp;
import com.gigigo.orchextra.core.data.api.MenuNetworkDataSourceImp;
import com.gigigo.orchextra.core.data.api.services.OcmApiService;
import com.gigigo.orchextra.core.domain.data.ElementNetworkDataSource;
import com.gigigo.orchextra.core.domain.data.SectionNetworkDataSource;
import com.gigigo.orchextra.core.domain.data.MenuNetworkDataSource;
import com.gigigo.orchextra.core.domain.data.SearcherNetworkDataSource;
import com.gigigo.orchextra.core.sdk.di.qualifiers.ApiServiceExecutorOcm;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;
import orchextra.javax.inject.Named;
import orchextra.javax.inject.Singleton;

@Module(includes = { ApiModule.class, ApiMapperModule.class })
public class NetworkModule {

  @Singleton @Provides ElementNetworkDataSource provideElementNetworkDataSource(
      @ApiServiceExecutorOcm ApiServiceExecutor serviceExecutor, OcmApiService apiService,
      @Named("MapperApiElementCacheResponse") ApiGenericResponseMapper responseMapper) {
    return new ElementNetworkDataSourceImp(serviceExecutor, apiService, responseMapper);
  }

  @Singleton @Provides MenuNetworkDataSource provideMenusNetworkDataSource(
      @ApiServiceExecutorOcm ApiServiceExecutor serviceExecutor, OcmApiService apiService,
      @Named("MapperApiMenuResponse") ApiGenericResponseMapper responseMapper) {
    return new MenuNetworkDataSourceImp(serviceExecutor, apiService, responseMapper);
  }

  @Singleton @Provides SectionNetworkDataSource provideHomeNetworkDataSource(
      @ApiServiceExecutorOcm ApiServiceExecutor serviceExecutor, OcmApiService apiService,
      @Named("MapperApiHomeResponse") ApiGenericResponseMapper responseMapper) {
    return new HomeNetworkDataSourceImp(serviceExecutor, apiService, responseMapper);
  }

  @Singleton @Provides SearcherNetworkDataSource provideSearcherNetworkDataSource(
      @ApiServiceExecutorOcm ApiServiceExecutor serviceExecutor, OcmApiService apiService,
      @Named("MapperApiHomeResponse") ApiGenericResponseMapper responseMapper) {
    return new SearcherNetworkDataSourceImp(serviceExecutor, apiService, responseMapper);
  }

}
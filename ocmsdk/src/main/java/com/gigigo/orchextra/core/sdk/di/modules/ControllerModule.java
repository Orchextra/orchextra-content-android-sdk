package com.gigigo.orchextra.core.sdk.di.modules;

import com.gigigo.orchextra.core.AppExecutors;
import com.gigigo.orchextra.core.controller.OcmControllerImp;
import com.gigigo.orchextra.core.data.OcmDbDataSource;
import com.gigigo.orchextra.core.data.OcmNetworkDataSource;
import com.gigigo.orchextra.core.data.api.services.OcmApiService;
import com.gigigo.orchextra.core.data.database.OcmDatabase;
import com.gigigo.orchextra.core.data.rxCache.OcmCache;
import com.gigigo.orchextra.core.data.rxCache.OcmCacheImp;
import com.gigigo.orchextra.core.data.rxCache.imageCache.OcmImageCache;
import com.gigigo.orchextra.core.data.rxCache.imageCache.OcmImageCacheImp;
import com.gigigo.orchextra.core.data.rxExecutor.JobExecutor;
import com.gigigo.orchextra.core.data.rxRepository.OcmDataRepository;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.rxExecutor.PostExecutionThread;
import com.gigigo.orchextra.core.domain.rxExecutor.ThreadExecutor;
import com.gigigo.orchextra.core.domain.rxInteractor.ClearCache;
import com.gigigo.orchextra.core.domain.rxInteractor.GetDetail;
import com.gigigo.orchextra.core.domain.rxInteractor.GetMenus;
import com.gigigo.orchextra.core.domain.rxInteractor.GetSection;
import com.gigigo.orchextra.core.domain.rxInteractor.PriorityScheduler;
import com.gigigo.orchextra.core.domain.rxInteractor.SearchElements;
import com.gigigo.orchextra.core.domain.rxRepository.OcmRepository;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import com.gigigo.orchextra.ocm.UIThread;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;
import orchextra.javax.inject.Singleton;

@Module(includes = { DomainModule.class, InteractorModule.class }) public class ControllerModule {

  @Provides OcmController provideOcmController(GetMenus getMenus, GetSection getSection,
      GetDetail getDetail, SearchElements searchElements, ClearCache clearCache,
      ConnectionUtils connectionUtils) {

    return new OcmControllerImp(getMenus, getSection, getDetail, searchElements, clearCache,
        connectionUtils);
  }

  @Provides @Singleton ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
    return jobExecutor;
  }

  @Provides @Singleton PriorityScheduler providePriorityScheduler() {
    return PriorityScheduler.create();
  }

  @Provides @Singleton PostExecutionThread providePostExecutionThread(UIThread uiThread) {
    return uiThread;
  }

  @Provides @Singleton OcmRepository provideOcmRepository(OcmDataRepository ocmDataRepository) {
    return ocmDataRepository;
  }

  @Provides @Singleton OcmCache provideCache(OcmContextProvider context) {
    return new OcmCacheImp(context.getApplicationContext(),
        context.getApplicationContext().getCacheDir().getPath());
  }

  @Provides @Singleton OcmImageCache provideImageCache(OcmContextProvider context,
      ThreadExecutor threadExecutor, ConnectionUtils connectionUtils) {
    return new OcmImageCacheImp(context.getApplicationContext(), threadExecutor, connectionUtils);
  }

  @Provides @Singleton OcmDatabase provideOcmDatabase(OcmContextProvider context) {
    return OcmDatabase.Companion.create(context.getApplicationContext());
  }

  @Provides @Singleton OcmDbDataSource provideOcmDbDataSource(OcmDatabase ocmDatabase,
      AppExecutors appExecutors) {
    return new OcmDbDataSource(ocmDatabase, appExecutors);
  }

  @Provides @Singleton AppExecutors provideAppExecutors() {
    return new AppExecutors();
  }

  @Provides @Singleton OcmNetworkDataSource provideOcmNetworkDataSource(OcmApiService ocmApiService,
      OcmDbDataSource ocmDbDataSource, AppExecutors appExecutors) {
    return new OcmNetworkDataSource(ocmApiService, ocmDbDataSource, appExecutors);
  }
}

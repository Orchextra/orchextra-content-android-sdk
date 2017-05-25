package com.gigigo.orchextra.core.sdk.di.modules;

import android.content.Context;
import com.gigigo.interactorexecutor.base.invoker.InteractorInvoker;
import com.gigigo.orchextra.core.controller.OcmControllerImp;
import com.gigigo.orchextra.core.data.api.services.OcmApiService;
import com.gigigo.orchextra.core.data.rxCache.OcmCache;
import com.gigigo.orchextra.core.data.rxCache.OcmKacheImp;
import com.gigigo.orchextra.core.data.rxExecutor.JobExecutor;
import com.gigigo.orchextra.core.data.rxRepository.OcmDataRepository;
import com.gigigo.orchextra.core.data.rxRepository.rxDatasource.OcmCloudDataStore;
import com.gigigo.orchextra.core.data.rxRepository.rxDatasource.OcmDataStore;
import com.gigigo.orchextra.core.data.rxRepository.rxDatasource.OcmDataStoreFactory;
import com.gigigo.orchextra.core.data.rxRepository.rxDatasource.OcmDiskDataStore;
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
import com.gigigo.orchextra.core.domain.rxExecutor.PostExecutionThread;
import com.gigigo.orchextra.core.domain.rxExecutor.ThreadExecutor;
import com.gigigo.orchextra.core.domain.rxInteractor.GetMenus;
import com.gigigo.orchextra.core.domain.rxInteractor.UseCase;
import com.gigigo.orchextra.core.domain.rxRepository.OcmRepository;
import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import com.gigigo.orchextra.core.sdk.di.qualifiers.CacheDir;
import com.gigigo.orchextra.ocm.UIThread;
import com.gigigo.orchextra.ocmsdk.BuildConfig;
import javax.inject.Named;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;
import orchextra.javax.inject.Singleton;
import retrofit2.Retrofit;

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
      DetailContentElementInteractorInvocator detailContentElementInteractorInvocator, GetMenus getMenus) {

    return new OcmControllerImp(menuInteractorInvocator, gridElementsInteractorInvocator,
        detailContentElementInteractorInvocator, getMenus);
  }

  @Provides @Singleton ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
    return jobExecutor;
  }

  @Provides @Singleton PostExecutionThread providePostExecutionThread(UIThread uiThread) {
    return uiThread;
  }

  @Provides @Singleton OcmRepository provideOcmRepository(OcmDataRepository ocmDataRepository) {
    return ocmDataRepository;
  }

  @Provides @Singleton OcmCache provideCache(
      OcmContextProvider context) {
    return new OcmKacheImp(context.getApplicationContext(), context.getApplicationContext().getCacheDir().getPath());
  }

}

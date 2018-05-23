package com.gigigo.orchextra.core.sdk.di.modules;

import com.gigigo.orchextra.core.data.api.utils.ConnectionUtilsImp;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;
import orchextra.javax.inject.Singleton;

@Module public class DomainModule {

  @Singleton @Provides ConnectionUtils provideConnectionUtils(OcmContextProvider contextProvider) {
    return new ConnectionUtilsImp(contextProvider.getApplicationContext());
  }
}

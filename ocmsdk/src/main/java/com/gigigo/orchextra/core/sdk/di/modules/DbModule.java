package com.gigigo.orchextra.core.sdk.di.modules;


import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import com.gigigo.orchextra.core.sdk.utils.OcmPreferences;
import com.gigigo.orchextra.core.sdk.utils.OcmPreferencesImp;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;
import orchextra.javax.inject.Singleton;

@Module public class DbModule {

  @Singleton
  @Provides OcmPreferences provideOcmPreferences(OcmContextProvider ocmContextProvider) {
    return new OcmPreferencesImp(ocmContextProvider.getApplicationContext());
  }


}

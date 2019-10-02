package com.gigigo.orchextra.core.sdk.di.modules;

import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import com.gigigo.orchextra.core.sdk.utils.OcmPreferences;
import com.gigigo.orchextra.core.sdk.utils.OcmPreferencesImp;
import com.gigigo.orchextra.core.utils.VimeoCredentials;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {

    @Singleton
    @Provides
    OcmPreferences provideOcmPreferences(OcmContextProvider ocmContextProvider) {
        return new OcmPreferencesImp(ocmContextProvider.getApplicationContext());
    }

    @Singleton
    @Provides
    VimeoCredentials provideVimeoCredentials() {
        return new VimeoCredentials();
    }
}

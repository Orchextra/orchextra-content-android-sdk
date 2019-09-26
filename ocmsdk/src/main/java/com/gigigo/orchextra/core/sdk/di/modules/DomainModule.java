package com.gigigo.orchextra.core.sdk.di.modules;

import com.gigigo.orchextra.core.data.api.utils.ConnectionUtilsImp;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DomainModule {

    @Singleton
    @Provides
    ConnectionUtils provideConnectionUtils(OcmContextProvider contextProvider) {
        return new ConnectionUtilsImp(contextProvider.getApplicationContext());
    }
}

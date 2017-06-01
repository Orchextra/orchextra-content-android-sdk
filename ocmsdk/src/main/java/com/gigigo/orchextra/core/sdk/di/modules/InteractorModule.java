package com.gigigo.orchextra.core.sdk.di.modules;

import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;
import orchextra.javax.inject.Singleton;

@Module(includes = { NetworkModule.class, DbModule.class }) public class InteractorModule {

}

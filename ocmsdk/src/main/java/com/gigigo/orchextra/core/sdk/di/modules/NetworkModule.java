package com.gigigo.orchextra.core.sdk.di.modules;

import com.gigigo.ggglib.network.executors.ApiServiceExecutor;
import com.gigigo.ggglib.network.mappers.ApiGenericResponseMapper;
import com.gigigo.orchextra.core.data.api.services.OcmApiService;
import com.gigigo.orchextra.core.sdk.di.qualifiers.ApiServiceExecutorOcm;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;
import orchextra.javax.inject.Named;
import orchextra.javax.inject.Singleton;

@Module(includes = { ApiModule.class, ApiMapperModule.class })
public class NetworkModule {


}
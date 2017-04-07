package com.gigigo.orchextra.core.sdk.di.modules;

import com.gigigo.ggglib.network.converters.ErrorConverter;
import com.gigigo.ggglib.network.defaultelements.RetryOnErrorPolicy;
import com.gigigo.ggglib.network.executors.ApiServiceExecutor;
import com.gigigo.ggglib.network.executors.RetrofitApiServiceExcecutor;
import com.gigigo.orchextra.core.data.api.services.DefaultRetryOnErrorPolicyImpl;
import com.gigigo.orchextra.core.data.api.services.OcmApiService;
import com.gigigo.orchextra.core.data.api.services.OkHttpHeadersInterceptorOcm;
import com.gigigo.orchextra.core.domain.entities.ocm.OxSession;
import com.gigigo.orchextra.core.sdk.di.qualifiers.ApiServiceExecutorOcm;
import com.gigigo.orchextra.core.sdk.di.qualifiers.GsonConverterFactoryObject;
import com.gigigo.orchextra.core.sdk.di.qualifiers.HttpLoggingInterceptorOcm;
import com.gigigo.orchextra.core.sdk.di.qualifiers.OkHttpClientOcm;
import com.gigigo.orchextra.core.sdk.di.qualifiers.RetrofitLog;
import com.gigigo.orchextra.core.sdk.di.qualifiers.RetrofitOcm;
import com.gigigo.orchextra.core.sdk.di.qualifiers.RetryOnErrorPolicyOcm;
import com.gigigo.orchextra.ocmsdk.BuildConfig;
import com.gigigo.orchextra.core.data.api.dto.base.BaseApiResponse;
import com.gigigo.orchextra.core.data.api.services.DefaultErrorConverterImpl;
import com.gigigo.orchextra.core.sdk.di.qualifiers.EndpointOcm;
import com.gigigo.orchextra.core.sdk.di.qualifiers.ErrorConverterOcm;
import com.gigigo.orchextra.core.sdk.di.qualifiers.HeadersInterceptorOcm;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;
import orchextra.javax.inject.Singleton;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module public class ApiModule {

  @Provides @Singleton @EndpointOcm String provideEndpoint() {
    return BuildConfig.API_URL;
  }

  @Provides @Singleton @RetrofitLog boolean provideRetrofitLog() {
    return BuildConfig.RETROFIT_LOG;
  }

  @Provides @Singleton @HttpLoggingInterceptorOcm
  HttpLoggingInterceptor provideLoggingInterceptor() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    return interceptor;
  }

  @Provides
  @Singleton OxSession provideOxSession() {
    return new OxSession();
  }

  @Provides @Singleton @HeadersInterceptorOcm Interceptor provideHeadersInterceptorOcm(
      OxSession oxSession) {
    return new OkHttpHeadersInterceptorOcm(oxSession);
  }

  @Provides @Singleton @OkHttpClientOcm OkHttpClient provideOkHttpClientOcm(
      @RetrofitLog boolean retrofitLog, @HeadersInterceptorOcm Interceptor headersInterceptor,
      @HttpLoggingInterceptorOcm HttpLoggingInterceptor loggingInterceptor) {

    OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
    okHttpClientBuilder.addInterceptor(headersInterceptor);

    if (retrofitLog) {
      okHttpClientBuilder.addInterceptor(loggingInterceptor);
    }

    OkHttpClient okHttpClient = okHttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS).
        readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build();

    return okHttpClient;
  }

  @Provides @Singleton @GsonConverterFactoryObject
  GsonConverterFactory provideGsonConverterFactory() {
    return retrofit2.converter.gson.GsonConverterFactory.create();
  }

  @Provides @Singleton @RetrofitOcm Retrofit provideOcmRetrofitObject(@EndpointOcm String enpoint,
      @GsonConverterFactoryObject
          retrofit2.converter.gson.GsonConverterFactory gsonConverterFactory,
      @OkHttpClientOcm OkHttpClient okClient) {

    Retrofit retrofit = new Retrofit.Builder().baseUrl(enpoint)
        .client(okClient)
        .addConverterFactory(gsonConverterFactory)
        .build();

    return retrofit;
  }

  @Provides @Singleton @ErrorConverterOcm ErrorConverter provideErrorConverter(
      @RetrofitOcm Retrofit retrofit) {
    return new DefaultErrorConverterImpl(retrofit, BaseApiResponse.class);
  }

  @Provides @Singleton @RetryOnErrorPolicyOcm RetryOnErrorPolicy provideRetryOnErrorPolicy() {
    return new DefaultRetryOnErrorPolicyImpl();
  }

  @Provides @ApiServiceExecutorOcm ApiServiceExecutor provideOcmApiServiceExecutor(
      @ErrorConverterOcm ErrorConverter errorConverter,
      @RetryOnErrorPolicyOcm RetryOnErrorPolicy retryOnErrorPolicy) {
    return new RetrofitApiServiceExcecutor.Builder().errorConverter(errorConverter)
        .retryOnErrorPolicy(retryOnErrorPolicy)
        .build();
  }

  @Provides @Singleton OcmApiService provideOrchextraApiService(@RetrofitOcm Retrofit retrofit) {
    return retrofit.create(OcmApiService.class);
  }
}

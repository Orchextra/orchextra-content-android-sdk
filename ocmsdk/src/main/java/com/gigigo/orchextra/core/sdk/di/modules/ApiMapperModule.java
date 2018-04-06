package com.gigigo.orchextra.core.sdk.di.modules;

import com.gigigo.ggglib.network.mappers.ApiGenericResponseMapper;
import com.gigigo.orchextra.core.data.mappers.OcmGenericResponseMapper;
import com.gigigo.orchextra.core.data.mappers.contentdata.ApiContentDataResponseMapper;
import com.gigigo.orchextra.core.data.mappers.contentdata.ApiContentItemLayoutMapper;
import com.gigigo.orchextra.core.data.mappers.contentdata.ApiContentItemMapper;
import com.gigigo.orchextra.core.data.mappers.contentdata.ApiContentItemPatternMapper;
import com.gigigo.orchextra.core.data.mappers.elements.ApiElementDataMapper;
import com.gigigo.orchextra.core.data.mappers.menus.ApiMenuContentListResponseMapper;
import com.gigigo.orchextra.core.data.mappers.menus.ApiMenuContentMapper;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;
import orchextra.javax.inject.Named;
import orchextra.javax.inject.Singleton;

@Module public class ApiMapperModule {

  @Singleton @Provides ApiMenuContentMapper provideApiMenuContentMapper() {
    return new ApiMenuContentMapper();
  }

  @Singleton @Provides ApiElementDataMapper provideApiElementDataMapper() {
    return new ApiElementDataMapper();
  }

  @Singleton @Provides @Named("MapperApiMenuResponse")
  ApiGenericResponseMapper provideApiMenuMapper() {
    return new OcmGenericResponseMapper(new ApiMenuContentListResponseMapper());
  }

  @Singleton @Provides ApiContentDataResponseMapper provideApiContentDataResponseMapper(
      ApiContentItemMapper apiContentItemMapper) {
    return new ApiContentDataResponseMapper(apiContentItemMapper);
  }

  @Singleton @Provides ApiContentItemPatternMapper provideApiContentItemPatternMapper() {
    return new ApiContentItemPatternMapper();
  }

  @Singleton @Provides ApiContentItemLayoutMapper provideApiContentItemLayoutMapper(
      ApiContentItemPatternMapper apiContentItemPatternMapper) {
    return new ApiContentItemLayoutMapper(apiContentItemPatternMapper);
  }

  @Singleton @Provides ApiContentItemMapper provideApiContentItemMapper(
      ApiContentItemLayoutMapper apiContentItemLayoutMapper) {
    return new ApiContentItemMapper(apiContentItemLayoutMapper);
  }

  @Singleton @Provides @Named("MapperApiHomeResponse")
  ApiGenericResponseMapper provideApiHomeMapper(ApiContentItemMapper apiContentItemMapper) {
    return new OcmGenericResponseMapper(new ApiContentDataResponseMapper(apiContentItemMapper));
  }

  @Singleton @Provides @Named("MapperApiElementCacheResponse")
  ApiGenericResponseMapper provideApiElementCacheResponseMapper(
      ApiElementDataMapper apiElementDataMapper) {
    return new OcmGenericResponseMapper(apiElementDataMapper);
  }
}

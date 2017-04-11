package com.gigigo.orchextra.core.sdk.di.modules;

import com.gigigo.orchextra.core.data.db.DataBaseDataSourceImp;
import com.gigigo.orchextra.core.domain.data.DataBaseDataSource;
import orchextra.dagger.Module;
import orchextra.dagger.Provides;
import orchextra.javax.inject.Singleton;

@Module public class DbModule {

  @Provides @Singleton DataBaseDataSource provideDataBaseDataSource() {
    return new DataBaseDataSourceImp();
  }
}

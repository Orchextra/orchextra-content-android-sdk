package com.gigigo.orchextra.core.sdk.di.providers;

import com.gigigo.orchextra.core.domain.data.DataBaseDataSource;

public interface DbModuleProvider {
  DataBaseDataSource provideDataBaseDataSource();
}

package com.gigigo.orchextra.core.sdk.di.providers;

import com.gigigo.orchextra.core.domain.data.ElementNetworkDataSource;
import com.gigigo.orchextra.core.domain.data.SectionNetworkDataSource;
import com.gigigo.orchextra.core.domain.data.MenuNetworkDataSource;
import com.gigigo.orchextra.core.domain.data.SearcherNetworkDataSource;

public interface NetworkModuleProvider {

  ElementNetworkDataSource provideElementNetworkDataSource();

  MenuNetworkDataSource provideMenusNetworkDataSource();

  SectionNetworkDataSource provideHomeNetworkDataSource();

  SearcherNetworkDataSource provideSearcherNetworkDataSource();
}

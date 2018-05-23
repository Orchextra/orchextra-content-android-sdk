package com.gigigo.orchextra.core.sdk.di.providers;


import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;

public interface DomainModuleProvider {

  ConnectionUtils provideConnectionUtils();
}

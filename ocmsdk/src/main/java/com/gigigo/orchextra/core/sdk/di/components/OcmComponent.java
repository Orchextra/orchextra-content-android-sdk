package com.gigigo.orchextra.core.sdk.di.components;

import com.gigigo.orchextra.core.sdk.di.modules.OcmModule;
import com.gigigo.orchextra.core.sdk.di.providers.OcmModuleProvider;
import com.gigigo.orchextra.ocm.OCManager;
import orchextra.dagger.Component;
import orchextra.javax.inject.Singleton;

@Singleton @Component(modules = OcmModule.class) public interface OcmComponent
    extends OcmModuleProvider {
  void injectOcm(OCManager instance);
}

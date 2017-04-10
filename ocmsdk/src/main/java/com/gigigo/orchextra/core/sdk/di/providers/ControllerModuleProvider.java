package com.gigigo.orchextra.core.sdk.di.providers;

import com.gigigo.orchextra.core.domain.OcmController;

public interface ControllerModuleProvider extends DomainModuleProvider, InteractorModuleProvider {

  OcmController provideOcmController();
}

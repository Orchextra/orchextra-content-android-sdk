package com.gigigo.orchextra.core.sdk.di.providers;

import com.gigigo.orchextra.core.sdk.OcmStyleUi;
import com.gigigo.orchextra.core.sdk.actions.ActionHandler;
import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import com.gigigo.orchextra.core.sdk.application.OcmSdkLifecycle;

public interface OcmModuleProvider extends ControllerModuleProvider, PresentationModuleProvider {

  OcmContextProvider provideOcmContextProvider();

  OcmSdkLifecycle provideOcmSdkLifecycle();

  OcmStyleUi provideOcmStyleUi();

  ActionHandler provideActionHandler();
}

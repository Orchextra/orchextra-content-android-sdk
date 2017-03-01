package com.gigigo.orchextra.core.sdk.di.providers;


import com.gigigo.interactorexecutor.base.invoker.InteractorInvoker;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;

public interface DomainModuleProvider {
  InteractorInvoker provideInteractorInvoker();

  ConnectionUtils provideConnectionUtils();
}

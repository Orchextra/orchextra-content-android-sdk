package com.gigigo.orchextra.core.sdk.di.providers;

import com.gigigo.interactorexecutor.base.viewinjector.GenericViewInjector;
import com.gigigo.orchextra.core.controller.model.detail.DetailElementsViewPresenter;

public interface PresentationModuleProvider {
  GenericViewInjector provideGenericViewInjector();

  DetailElementsViewPresenter provideDetailElementsViewPresenter();
}

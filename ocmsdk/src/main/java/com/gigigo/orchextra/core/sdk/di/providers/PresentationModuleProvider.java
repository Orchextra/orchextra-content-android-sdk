package com.gigigo.orchextra.core.sdk.di.providers;

import com.gigigo.orchextra.core.controller.model.detail.DetailElementsViewPresenter;

public interface PresentationModuleProvider {

  DetailElementsViewPresenter provideDetailElementsViewPresenter();
}

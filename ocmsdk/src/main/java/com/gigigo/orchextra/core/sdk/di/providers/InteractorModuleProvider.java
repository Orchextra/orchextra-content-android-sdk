package com.gigigo.orchextra.core.sdk.di.providers;

import com.gigigo.orchextra.core.domain.interactors.elements.GetElementByIdInteractor;
import com.gigigo.orchextra.core.domain.interactors.home.GetMenuDataInteractor;

public interface InteractorModuleProvider extends NetworkModuleProvider {

  GetElementByIdInteractor provideGetElementByIdInteractor();
  GetMenuDataInteractor provideGetSectionsDataInteractor();
}

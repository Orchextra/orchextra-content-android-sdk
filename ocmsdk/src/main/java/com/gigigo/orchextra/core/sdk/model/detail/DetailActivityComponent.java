package com.gigigo.orchextra.core.sdk.model.detail;

import com.gigigo.orchextra.core.sdk.di.providers.OcmModuleProvider;
import com.gigigo.orchextra.core.sdk.di.components.OcmComponent;
import com.gigigo.orchextra.core.sdk.di.scopes.PerActivity;
import orchextra.dagger.Component;

@PerActivity @Component(dependencies = OcmComponent.class, modules = DetailActivityModule.class)
public interface DetailActivityComponent extends OcmModuleProvider {
  void injectDetailActivity(DetailActivity detailActivity);
}

package com.gigigo.orchextra.core.sdk.model.detail.layouts;

import com.gigigo.orchextra.core.sdk.di.components.OcmComponent;
import com.gigigo.orchextra.core.sdk.di.providers.OcmModuleProvider;
import com.gigigo.orchextra.core.sdk.di.scopes.PerSection;
import orchextra.dagger.Component;

@PerSection @Component(dependencies = OcmComponent.class, modules = DetailContentDataModule.class)
public interface DetailContentDataComponent extends OcmModuleProvider {
  void injectDetailContentData(DetailParentContentData detailParentContentData);
}

package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype;

import com.gigigo.orchextra.core.sdk.di.components.OcmComponent;
import com.gigigo.orchextra.core.sdk.di.providers.OcmModuleProvider;
import com.gigigo.orchextra.core.sdk.di.scopes.PerSection;
import com.gigigo.orchextra.core.sdk.model.detail.layouts.DetailContentDataModule;
import com.gigigo.orchextra.core.sdk.model.detail.layouts.DetailParentContentData;
import orchextra.dagger.Component;

@PerSection @Component(dependencies = OcmComponent.class, modules = ArticleContentDataModule.class)
public interface ArticleContentDataComponent extends OcmModuleProvider {
  void injectArticleContentData(ArticleContentData articleContentData);
}

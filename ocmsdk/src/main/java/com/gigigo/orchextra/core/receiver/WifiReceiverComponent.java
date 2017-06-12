package com.gigigo.orchextra.core.receiver;

import com.gigigo.orchextra.core.sdk.di.components.OcmComponent;
import com.gigigo.orchextra.core.sdk.di.providers.OcmModuleProvider;
import com.gigigo.orchextra.core.sdk.di.scopes.PerSection;
import com.gigigo.orchextra.core.sdk.model.detail.layouts.DetailParentContentData;
import orchextra.dagger.Component;

@PerSection @Component(dependencies = OcmComponent.class, modules = WifiReceiverModule.class)
public interface WifiReceiverComponent extends OcmModuleProvider {
  void injectWifiReceiver(WifiReceiver wifiReceiver);
}

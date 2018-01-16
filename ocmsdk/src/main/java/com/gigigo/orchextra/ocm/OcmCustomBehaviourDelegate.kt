package com.gigigo.orchextra.ocm

interface OcmCustomBehaviourDelegate {
  fun contentNeedsValidation(customProperties: Map<String, Any>, completion: (Boolean) -> Unit)

  //ViewCustomizationType customizationForContent(Map<String, Any> properties, ViewType viewType);
}

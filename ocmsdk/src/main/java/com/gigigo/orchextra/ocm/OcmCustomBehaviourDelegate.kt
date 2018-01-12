package com.gigigo.orchextra.ocm

interface OcmCustomBehaviourDelegate {
  fun contentNeedsValidation(customProperties: Map<String, String>, completion: (Boolean) -> Unit)

  //ViewCustomizationType customizationForContent(Map<String, String> properties, ViewType viewType);
}

package com.gigigo.orchextra.ocm;

import java.util.Map;

public interface OcmCustomBehaviourDelegate {
  void contentNeedsValidation(Map<String, String> customProperties, CompletionCallback completionCallback);

  //ViewCustomizationType customizationForContent(Map<String, String> customProperties, ViewType viewType);
}

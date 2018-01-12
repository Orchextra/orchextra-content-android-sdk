package com.gigigo.orchextra.core.domain.entities.elements;

import java.util.Map;

public class ElementCustomProperty {
  private Map<String, String> customProperties;

  public Map<String, String> getCustomProperties() {
    return customProperties;
  }

  public void setCustomProperties(Map<String, String> customProperties) {
    this.customProperties = customProperties;
  }
}
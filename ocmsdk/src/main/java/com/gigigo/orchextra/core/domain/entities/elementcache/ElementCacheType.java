package com.gigigo.orchextra.core.domain.entities.elementcache;

public enum ElementCacheType {
  GO_CONTENT("goContent"),
  WEBVIEW("webview"),
  BROWSER("browser"),
  ARTICLE("article"),
  DEEP_LINK("deepLink"),
  SCAN("scan"),
  VUFORIA("vuforia"),
  IMAGE("image"),
  VIDEO("video"),
  NONE("");

  private final String type;

  ElementCacheType(String type) {
    this.type = type;
  }

  public static ElementCacheType convertStringToEnum(String type) {
    ElementCacheType[] values = ElementCacheType.values();
    for (ElementCacheType value : values) {
      if (value.getType().equals(type)) {
        return value;
      }
    }
    return NONE;
  }

  private String getType() {
    return type;
  }
}

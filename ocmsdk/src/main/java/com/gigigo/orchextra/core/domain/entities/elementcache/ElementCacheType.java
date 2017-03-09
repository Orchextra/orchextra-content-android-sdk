package com.gigigo.orchextra.core.domain.entities.elementcache;

public enum ElementCacheType {
  ARTICLE("article"),
  WEBVIEW("webview"),
  VUFORIA("vuforia"),
  SCAN("scan"),
  BROWSER("browser"),
  VIDEO("video"),

  GO_CONTENT("goContent"),
  DEEP_LINK("deepLink"),
  IMAGE("image"),
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

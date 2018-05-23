package com.gigigo.orchextra.core.domain.entities.contentdata;

public enum ContentItemTypeLayout {
  GRID("grid"),
  FULLSCREEN("fullScreen"),
  CAROUSEL("carousel");

  private final String type;

  ContentItemTypeLayout(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public static ContentItemTypeLayout convertStringToEnum(String type) {
    ContentItemTypeLayout[] values = ContentItemTypeLayout.values();
    for (ContentItemTypeLayout value : values) {
      if (value.getType().equals(type)) {
        return value;
      }
    }
    return GRID;
  }
}

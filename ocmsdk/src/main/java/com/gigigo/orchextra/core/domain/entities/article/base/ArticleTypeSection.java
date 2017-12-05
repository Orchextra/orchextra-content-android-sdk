package com.gigigo.orchextra.core.domain.entities.article.base;

import java.io.Serializable;

public enum ArticleTypeSection implements Serializable {
  HEADER("header"),
  IMAGE("image"),
  VIDEO("video"),
  RICH_TEXT("richText"),
  IMAGE_AND_TEXT("imageText"),
  TEXT_AND_IMAGE("textImage"),
  BUTTON("button"),
  NONE("");

  private final String typeSection;

  ArticleTypeSection(String typeSection) {
    this.typeSection = typeSection;
  }

  public static ArticleTypeSection convertStringToEnum(String type) {
    ArticleTypeSection[] values = ArticleTypeSection.values();
    for (ArticleTypeSection value : values) {
      if (value.getTypeSection().equals(type)) {
        return value;
      }
    }
    return NONE;
  }

  public String getTypeSection() {
    return typeSection;
  }
}

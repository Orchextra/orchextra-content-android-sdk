package com.gigigo.orchextra.core.domain.entities.article;

public enum ArticleTypeSection {
  HEADER("header"),
  IMAGE("image"),
  VIDEO("video"),
  RICH_TEXT("richText"),
  IMAGE_AND_TEXT("imageText"),
  TEXT_AND_IMAGE("textImage"),
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

package com.gigigo.orchextra.core.domain.entities.article.base;

public enum ArticleButtonSize {
  BIG("big"),
  MEDIUM("medium"),
  SMALL("small");

  private final String size;

  ArticleButtonSize(String size) {
    this.size = size;
  }

  public String getSize() {
    return size;
  }

  public static ArticleButtonSize convertStringToEnum(String size) {
    ArticleButtonSize[] values = ArticleButtonSize.values();
    for (ArticleButtonSize value : values) {
      if (value.getSize().equals(size)) {
        return value;
      }
    }

    return MEDIUM;
  }
}

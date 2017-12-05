package com.gigigo.orchextra.core.domain.entities.article.base;

import java.io.Serializable;

public enum ArticleButtonType implements Serializable {
  IMAGE("image"),
  DEFAULT("default");

  private final String typeButton;

  ArticleButtonType(String typeButton) {
    this.typeButton = typeButton;
  }

  public static ArticleButtonType convertStringToEnum(String type) {
    ArticleButtonType[] values = ArticleButtonType.values();
    for (ArticleButtonType value : values) {
      if (value.getTypeButton().equals(type)) {
        return value;
      }
    }
    return DEFAULT;
  }

  public String getTypeButton() {
    return typeButton;
  }
}

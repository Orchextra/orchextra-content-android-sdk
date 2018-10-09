package com.gigigo.orchextra.core.domain.entities.article.base

import java.io.Serializable

enum class ArticleButtonType constructor(val typeButton: String) : Serializable {
  IMAGE("image"),
  DEFAULT("default");

  companion object {
    fun convertFromString(type: String?): ArticleButtonType {
      for (value in ArticleButtonType.values()) {
        if (value.typeButton.equals(type, true)) {
          return value
        }
      }
      return DEFAULT
    }
  }
}

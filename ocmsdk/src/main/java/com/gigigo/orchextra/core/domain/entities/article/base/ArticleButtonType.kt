package com.gigigo.orchextra.core.domain.entities.article.base

import java.io.Serializable

enum class ArticleButtonType constructor(val typeButton: String) : Serializable {
  IMAGE("image"),
  DEFAULT("default");

  companion object {
    fun convertFromString(type: String?): ArticleButtonType {
      val values = ArticleButtonType.values()
      for (value in values) {
        if (value.typeButton == type) {
          return value
        }
      }
      return DEFAULT
    }
  }
}

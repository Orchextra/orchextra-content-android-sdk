package com.gigigo.orchextra.core.domain.entities.article.base

import java.io.Serializable

enum class ArticleButtonSize constructor(val size: String) : Serializable {
  BIG("big"),
  MEDIUM("medium"),
  SMALL("small");

  companion object {
    fun convertFromString(size: String?): ArticleButtonSize {
      val values = ArticleButtonSize.values()
      for (value in values) {
        if (value.size == size) {
          return value
        }
      }
      return MEDIUM
    }
  }
}
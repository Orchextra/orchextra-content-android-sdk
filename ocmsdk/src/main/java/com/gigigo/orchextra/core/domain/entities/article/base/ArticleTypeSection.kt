package com.gigigo.orchextra.core.domain.entities.article.base

import java.io.Serializable

enum class ArticleTypeSection constructor(val typeSection: String) : Serializable {
  HEADER("header"),
  IMAGE("image"),
  VIDEO("video"),
  RICH_TEXT("richText"),
  IMAGE_AND_TEXT("imageText"),
  TEXT_AND_IMAGE("textImage"),
  BUTTON("button"),
  NONE("");


  companion object {

    fun convertStringToEnum(type: String?): ArticleTypeSection {
      for (value in ArticleTypeSection.values()) {
        if (value.typeSection.equals(type, true)) {
          return value
        }
      }
      return NONE
    }
  }
}

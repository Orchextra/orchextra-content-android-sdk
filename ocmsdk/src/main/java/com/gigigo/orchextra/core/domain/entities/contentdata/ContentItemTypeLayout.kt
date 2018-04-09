package com.gigigo.orchextra.core.domain.entities.contentdata

enum class ContentItemTypeLayout constructor(val type: String) {
  GRID("grid"),
  CAROUSEL("carousel");

  companion object {

    fun convertFromString(type: String?): ContentItemTypeLayout {
      val values = ContentItemTypeLayout.values()
      for (value in values) {
        if (value.type.equals(type,true)) {
          return value
        }
      }
      return GRID
    }
  }
}
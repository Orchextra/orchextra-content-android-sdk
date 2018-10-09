package com.gigigo.orchextra.core.domain.entities.elementcache

import java.io.Serializable

enum class ElementCacheBehaviour constructor(val cacheBehaviour: String) : Serializable {
  CLICK("click"),
  SWIPE("swipe"),
  NONE("");

  companion object {

    fun convertStringToEnum(behaviour: String?): ElementCacheBehaviour {
      val values = ElementCacheBehaviour.values()
      for (value in values) {
        if (value.cacheBehaviour.equals(behaviour, true)) {
          return value
        }
      }
      return NONE
    }
  }
}
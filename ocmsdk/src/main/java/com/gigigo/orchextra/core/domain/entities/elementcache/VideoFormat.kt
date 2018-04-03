package com.gigigo.orchextra.core.domain.entities.elementcache

import java.io.Serializable

enum class VideoFormat constructor(val videoFormat: String) : Serializable {
  YOUTUBE("youtube"),
  VIMEO("vimeo"),
  NONE("");

  companion object {

    fun convertStringToType(format: String): VideoFormat {
      for (value in VideoFormat.values()) {
        if (value.videoFormat.equals(format, true)) {
          return value
        }
      }
      return NONE
    }
  }
}
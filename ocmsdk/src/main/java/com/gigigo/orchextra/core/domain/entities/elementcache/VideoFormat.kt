package com.gigigo.orchextra.core.domain.entities.elementcache

import java.io.Serializable

enum class VideoFormat private constructor(val videoFormat: String) : Serializable {
  YOUTUBE("youtube"), VIMEO("vimeo"), NONE("");


  companion object {

    fun convertStringToType(format: String): VideoFormat {
      val values = VideoFormat.values()
      for (value in values) {
        if (value.videoFormat.equals(format, ignoreCase = true)) {
          return value
        }
      }
      return NONE
    }
  }
}

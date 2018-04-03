package com.gigigo.orchextra.core.domain.entities.elementcache

import java.io.Serializable

enum class ElementCacheType private constructor(private val type: String) : Serializable {
  ARTICLE("article"),
  CARDS("cards"),
  VUFORIA("vuforia"),
  SCAN("scan"),
  WEBVIEW("webview"), //WEBVIEW
  BROWSER("browser"), //CUSTOM TABS
  EXTERNAL_BROWSER("externalBrowser"), //CHROME
  VIDEO("video"),
  GO_CONTENT("openContent"),
  DEEP_LINK("deepLink"),
  IMAGE("image"),
  NONE("");


  companion object {

    fun convertStringToEnum(type: String): ElementCacheType {
      val values = ElementCacheType.values()
      for (value in values) {
        if (value.type.equals(type, ignoreCase = true)) {
          return value
        }
      }
      return NONE
    }
  }
}

package com.gigigo.orchextra.core.domain.entities.elementcache

import java.io.Serializable

enum class ElementCacheType constructor(val type: String) : Serializable {
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

    fun convertStringToEnum(type: String?): ElementCacheType {
      for (value in ElementCacheType.values()) {
        if (value.type.equals(type, true)) {
          return value
        }
      }
      return NONE
    }
  }
}
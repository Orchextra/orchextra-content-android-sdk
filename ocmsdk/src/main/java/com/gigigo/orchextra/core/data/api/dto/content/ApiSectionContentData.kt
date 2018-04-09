package com.gigigo.orchextra.core.data.api.dto.content

import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCache

class ApiSectionContentData {
  val content: ApiContentItem? = null
  val elementsCache: Map<String, ApiElementCache>? = null
  val version: String? = null
  val expireAt: String? = null
  var isFromCloud: Boolean = false
}

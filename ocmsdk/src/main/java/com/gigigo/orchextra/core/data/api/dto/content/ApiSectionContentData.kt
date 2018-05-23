package com.gigigo.orchextra.core.data.api.dto.content

import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCache
import com.mskn73.kache.Kacheable

//@KacheLife(expiresTime = 1000 * 60 * 60 * 24)  // 1 day
class ApiSectionContentData : Kacheable {
  val content: ApiContentItem? = null
  val elementsCache: Map<String, ApiElementCache>? = null
  override var key: String = ""
  val version: String? = null
  val expireAt: String? = null
  var isFromCloud: Boolean = false
}

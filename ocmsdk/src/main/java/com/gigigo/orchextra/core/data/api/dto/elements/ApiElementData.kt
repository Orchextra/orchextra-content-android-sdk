package com.gigigo.orchextra.core.data.api.dto.elements

import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCache
import com.mskn73.kache.Kacheable

//@KacheLife(expiresTime = 1000 * 60 * 60 * 24) // 1 day
class ApiElementData(val element: ApiElementCache) : Kacheable {

  override val key: String
    get() = element.slug
}

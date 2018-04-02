package com.gigigo.orchextra.core.data.api.dto.elements

import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCache
import com.mskn73.kache.Kacheable

class ApiElementData(val element: ApiElementCache) : Kacheable {

  override val key: String
    get() = element.slug
}

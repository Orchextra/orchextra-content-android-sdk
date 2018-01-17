package com.gigigo.orchextra.core.data.api.dto.versioning

import com.gigigo.orchextra.core.data.rxCache.OcmCacheImp
import com.mskn73.kache.Kacheable
import com.mskn73.kache.annotations.KacheLife

@KacheLife(expiresTime = (1000 * 5).toLong()) // seconds
class ApiVersionKache(val version: String) : Kacheable {

  override val key: String
    get() = OcmCacheImp.VERSION_KEY
}
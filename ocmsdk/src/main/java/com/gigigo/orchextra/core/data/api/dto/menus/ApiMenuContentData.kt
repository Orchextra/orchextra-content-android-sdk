package com.gigigo.orchextra.core.data.api.dto.menus

import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCache
import com.gigigo.orchextra.core.data.rxCache.OcmCacheImp
import com.google.gson.annotations.SerializedName
import com.mskn73.kache.Kacheable

//@KacheLife(expiresTime = 1000 * 60 * 60 * 24) // 1 day
class ApiMenuContentData(
    var isFromCloud: Boolean = false,
    @SerializedName("menus") val menuContentList: List<ApiMenuContent>?,
    @SerializedName("elementsCache") val elementsCache: Map<String, ApiElementCache>?
) : Kacheable {
  override val key: String
    get() = OcmCacheImp.MENU_KEY

}
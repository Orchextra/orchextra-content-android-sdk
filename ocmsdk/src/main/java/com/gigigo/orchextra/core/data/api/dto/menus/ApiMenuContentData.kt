package com.gigigo.orchextra.core.data.api.dto.menus

import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCache
import com.google.gson.annotations.SerializedName

class ApiMenuContentData(
    var isFromCloud: Boolean = false,
    @SerializedName("menus") val menuContentList: List<ApiMenuContent>?,
    @SerializedName("elementsCache") val elementsCache: Map<String, ApiElementCache>?
)
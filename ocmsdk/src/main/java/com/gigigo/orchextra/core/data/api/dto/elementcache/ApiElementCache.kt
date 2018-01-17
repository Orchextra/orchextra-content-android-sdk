package com.gigigo.orchextra.core.data.api.dto.elementcache

import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementSegmentation

class ApiElementCache(
    val slug: String,
    val type: String? = null,
    val tags: List<String>? = null,
    val preview: ApiElementCachePreview? = null,
    val render: ApiElementCacheRender? = null,
    val share: ApiElementCacheShare? = null,
    val segmentation: ApiElementSegmentation? = null,
    val name: String? = null,
    var updatedAt: Long = 0)
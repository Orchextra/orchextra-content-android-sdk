package com.gigigo.orchextra.core.data.api.dto.elementcache

class ApiElementCache(
    val slug: String,
    val type: String? = null,
    val tags: List<String>? = null,
    val preview: ApiElementCachePreview? = null,
    val render: ApiElementCacheRender? = null,
    val share: ApiElementCacheShare? = null,
    val customProperties: Map<String, Any>  = emptyMap(),
    val name: String? = null,
    var updatedAt: Long = 0) {
    companion object {
        fun empty() = ApiElementCache("")
    }
}

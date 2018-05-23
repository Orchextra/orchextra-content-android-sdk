package com.gigigo.orchextra.core.data.api.dto.elementcache

import com.gigigo.orchextra.core.data.api.dto.article.ApiArticleElement

class ApiElementCacheRender(
    val contentUrl: String?,
    val url: String?,
    val title: String?,
    val elements: List<ApiArticleElement>?,
    val schemeUri: String?,
    val source: String?,
    val format: String?,
    var federatedAuth: FederatedAuthorizationData?)
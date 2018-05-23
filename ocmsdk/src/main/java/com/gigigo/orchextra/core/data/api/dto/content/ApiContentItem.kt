package com.gigigo.orchextra.core.data.api.dto.content

import com.gigigo.orchextra.core.data.api.dto.elements.ApiElement

class ApiContentItem(
    val slug: String,
    val type: String?,
    val tags: List<String>?,
    val layout: ApiContentItemLayout?,
    val elements: List<ApiElement>?)
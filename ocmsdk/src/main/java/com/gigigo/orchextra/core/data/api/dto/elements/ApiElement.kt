package com.gigigo.orchextra.core.data.api.dto.elements

class ApiElement(
    val tags: List<String>?,
    val customProperties: Map<String, Any>?,
    val sectionView: ApiElementSectionView?,
    val slug: String,
    val elementUrl: String?,
    val name: String?,
    val dates: List<List<String>>?)

package com.gigigo.orchextra.core.data.api.dto.menus

import com.gigigo.orchextra.core.data.api.dto.elements.ApiElement
import com.google.gson.annotations.SerializedName

class ApiMenuContent(
    @SerializedName("_id") val id: String?,
    @SerializedName("slug") val slug: String,
    @SerializedName("elements") val elements: List<ApiElement>?)
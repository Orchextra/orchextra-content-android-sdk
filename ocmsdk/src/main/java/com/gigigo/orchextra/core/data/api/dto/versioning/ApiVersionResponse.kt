package com.gigigo.orchextra.core.data.api.dto.versioning

import com.google.gson.annotations.SerializedName

class ApiVersionResponse(
    @SerializedName("status") val status: Boolean?,
    @SerializedName("data") val data: String?)
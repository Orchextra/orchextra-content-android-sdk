package com.gigigo.orchextra.core.data.api.dto.base

import com.google.gson.annotations.SerializedName

class ApiErrorResponse(
    @field:SerializedName("code") var code: Int,
    @field:SerializedName("message") var message: String?)
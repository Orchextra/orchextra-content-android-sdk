package com.gigigo.showcase.domain.entity

data class ConfigData(
    val apiKey: String? = "",
    val apiSecret: String? = "",
    val businessUnit: String? = "") {

  fun isValid(): Boolean = !apiKey.isNullOrEmpty() && !apiSecret.isNullOrEmpty()
}
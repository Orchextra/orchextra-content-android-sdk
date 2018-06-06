package com.gigigo.orchextra.wrapper

import android.app.Activity

data class OxConfig(
    val apiKey: String,
    val apiSecret: String,
    val firebaseApiKey: String = "",
    val firebaseApplicationId: String = "",
    val deviceBusinessUnits: List<String> = arrayListOf(),
    val notificationActivityClass: Class<Activity>? = null,
    val triggeringEnabled: Boolean = true,
    val anonymous: Boolean = false)
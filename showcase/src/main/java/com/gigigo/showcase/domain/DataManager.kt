package com.gigigo.showcase.domain

import android.content.SharedPreferences
import com.gigigo.showcase.domain.entity.ConfigData


class DataManager(private val preferences: SharedPreferences) {

  fun getConfigData(): ConfigData {
    return ConfigData(
        apiKey = preferences.getString(API_KEY_KEY, DEFAULT_API_KEY),
        apiSecret = preferences.getString(API_SECRET_KEY, DEFAULT_API_SECRET),
        businessUnit = preferences.getString(BUSINESS_UNIT_KEY, DEFAULT_BUSINESS_UNIT))
  }

  fun saveConfigData(data: ConfigData): Boolean {
    val editor = preferences.edit()

    editor.putString(API_KEY_KEY, data.apiKey)
    editor.putString(API_SECRET_KEY, data.apiSecret)
    editor.putString(BUSINESS_UNIT_KEY, data.businessUnit)

    return editor.commit()
  }

  companion object {
    private const val DEFAULT_API_KEY = "8286702045adf5a3ad816f70ecb80e4c91fbb8de"
    private const val DEFAULT_API_SECRET = "eab37080130215ced60eb9d5ff729049749ec205"
    private const val DEFAULT_BUSINESS_UNIT = "oat-it"

    private const val API_KEY_KEY = "API_KEY_KEY"
    private const val API_SECRET_KEY = "API_SECRET_KEY"
    private const val BUSINESS_UNIT_KEY = "BUSINESS_UNIT_KEY"
  }
}
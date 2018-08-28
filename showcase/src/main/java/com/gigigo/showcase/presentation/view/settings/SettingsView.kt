package com.gigigo.showcase.presentation.view.settings

import com.gigigo.showcase.domain.entity.ConfigData

interface SettingsView {

  fun showLoading()

  fun showConfigData(configData: ConfigData)
}
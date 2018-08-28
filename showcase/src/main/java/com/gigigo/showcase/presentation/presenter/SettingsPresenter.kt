package com.gigigo.showcase.presentation.presenter

import com.gigigo.showcase.domain.DataManager
import com.gigigo.showcase.domain.entity.ConfigData
import com.gigigo.showcase.presentation.view.settings.SettingsView

class SettingsPresenter(private val dataManager: DataManager) : Presenter<SettingsView> {

  private lateinit var view: SettingsView

  override fun attachView(view: SettingsView, isNew: Boolean) {
    this.view = view

    view.showConfigData(dataManager.getConfigData())
  }

  fun onStartClick(configData: ConfigData, customFields: Map<String, String>) {
    dataManager.saveConfigData(configData)

    val userData = dataManager.getUserData().copy(
        type = customFields.get("type"),
        level = customFields.get("level"))
    dataManager.saveUserData(userData)
    view.showNewProject()
  }

  override fun detachView() {

  }
}
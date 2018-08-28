package com.gigigo.showcase.presentation.presenter

import com.gigigo.showcase.domain.DataManager
import com.gigigo.showcase.domain.entity.ConfigData
import com.gigigo.showcase.ocm.ContentManager
import com.gigigo.showcase.presentation.view.settings.SettingsView

class SettingsPresenter(private val dataManager: DataManager,
    private val contentManager: ContentManager) : Presenter<SettingsView> {

  private lateinit var view: SettingsView

  override fun attachView(view: SettingsView, isNew: Boolean) {
    this.view = view

    view.showConfigData(dataManager.getConfigData())
  }

  fun onStartClick(configData: ConfigData) {
    dataManager.saveConfigData(configData)
    view.showNewProject()
  }

  override fun detachView() {

  }
}
package com.gigigo.showcase.presentation.presenter

import com.gigigo.showcase.presentation.view.settings.SettingsView

class SettingsPresenter : Presenter<SettingsView> {

  private lateinit var view: SettingsView

  override fun attachView(view: SettingsView, isNew: Boolean) {
    this.view = view


  }

  fun onStartClick() {

  }

  override fun detachView() {

  }
}
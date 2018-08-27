package com.gigigo.showcase.presentation.presenter

import com.gigigo.showcase.ocm.ContentManager
import com.gigigo.showcase.presentation.view.main.MainView
import com.gigigo.showcase.presentation.view.settings.ProjectData

class MainPresenter(private val contentManager: ContentManager) : Presenter<MainView> {

  private lateinit var view: MainView

  override fun attachView(view: MainView, isNew: Boolean) {
    this.view = view

    view.showLoading()
    initOcm {
      getContent()
    }
  }

  private fun initOcm(callback: () -> Unit) {
    contentManager.init(ProjectData.getDefaultApiKey(), ProjectData.getDefaultApiSecret(), "",
        callback) {
      view.showErrorView()
    }
  }

  private fun getContent() {
    view.showEmptyView()
  }

  override fun detachView() {

  }
}
package com.gigigo.showcase.presentation.presenter

import com.gigigo.orchextra.core.domain.entities.menus.DataRequest
import com.gigigo.orchextra.ocm.Ocm
import com.gigigo.orchextra.ocm.OcmCallbacks
import com.gigigo.orchextra.ocm.dto.UiMenuData
import com.gigigo.showcase.ocm.ContentManager
import com.gigigo.showcase.presentation.view.main.MainView
import com.gigigo.showcase.presentation.view.settings.ProjectData
import timber.log.Timber

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
    contentManager.init(ProjectData.getDefaultApiKey(), ProjectData.getDefaultApiSecret(), "oat-it",
        callback) {
      view.showErrorView()
    }
  }

  private fun getContent() {
    Ocm.getMenus(DataRequest.FORCE_CLOUD, object : OcmCallbacks.Menus {
      override fun onMenusLoaded(uiMenuData: UiMenuData?) {

        if (uiMenuData == null || uiMenuData.uiMenuList.isEmpty()) {
          view.showEmptyView()
        } else {
          view.showContentView(uiMenuData.uiMenuList)
        }
      }

      override fun onMenusFails(throwable: Throwable) {
        Timber.e(throwable, "getMenus()")
        view.showErrorView()
      }
    })
  }

  override fun detachView() {

  }
}
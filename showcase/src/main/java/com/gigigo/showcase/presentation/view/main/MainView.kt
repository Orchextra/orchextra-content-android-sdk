package com.gigigo.showcase.presentation.view.main

import com.gigigo.orchextra.ocm.dto.UiMenu

interface MainView {

  fun showLoading()

  fun showEmptyView()

  fun showErrorView()

  fun showContentView(uiMenuList: List<UiMenu>)

  fun showNetworkErrorView()
}
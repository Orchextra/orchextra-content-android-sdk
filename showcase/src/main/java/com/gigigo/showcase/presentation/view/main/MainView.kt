package com.gigigo.showcase.presentation.view.main

interface MainView {

  fun showLoading()

  fun showEmptyView()

  fun showErrorView()

  fun showContentView()

  fun showNetworkErrorView()
}
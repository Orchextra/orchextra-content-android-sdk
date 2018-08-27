package com.gigigo.showcase.presentation.presenter

interface Presenter<V> {

  fun attachView(view: V, isNew: Boolean)

  fun detachView()
}
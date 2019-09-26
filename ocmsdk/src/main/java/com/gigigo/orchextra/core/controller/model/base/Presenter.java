package com.gigigo.orchextra.core.controller.model.base;


public abstract class Presenter<UiView> {

  private UiView uiView;

  public Presenter() {

  }

  public void attachView(UiView view) {
    this.uiView = view;
    onViewAttached();
  }

  public void detachView() {
    this.uiView = null;
  }

  public UiView getView() {
    return uiView;
  }

  public abstract void onViewAttached();
}

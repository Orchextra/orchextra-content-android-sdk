package com.gigigo.orchextra.core.controller.model.base;

/**
 * Created by alex on 01/12/2017.
 */

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

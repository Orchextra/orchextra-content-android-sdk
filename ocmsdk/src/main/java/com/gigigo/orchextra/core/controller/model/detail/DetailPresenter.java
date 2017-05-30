package com.gigigo.orchextra.core.controller.model.detail;

import com.gigigo.interactorexecutor.base.Presenter;
import com.gigigo.interactorexecutor.base.viewinjector.GenericViewInjector;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.callbacks.OnFinishViewListener;
import com.gigigo.orchextra.ocm.views.UiDetailBaseContentData;

public class DetailPresenter extends Presenter<DetailView> {

  private OnFinishViewListener onFinishViewListener;

  public DetailPresenter(GenericViewInjector viewInjector) {
    super(viewInjector);
  }

  @Override public void onViewAttached() {
    getView().initUi();
  }

  public void loadSection(String elementUrl) {
    UiDetailBaseContentData contentDetailView = Ocm.generateDetailView(elementUrl);

    if (contentDetailView != null) {
      contentDetailView.setOnFinishListener(onFinishViewListener);
      getView().setView(contentDetailView);
    } else {
      getView().showError();
    }
  }

  public void setOnFinishViewListener(OnFinishViewListener onFinishViewListener) {
    this.onFinishViewListener = onFinishViewListener;
  }
}

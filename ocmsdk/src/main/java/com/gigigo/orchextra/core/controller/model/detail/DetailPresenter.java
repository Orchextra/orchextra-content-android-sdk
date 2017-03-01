package com.gigigo.orchextra.core.controller.model.detail;

import com.gigigo.interactorexecutor.base.Presenter;
import com.gigigo.interactorexecutor.base.viewinjector.GenericViewInjector;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.views.UiDetailBaseContentData;

public class DetailPresenter extends Presenter<DetailView> {

  public DetailPresenter(GenericViewInjector viewInjector) {
    super(viewInjector);
  }

  @Override public void onViewAttached() {
    getView().initUi();
  }

  public void loadSection(String elementUrl) {
    UiDetailBaseContentData contentDetailView = Ocm.generateDetailView(elementUrl);

    if (contentDetailView != null) {
      contentDetailView.setOnFinishListener(new UiDetailBaseContentData.OnFinishViewListener() {
        @Override public void onFinish() {
          getView().finishView();
        }
      });
      getView().setView(contentDetailView);
    } else {
      getView().showError();
    }
  }
}

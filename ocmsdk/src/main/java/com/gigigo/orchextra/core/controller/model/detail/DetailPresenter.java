package com.gigigo.orchextra.core.controller.model.detail;

import com.gigigo.orchextra.core.controller.model.base.Presenter;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.callbacks.OcmCredentialCallback;
import com.gigigo.orchextra.ocm.callbacks.OnFinishViewListener;
import com.gigigo.orchextra.ocm.views.UiDetailBaseContentData;
import com.gigigo.orchextra.wrapper.CrmUser;

public class DetailPresenter extends Presenter<DetailView> {

  private OnFinishViewListener onFinishViewListener;
  private String accessToken;

  public DetailPresenter() {

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

  public void setLoginUserFromNativeLogin(String userId) {

    CrmUser crmUser = new CrmUser(userId, null, null);
    Ocm.bindUser(crmUser);
    Ocm.setUserIsAuthorizated(true);
    Ocm.start(new OcmCredentialCallback() {
      @Override public void onCredentialReceiver(String accessToken) {
        if (!accessToken.equals(DetailPresenter.this.accessToken)) {
          DetailPresenter.this.accessToken = accessToken;
          getView().redirectToAction();
        }
      }

      @Override public void onCredentailError(String code) {

      }
    });
  }
}

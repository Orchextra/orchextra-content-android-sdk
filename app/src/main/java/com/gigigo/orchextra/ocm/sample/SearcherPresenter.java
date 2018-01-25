package com.gigigo.orchextra.ocm.sample;

import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.views.UiSearchBaseContentData;

public class SearcherPresenter {

  private SearcherActivity view;
  private UiSearchBaseContentData uiSearchBaseContentData;


  public void onViewAttached() {
    view.initUi();
  }

  public void loadView() {
    uiSearchBaseContentData = Ocm.generateSearchView();
    view.setView(uiSearchBaseContentData);
  }

  public void doSearch(String textToSearch) {
    if (uiSearchBaseContentData != null) {

      uiSearchBaseContentData.doSearch(textToSearch);
    }
  }

  public void attachView(SearcherActivity activity) {
    this.view = activity;
    onViewAttached();
  }
}

package com.gigigo.orchextra.core.domain.interactors.searcher;

import com.gigigo.interactorexecutor.interactors.Interactor;
import com.gigigo.interactorexecutor.interactors.InteractorResponse;
import com.gigigo.interactorexecutor.responses.BusinessObject;
import com.gigigo.orchextra.core.domain.data.SearcherNetworkDataSource;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.interactors.errors.GenericResponseDataError;
import com.gigigo.orchextra.core.domain.interactors.errors.NoNetworkConnectionError;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;

public class SearchTextInteractor implements Interactor<InteractorResponse<ContentData>> {

  private final ConnectionUtils connectionUtils;
  private final SearcherNetworkDataSource searcherNetworkDataSource;

  private String textToSearch;

  public SearchTextInteractor(ConnectionUtils connectionUtils,
      SearcherNetworkDataSource searcherNetworkDataSource) {
    this.connectionUtils = connectionUtils;
    this.searcherNetworkDataSource = searcherNetworkDataSource;
  }

  @Override public InteractorResponse<ContentData> call() throws Exception {
    if (!connectionUtils.hasConnection()) {
      return new InteractorResponse(new NoNetworkConnectionError());
    }

    BusinessObject<ContentData> boContentData = searcherNetworkDataSource.doSearch(textToSearch);

    if (!boContentData.isSuccess()) {
      return new InteractorResponse<>(new GenericResponseDataError(boContentData.getBusinessError()));
    }

    return new InteractorResponse<>(boContentData.getData());
  }

  public void setTextToSearch(String textToSearch) {
    this.textToSearch = textToSearch;
  }
}

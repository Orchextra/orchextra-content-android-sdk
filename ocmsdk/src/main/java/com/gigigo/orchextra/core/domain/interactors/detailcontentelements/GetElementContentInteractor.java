package com.gigigo.orchextra.core.domain.interactors.detailcontentelements;

import com.gigigo.interactorexecutor.interactors.Interactor;
import com.gigigo.interactorexecutor.interactors.InteractorResponse;
import com.gigigo.orchextra.core.domain.data.DataBaseDataSource;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItem;

public class GetElementContentInteractor implements Interactor<InteractorResponse<ContentItem>> {

  private final DataBaseDataSource dataBaseDataSource;

  private String section;

  public GetElementContentInteractor(DataBaseDataSource dataBaseDataSource) {
    this.dataBaseDataSource = dataBaseDataSource;
  }

  @Override public InteractorResponse<ContentItem> call() throws Exception {
    ContentItem contentItem = dataBaseDataSource.retrieveDetailContentData(section);
    return new InteractorResponse<>(contentItem);

    //TODO Retrieve item from server when item can't be found in the database
  }

  public void setSection(String section) {
    this.section = section;
  }
}

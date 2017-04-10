package com.gigigo.orchextra.core.domain.interactors.elements;

import com.gigigo.interactorexecutor.interactors.Interactor;
import com.gigigo.interactorexecutor.interactors.InteractorResponse;
import com.gigigo.orchextra.core.domain.data.DataBaseDataSource;

public class ClearElementsInteractor implements Interactor<InteractorResponse<Boolean>> {

  private final DataBaseDataSource dataBaseDataSource;

  public ClearElementsInteractor(DataBaseDataSource dataBaseDataSource) {
    this.dataBaseDataSource = dataBaseDataSource;
  }

  @Override public InteractorResponse<Boolean> call() throws Exception {
    dataBaseDataSource.clearElements();
    return new InteractorResponse<>(true);
  }
}

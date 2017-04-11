package com.gigigo.orchextra.core.domain.interactors.home;

import com.gigigo.interactorexecutor.interactors.Interactor;
import com.gigigo.interactorexecutor.interactors.InteractorResponse;
import com.gigigo.orchextra.core.domain.data.DataBaseDataSource;

public class ClearMenuDataInteractor implements Interactor<InteractorResponse<Boolean>> {

  private final DataBaseDataSource dataBaseDataSource;

  public ClearMenuDataInteractor(DataBaseDataSource dataBaseDataSource) {
    this.dataBaseDataSource = dataBaseDataSource;
  }

  @Override public InteractorResponse<Boolean> call() throws Exception {
    dataBaseDataSource.clearMenu();
    return new InteractorResponse<>(true);
  }
}

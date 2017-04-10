package com.gigigo.orchextra.core.domain.interactors.elements;

import com.gigigo.interactorexecutor.interactors.Interactor;
import com.gigigo.interactorexecutor.interactors.InteractorResponse;
import com.gigigo.orchextra.core.domain.data.DataBaseDataSource;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;

public class SaveElementByIdInteractor implements Interactor<InteractorResponse<Boolean>> {

  private final DataBaseDataSource dataBaseDataSource;
  private String id;
  private ElementCache element;

  public SaveElementByIdInteractor(DataBaseDataSource dataBaseDataSource) {
    this.dataBaseDataSource = dataBaseDataSource;
  }

  @Override public InteractorResponse<Boolean> call() throws Exception {
    if (id != null && !id.isEmpty() && element != null) {
      dataBaseDataSource.saveElementWithId(id, element);
    }
    return new InteractorResponse<>(true);
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setElement(ElementCache element) {
    this.element = element;
  }
}

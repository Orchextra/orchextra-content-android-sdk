package com.gigigo.orchextra.core.domain.interactors.elements;

import com.gigigo.interactorexecutor.interactors.Interactor;
import com.gigigo.interactorexecutor.interactors.InteractorResponse;
import com.gigigo.interactorexecutor.responses.BusinessObject;
import com.gigigo.orchextra.core.domain.data.DataBaseDataSource;
import com.gigigo.orchextra.core.domain.data.ElementNetworkDataSource;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.elements.ElementData;
import com.gigigo.orchextra.core.domain.interactors.errors.GenericResponseDataError;
import com.gigigo.orchextra.core.domain.interactors.errors.NoNetworkConnectionError;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;

public class GetElementByIdInteractor implements Interactor<InteractorResponse<ElementCache>> {

  private final ConnectionUtils connectionUtils;
  private final ElementNetworkDataSource elementNetworkDataSource;
  private final DataBaseDataSource dataBaseDataSource;

  private String elementId;

  public GetElementByIdInteractor(ConnectionUtils connectionUtils,
      ElementNetworkDataSource elementNetworkDataSource, DataBaseDataSource dataBaseDataSource) {
    this.connectionUtils = connectionUtils;
    this.elementNetworkDataSource = elementNetworkDataSource;
    this.dataBaseDataSource = dataBaseDataSource;
  }

  @Override public InteractorResponse<ElementCache> call() throws Exception {
    ElementCache elementCache = dataBaseDataSource.retrieveElementById(elementId);

    if (elementCache != null
        && elementCache.getPreview() != null      // Checks if the element
        && elementCache.getRender() != null) {    // cache is musty
      return new InteractorResponse<>(elementCache);
    }

    return getFromNetwork();
  }

  private InteractorResponse<ElementCache> getFromNetwork() {
    if (connectionUtils.hasConnection()) {

      BusinessObject<ElementData> boElement = elementNetworkDataSource.getElementById(elementId);

      ElementData data = boElement.getData();
      if (boElement.isSuccess() && data != null) {
        dataBaseDataSource.saveElementWithId(elementId, data.getElement());
        return new InteractorResponse<>(data.getElement());
      } else {
        return new InteractorResponse<>(new GenericResponseDataError(boElement.getBusinessError()));
      }
    } else {
      return new InteractorResponse(new NoNetworkConnectionError());
    }
  }

  public void setElementId(String elementId) {
    this.elementId = elementId;
  }
}

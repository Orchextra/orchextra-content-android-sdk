package com.gigigo.orchextra.core.domain.interactors.elements;

import com.gigigo.interactorexecutor.interactors.Interactor;
import com.gigigo.interactorexecutor.interactors.InteractorResponse;
import com.gigigo.interactorexecutor.responses.BusinessObject;
import com.gigigo.orchextra.core.domain.data.ElementNetworkDataSource;

import com.gigigo.orchextra.core.domain.entities.elements.ElementData;
import com.gigigo.orchextra.core.domain.interactors.errors.GenericResponseDataError;
import com.gigigo.orchextra.core.domain.interactors.errors.NoNetworkConnectionError;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;

public class GetElementByIdInteractor implements Interactor<InteractorResponse<ElementData>> {

  private final ConnectionUtils connectionUtils;
  private final ElementNetworkDataSource elementNetworkDataSource;

  private String elementId;

  public GetElementByIdInteractor(ConnectionUtils connectionUtils,
      ElementNetworkDataSource elementNetworkDataSource) {
    this.connectionUtils = connectionUtils;
    this.elementNetworkDataSource = elementNetworkDataSource;
  }

  @Override public InteractorResponse<ElementData> call() throws Exception {
    if (connectionUtils.hasConnection()) {

      BusinessObject<ElementData> boElement = elementNetworkDataSource.getElementById(elementId);

      if (boElement.isSuccess()) {
        return new InteractorResponse<>(boElement.getData());
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

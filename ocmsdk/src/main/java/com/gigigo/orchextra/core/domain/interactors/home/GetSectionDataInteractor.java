package com.gigigo.orchextra.core.domain.interactors.home;

import com.gigigo.interactorexecutor.interactors.Interactor;
import com.gigigo.interactorexecutor.interactors.InteractorResponse;

import com.gigigo.interactorexecutor.responses.BusinessError;
import com.gigigo.interactorexecutor.responses.BusinessObject;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.data.SectionNetworkDataSource;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItem;
import com.gigigo.orchextra.core.domain.interactors.errors.GenericResponseDataError;
import com.gigigo.orchextra.core.domain.interactors.errors.NoNetworkConnectionError;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;

/**
 * Created by rui.alonso on 20/9/16.
 */
public class GetSectionDataInteractor implements Interactor<InteractorResponse<ContentItem>> {

  private final ConnectionUtils connectionUtils;
  private final SectionNetworkDataSource sectionNetworkDataSource;
  private final OcmController ocmController;

  private String section;
  private boolean useCache;

  public GetSectionDataInteractor(ConnectionUtils connectionUtils,
      SectionNetworkDataSource sectionNetworkDataSource,
      OcmController ocmController) {
    this.connectionUtils = connectionUtils;
    this.sectionNetworkDataSource = sectionNetworkDataSource;
    this.ocmController = ocmController;
  }

  @Override public InteractorResponse<ContentItem> call() throws Exception {

    if (useCache) {
      ContentItem contentItem = ocmController.getSectionContentById(section);
      if (contentItem != null) {
        return new InteractorResponse<>(contentItem);
      }
    }

    InteractorResponse<ContentData> boContentData = sendRequestToServer();

    if (!boContentData.hasError()) {
      ocmController.saveSectionContentData(section, boContentData.getResult());
    }

    if (boContentData.getResult() != null) {
      return new InteractorResponse<>(boContentData.getResult().getContent());
    } else {
      return new InteractorResponse<>(boContentData.getError());
    }
  }

  private InteractorResponse<ContentData> sendRequestToServer() {
    if (connectionUtils.hasConnection()) {

      String contentUrl = ocmController.getContentUrlBySection(section);

      if (contentUrl == null) {
        return new InteractorResponse<>(
            new GenericResponseDataError(BusinessError.createKOInstance("")));
      }

      BusinessObject<ContentData> boContentData =
          sectionNetworkDataSource.getSectionData(contentUrl);

      if (boContentData.isSuccess()) {
        return new InteractorResponse<>(boContentData.getData());
      } else {
        return new InteractorResponse<>(
            new GenericResponseDataError(boContentData.getBusinessError()));
      }
    } else {
      return new InteractorResponse(new NoNetworkConnectionError());
    }
  }

  public void setSection(String section) {
    this.section = section;
  }

  public void setUseCache(boolean useCache) {
    this.useCache = useCache;
  }
}

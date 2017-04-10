package com.gigigo.orchextra.core.domain.invocators;

import com.gigigo.interactorexecutor.base.invoker.InteractorExecution;
import com.gigigo.interactorexecutor.base.invoker.InteractorInvoker;
import com.gigigo.interactorexecutor.base.invoker.InteractorResult;
import com.gigigo.orchextra.core.domain.data.DataBaseDataSource;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItem;
import com.gigigo.orchextra.core.domain.interactors.detailcontentelements.GetElementContentInteractor;
import com.gigigo.orchextra.core.domain.interactors.errors.GenericResponseDataError;
import com.gigigo.orchextra.core.domain.interactors.errors.NoNetworkConnectionError;
import java.util.concurrent.CountDownLatch;

public class DetailContentElementInteractorInvocator {

  private final GetElementContentInteractor getElementContentInteractor;
  private final InteractorInvoker interactorInvoker;
  private final DataBaseDataSource dataBaseDataSource;

  private ContentItem contentItem;

  public DetailContentElementInteractorInvocator(InteractorInvoker interactorInvoker,
      GetElementContentInteractor getElementContentInteractor, DataBaseDataSource dataBaseDataSource) {

    this.interactorInvoker = interactorInvoker;
    this.getElementContentInteractor = getElementContentInteractor;
    this.dataBaseDataSource = dataBaseDataSource;
  }

  public ContentItem getDetailSectionContentBySection(String section) {
    contentItem = null;

    getDetailSectionContentByIdInBackground(section);

    return contentItem;
  }

  private void getDetailSectionContentByIdInBackground(final String section) {
    try {

      final CountDownLatch countDownLatch = new CountDownLatch(1);

      getElementContentInteractor.setSection(section);

      new InteractorExecution<>(getElementContentInteractor).result(
          new InteractorResult<ContentItem>() {
            @Override public void onResult(ContentItem result) {
              dataBaseDataSource.saveDetailElementContentItemBySection(section, result);
              contentItem = result;
              countDownLatch.countDown();
            }
          })
          .error(NoNetworkConnectionError.class, new InteractorResult<NoNetworkConnectionError>() {
            @Override public void onResult(NoNetworkConnectionError interactorError) {
              contentItem = null;
              countDownLatch.countDown();
            }
          })
          .error(GenericResponseDataError.class, new InteractorResult<GenericResponseDataError>() {
            @Override public void onResult(GenericResponseDataError interactorError) {
              contentItem = null;
              countDownLatch.countDown();
            }
          })
          .execute(interactorInvoker);

      countDownLatch.await();
    } catch (Exception e) {
      contentItem = null;
    }
  }

  public void saveDetailSectionContentBySection(String section, ContentItem contentItem) {
    dataBaseDataSource.saveDetailElementContentItemBySection(section, contentItem);
  }

  public void clear() {
    dataBaseDataSource.clearDetailElements();
  }
}

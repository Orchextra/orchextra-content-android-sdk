package com.gigigo.orchextra.core.domain.invocators;

import com.gigigo.interactorexecutor.base.invoker.InteractorExecution;
import com.gigigo.interactorexecutor.base.invoker.InteractorInvoker;
import com.gigigo.interactorexecutor.base.invoker.InteractorResult;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.interactors.elements.ClearElementsInteractor;
import com.gigigo.orchextra.core.domain.interactors.elements.GetElementByIdInteractor;
import com.gigigo.orchextra.core.domain.interactors.elements.SaveElementByIdInteractor;
import com.gigigo.orchextra.core.domain.interactors.errors.GenericResponseDataError;
import com.gigigo.orchextra.core.domain.interactors.errors.NoNetworkConnectionError;
import java.util.concurrent.CountDownLatch;

public class GridElementsInteractorInvocator {

  private final InteractorInvoker interactorInvoker;
  private final GetElementByIdInteractor getElementByIdInteractor;
  private final ClearElementsInteractor clearElementsInteractor;
  private final SaveElementByIdInteractor saveElementByIdInteractor;

  private ElementCache elementCache;

  public GridElementsInteractorInvocator(InteractorInvoker interactorInvoker,
      GetElementByIdInteractor getElementByIdInteractor,
      SaveElementByIdInteractor saveElementByIdInteractor,
      ClearElementsInteractor clearElementsInteractor) {

    this.interactorInvoker = interactorInvoker;
    this.getElementByIdInteractor = getElementByIdInteractor;
    this.saveElementByIdInteractor = saveElementByIdInteractor;
    this.clearElementsInteractor = clearElementsInteractor;
  }

  public ElementCache getElementById(String slug) {

    //asv wtf?? este pseudo await funca??
    elementCache = null;

    getElementByIdInBackground(slug);

    return elementCache;
  }

  private void getElementByIdInBackground(String slug) {
    try {
System.out.println("getElementByIdInBackground\n\n");
      final CountDownLatch countDownLatch = new CountDownLatch(1);

      getElementByIdInteractor.setElementId(slug);

      new InteractorExecution<>(getElementByIdInteractor).result(
          new InteractorResult<ElementCache>() {
            @Override public void onResult(ElementCache result) {
              elementCache = result;
              countDownLatch.countDown();
            }
          })
          .error(NoNetworkConnectionError.class, new InteractorResult<NoNetworkConnectionError>() {
            @Override public void onResult(NoNetworkConnectionError interactorError) {
              elementCache = null;
              countDownLatch.countDown();
            }
          })
          .error(GenericResponseDataError.class, new InteractorResult<GenericResponseDataError>() {
            @Override public void onResult(GenericResponseDataError interactorError) {
              elementCache = null;
              countDownLatch.countDown();
            }
          })
          .execute(interactorInvoker);

      countDownLatch.await();
    } catch (Exception e) {
      elementCache = null;
    }
  }

  public void clear() {
    new InteractorExecution<>(clearElementsInteractor).execute(interactorInvoker);
  }

  public void saveElementById(String key, ElementCache elementCache) {
    saveElementByIdInteractor.setId(key);
    saveElementByIdInteractor.setElement(elementCache);
    new InteractorExecution<>(saveElementByIdInteractor).execute(interactorInvoker);
  }
}

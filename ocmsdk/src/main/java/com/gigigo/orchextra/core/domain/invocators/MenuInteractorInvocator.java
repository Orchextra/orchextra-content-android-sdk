package com.gigigo.orchextra.core.domain.invocators;

import com.gigigo.interactorexecutor.base.invoker.InteractorExecution;
import com.gigigo.interactorexecutor.base.invoker.InteractorInvoker;
import com.gigigo.interactorexecutor.base.invoker.InteractorResult;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import com.gigigo.orchextra.core.domain.interactors.errors.GenericResponseDataError;
import com.gigigo.orchextra.core.domain.interactors.home.ClearMenuDataInteractor;
import com.gigigo.orchextra.core.domain.interactors.home.GetMenuDataInteractor;
import java.util.concurrent.CountDownLatch;

public class MenuInteractorInvocator {

  private final InteractorInvoker interactorInvoker;
  private final GetMenuDataInteractor getMenuDataInteractor;
  private final ClearMenuDataInteractor clearMenuDataInteractor;

  private MenuContentData result;

  public MenuInteractorInvocator(InteractorInvoker interactorInvoker,
      GetMenuDataInteractor getMenuDataInteractor,
      ClearMenuDataInteractor clearMenuDataInteractor) {

    this.interactorInvoker = interactorInvoker;
    this.getMenuDataInteractor = getMenuDataInteractor;
    this.clearMenuDataInteractor = clearMenuDataInteractor;
  }

  public MenuContentData getMenu(boolean useCache) {
    result = null;

    getMenuInBackground(useCache);

    return result;
  }

  private void getMenuInBackground(boolean useCache) {
    try {

      final CountDownLatch countDownLatch = new CountDownLatch(1);

      getMenuDataInteractor.setUseCache(useCache);

      new InteractorExecution<>(getMenuDataInteractor).result(
          new InteractorResult<MenuContentData>() {
            @Override public void onResult(MenuContentData menuContentData) {
              result = menuContentData;
              countDownLatch.countDown();
            }
          })
          .error(GenericResponseDataError.class, new InteractorResult<GenericResponseDataError>() {
            @Override public void onResult(GenericResponseDataError error) {
              result = null;
              countDownLatch.countDown();
            }
          })
          .execute(interactorInvoker);

      countDownLatch.await();
    } catch (Exception e) {
      result = null;
    }
  }

  public void clear() {
    new InteractorExecution<>(clearMenuDataInteractor).execute(interactorInvoker);
  }
}

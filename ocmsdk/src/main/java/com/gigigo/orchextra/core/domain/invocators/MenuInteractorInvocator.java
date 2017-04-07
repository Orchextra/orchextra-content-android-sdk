package com.gigigo.orchextra.core.domain.invocators;

import com.gigigo.interactorexecutor.base.invoker.InteractorExecution;
import com.gigigo.interactorexecutor.base.invoker.InteractorInvoker;
import com.gigigo.interactorexecutor.interactors.InteractorResponse;
import com.gigigo.orchextra.core.domain.OnRetrieveMenuListener;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import com.gigigo.orchextra.core.domain.interactors.home.ClearMenuDataInteractor;
import com.gigigo.orchextra.core.domain.interactors.home.GetMenuDataInteractor;
import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import com.gigigo.threaddecoratedview.views.ThreadSpec;
import java.util.concurrent.CountDownLatch;

public class MenuInteractorInvocator {

  private final InteractorInvoker interactorInvoker;
  private final GetMenuDataInteractor getMenuDataInteractor;
  private final ThreadSpec threadSpec;
  private final ClearMenuDataInteractor clearMenuDataInteractor;
  private final OcmContextProvider contextProvider;

  private OnRetrieveMenuListener onRetrieveMenuListener;

  final CountDownLatch countDownLatch = new CountDownLatch(1);
  private final MenuContentData[] result = new MenuContentData[1];

  public MenuInteractorInvocator(OcmContextProvider contextProvider, ThreadSpec threadSpec, InteractorInvoker interactorInvoker,
      GetMenuDataInteractor getMenuDataInteractor,
      ClearMenuDataInteractor clearMenuDataInteractor) {

    this.threadSpec = threadSpec;
    this.contextProvider = contextProvider;
    this.interactorInvoker = interactorInvoker;
    this.getMenuDataInteractor = getMenuDataInteractor;
    this.clearMenuDataInteractor = clearMenuDataInteractor;
  }

  public MenuContentData getMenu(boolean useCache) {

    try {

      getMenu1(useCache);

      countDownLatch.await();

      return result[0];
    } catch (Exception e) {
      return null;
    }
  }

  private void getMenu1(boolean useCache) {
    getMenuDataInteractor.setUseCache(useCache);

    new Thread(new Runnable() {
      @Override public void run() {
        try {
          InteractorResponse<MenuContentData> call = getMenuDataInteractor.call();
          final MenuContentData menuContentData = call.getResult();

          result[0] = menuContentData;
          countDownLatch.countDown();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

  public void clear() {
    new InteractorExecution<>(clearMenuDataInteractor).execute(interactorInvoker);
  }





}

package com.gigigo.orchextra.core.controller;

import com.gigigo.interactorexecutor.base.invoker.InteractorExecution;
import com.gigigo.interactorexecutor.base.invoker.InteractorInvoker;
import com.gigigo.interactorexecutor.base.invoker.InteractorResult;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItem;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.elements.ElementData;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import com.gigigo.orchextra.core.domain.interactors.elements.GetElementByIdInteractor;
import com.gigigo.orchextra.core.domain.interactors.errors.GenericResponseDataError;
import com.gigigo.orchextra.core.domain.interactors.errors.NoNetworkConnectionError;
import com.gigigo.orchextra.core.domain.invocators.MenuInteractorInvocator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class OcmControllerImp implements OcmController {

  private final InteractorInvoker interactorInvoker;
  private final GetElementByIdInteractor getElementByIdInteractor;
  private final MenuInteractorInvocator menuInteractorInvocator;

  private Map<String, ContentItem> contentDataList;
  private Map<String, ElementCache> elementCacheDataList;

  public OcmControllerImp(InteractorInvoker interactorInvoker,
      GetElementByIdInteractor getElementByIdInteractor,
      MenuInteractorInvocator interactorInvocation) {

    this.interactorInvoker = interactorInvoker;
    this.getElementByIdInteractor = getElementByIdInteractor;
    this.menuInteractorInvocator = interactorInvocation;

    contentDataList = new HashMap<>();
    elementCacheDataList = new HashMap<>();

    //menuInteractorInvocator.attachView(this);
  }

  @Override public MenuContentData getMenu(boolean useCache) {
    return menuInteractorInvocator.getMenu(useCache);
  }

  @Override public String getContentUrlBySection(String section) {
    MenuContentData savedMenuContentData = menuInteractorInvocator.getMenu(true);
    if (savedMenuContentData == null || savedMenuContentData.getElementsCache() == null) {
      return null;
    }

    ElementCache elementCache = savedMenuContentData.getElementsCache().get(section);

    if (elementCache == null || elementCache.getRender() == null) {
      return null;
    }

    return elementCache.getRender().getContentUrl();
  }

  @Override public ContentItem getSectionContentById(String section) {
    if (contentDataList != null && contentDataList.containsKey(section)) {
      return contentDataList.get(section);
    } else {
      return null;
    }
  }

  @Override public void saveSectionContentData(String section, ContentData contentData) {
    contentDataList.put(section, contentData.getContent());

    Map<String, ElementCache> elementsCache = contentData.getElementsCache();
    if (elementsCache != null) {
      for (String key : elementsCache.keySet()) {
        elementCacheDataList.put(key, elementsCache.get(key));
      }
    }
  }

  @Override public void clearCache() {
    menuInteractorInvocator.clear();
    contentDataList = new HashMap<>();
    elementCacheDataList = new HashMap<>();
  }

  @Override public ElementCache getCachedElement(final String elementUrl) {
    try {
      final ElementCache[] elementCache = { elementCacheDataList.get(elementUrl) };

      if (elementCache[0] == null) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        String slug = getSlug(elementUrl);

        getElementByIdInteractor.setElementId(slug);

        new InteractorExecution<>(getElementByIdInteractor).result(
            new InteractorResult<ElementData>() {
              @Override public void onResult(ElementData result) {
                elementCacheDataList.put(elementUrl, result.getElement());
                elementCache[0] = result.getElement();
                countDownLatch.countDown();
              }
            })
            .error(NoNetworkConnectionError.class,
                new InteractorResult<NoNetworkConnectionError>() {
                  @Override public void onResult(NoNetworkConnectionError interactorError) {
                    elementCache[0] = null;
                    countDownLatch.countDown();
                  }
                })
            .error(GenericResponseDataError.class,
                new InteractorResult<GenericResponseDataError>() {
                  @Override public void onResult(GenericResponseDataError interactorError) {
                    elementCache[0] = null;
                    countDownLatch.countDown();
                  }
                })
            .execute(interactorInvoker);

        countDownLatch.await();
      }

      return elementCache[0];
    } catch (Exception e) {
      return null;
    }
  }

  private String getSlug(String elementUrl) {

    try {
      return elementUrl.substring(elementUrl.lastIndexOf("/") + 1, elementUrl.length());
    } catch (Exception ignored) {
      return null;
    }
  }
}

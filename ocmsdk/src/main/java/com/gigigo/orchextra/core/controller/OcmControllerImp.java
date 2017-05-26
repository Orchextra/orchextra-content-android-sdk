package com.gigigo.orchextra.core.controller;

import android.util.Log;
import com.gigigo.orchextra.core.data.rxRepository.OcmDataRepository;
import com.gigigo.orchextra.core.data.rxRepository.rxDatasource.OcmCloudDataStore;
import com.gigigo.orchextra.core.data.rxRepository.rxDatasource.OcmDataStoreFactory;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItem;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import com.gigigo.orchextra.core.domain.invocators.DetailContentElementInteractorInvocator;
import com.gigigo.orchextra.core.domain.invocators.GridElementsInteractorInvocator;
import com.gigigo.orchextra.core.domain.invocators.MenuInteractorInvocator;
import com.gigigo.orchextra.core.domain.rxInteractor.DefaultObserver;
import com.gigigo.orchextra.core.domain.rxInteractor.GetMenus;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

public class OcmControllerImp implements OcmController {

  private final MenuInteractorInvocator menuInteractorInvocator;
  private final GridElementsInteractorInvocator gridElementsInteractorInvocator;
  private final DetailContentElementInteractorInvocator detailContentElementInteractorInvocator;
  private final GetMenus getMenus;

  public OcmControllerImp(MenuInteractorInvocator interactorInvocation,
      GridElementsInteractorInvocator gridElementsInteractorInvocator, DetailContentElementInteractorInvocator detailContentElementInteractorInvocator,
      GetMenus getMenus) {

    this.getMenus = getMenus;
    this.menuInteractorInvocator = interactorInvocation;
    this.gridElementsInteractorInvocator = gridElementsInteractorInvocator;
    this.detailContentElementInteractorInvocator = detailContentElementInteractorInvocator;
  }

  @Override public MenuContentData getMenu(boolean useCache) {
    try {
      MenuObserver menuObserver = new MenuObserver();

      int i = Observable.fromArray(1,2,3,4).blockingFirst();
      MenuContentData a = Single.fromObservable(getMenus.execute(menuObserver, GetMenus.Params.forForceReload(!useCache))).blockingGet();
    }catch (Exception e) {
      e.printStackTrace();
    }
    //getMenus.execute(menuObserver, GetMenus.Params.forForceReload(!useCache)).blockingSingle(); //menuInteractorInvocator.getMenu(useCache);
    return menuInteractorInvocator.getMenu(useCache);
  }


  private final class MenuObserver extends DefaultObserver<MenuContentData> {

    @Override public void onComplete() {

    }

    @Override public void onError(Throwable e) {
      e.printStackTrace();
    }

    @Override public void onNext(MenuContentData menuContentData) {
      Log.v("MenuContent", "elements " + menuContentData.getMenuContentList().size());
    }
  }
  //
  //@Override
  //public ElementCache getElementCacheBySection(String section) {
  //  MenuContentData savedMenuContentData = menuInteractorInvocator.getMenu(true);
  //  if (savedMenuContentData == null || savedMenuContentData.getElementsCache() == null) {
  //    return null;
  //  }
  //
  //  return savedMenuContentData.getElementsCache().get(section);
  //}
  //
  //@Override public String getContentUrlBySection(String section) {
  //  ElementCache elementCache = getElementCacheBySection(section);
  //
  //  if (elementCache == null || elementCache.getRender() == null) {
  //    return null;
  //  }
  //
  //  return elementCache.getRender().getContentUrl();
  //}
  //
  //@Override public ContentItem getSectionContentById(String section) {
  //  return detailContentElementInteractorInvocator.getDetailSectionContentBySection(section);
  //}
  //
  //@Override public void saveSectionContentData(String section, ContentData contentData) {
  //  detailContentElementInteractorInvocator.saveDetailSectionContentBySection(section, contentData.getContent());
  //
  //  Map<String, ElementCache> elementsCache = contentData.getElementsCache();
  //  if (elementsCache != null) {
  //    for (String key : elementsCache.keySet()) {
  //      gridElementsInteractorInvocator.saveElementById(key, elementsCache.get(key));
  //    }
  //  }
  //}
  //
  //@Override public void clearCache() {
  //  menuInteractorInvocator.clear();
  //  detailContentElementInteractorInvocator.clear();
  //  gridElementsInteractorInvocator.clear();
  //}
  //
  //@Override public ElementCache getCachedElement(final String elementUrl) {
  //  try {
  //    ElementCache elementCache = gridElementsInteractorInvocator.getElementById(elementUrl);
  //
  //    if (elementCache == null) {
  //      String slug = getSlug(elementUrl);
  //      elementCache = gridElementsInteractorInvocator.getElementById(slug);
  //    }
  //
  //    return elementCache;
  //  } catch (Exception e) {
  //    return null;
  //  }
  //}
  //
  //private String getSlug(String elementUrl) {
  //
  //  try {
  //    return elementUrl.substring(elementUrl.lastIndexOf("/") + 1, elementUrl.length());
  //  } catch (Exception ignored) {
  //    return null;
  //  }
  //}




  private final class UserListObserver extends DefaultObserver<MenuContentData> {

    @Override public void onComplete() {

    }

    @Override public void onError(Throwable e) {
      //presenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
    }

    @Override public void onNext(MenuContentData menuContentData) {
      Log.v("MenuContentData", "Elements: " + menuContentData.getMenuContentList().size());
    }
  }

  @Override public ElementCache getCachedElement(String elementUrl) {
    return null;
  }

  @Override public ElementCache getElementCacheBySection(String section) {
    return null;
  }

  @Override public String getContentUrlBySection(String section) {
    return null;
  }

  @Override public ContentItem getSectionContentById(String section) {
    return null;
  }

  @Override public void saveSectionContentData(String section, ContentData contentData) {

  }

  @Override public void clearCache() {

  }
}

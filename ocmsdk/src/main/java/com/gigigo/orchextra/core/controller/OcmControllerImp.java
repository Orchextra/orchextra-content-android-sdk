package com.gigigo.orchextra.core.controller;

import com.gigigo.orchextra.core.data.rxException.ApiDetailNotFoundException;
import com.gigigo.orchextra.core.data.rxException.ApiMenuNotFoundException;
import com.gigigo.orchextra.core.data.rxException.ApiSearchNotFoundException;
import com.gigigo.orchextra.core.data.rxException.ApiSectionNotFoundException;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItem;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.elements.ElementData;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import com.gigigo.orchextra.core.domain.invocators.DetailContentElementInteractorInvocator;
import com.gigigo.orchextra.core.domain.invocators.GridElementsInteractorInvocator;
import com.gigigo.orchextra.core.domain.invocators.MenuInteractorInvocator;
import com.gigigo.orchextra.core.domain.rxInteractor.DefaultObserver;
import com.gigigo.orchextra.core.domain.rxInteractor.GetDetail;
import com.gigigo.orchextra.core.domain.rxInteractor.GetMenus;
import com.gigigo.orchextra.core.domain.rxInteractor.GetSection;
import com.gigigo.orchextra.core.domain.rxInteractor.SearchElements;
import java.util.Map;

public class OcmControllerImp implements OcmController {

  private final MenuInteractorInvocator menuInteractorInvocator;
  private final GridElementsInteractorInvocator gridElementsInteractorInvocator;
  private final DetailContentElementInteractorInvocator detailContentElementInteractorInvocator;
  private final GetMenus getMenus;
  private final GetSection getSection;
  private final GetDetail getDetail;
  private final SearchElements searchElements;

  public OcmControllerImp(MenuInteractorInvocator interactorInvocation,
      GridElementsInteractorInvocator gridElementsInteractorInvocator,
      DetailContentElementInteractorInvocator detailContentElementInteractorInvocator,
      GetMenus getMenus, GetSection getSection, GetDetail getDetail,
      SearchElements searchElements) {

    this.menuInteractorInvocator = interactorInvocation;
    this.gridElementsInteractorInvocator = gridElementsInteractorInvocator;
    this.detailContentElementInteractorInvocator = detailContentElementInteractorInvocator;
    this.getMenus = getMenus;
    this.getSection = getSection;
    this.getDetail = getDetail;
    this.searchElements = searchElements;
  }

  @Override public MenuContentData getMenu(boolean useCache) {
    return menuInteractorInvocator.getMenu(useCache);
  }

  @Override public ElementCache getElementCacheBySection(String section) {
    MenuContentData savedMenuContentData = menuInteractorInvocator.getMenu(true);
    if (savedMenuContentData == null || savedMenuContentData.getElementsCache() == null) {
      return null;
    }

    return savedMenuContentData.getElementsCache().get(section);
  }

  @Override public String getContentUrlBySection(String section) {
    ElementCache elementCache = getElementCacheBySection(section);

    if (elementCache == null || elementCache.getRender() == null) {
      return null;
    }

    return elementCache.getRender().getContentUrl();
  }

  @Override public ContentItem getSectionContentById(String section) {
    return detailContentElementInteractorInvocator.getDetailSectionContentBySection(section);
  }

  @Override public void saveSectionContentData(String section, ContentData contentData) {
    detailContentElementInteractorInvocator.saveDetailSectionContentBySection(section,
        contentData.getContent());

    Map<String, ElementCache> elementsCache = contentData.getElementsCache();
    if (elementsCache != null) {
      for (String key : elementsCache.keySet()) {
        gridElementsInteractorInvocator.saveElementById(key, elementsCache.get(key));
      }
    }
  }

  @Override public void clearCache() {
    menuInteractorInvocator.clear();
    detailContentElementInteractorInvocator.clear();
    gridElementsInteractorInvocator.clear();
  }

  @Override public ElementCache getCachedElement(final String elementUrl) {
    try {
      String slug = getSlug(elementUrl);
      ElementCache elementCache = gridElementsInteractorInvocator.getElementById(slug);
      /*ElementCache elementCache = gridElementsInteractorInvocator.getElementById(elementUrl);

      if (elementCache == null) {
        String slug = getSlug(elementUrl);
        elementCache = gridElementsInteractorInvocator.getElementById(slug);
      }
      */
      return elementCache;
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

  //region new
  @Override public void getMenu(boolean forceReload, GetMenusControllerCallback getMenusCallback) {
    getMenus.execute(new MenuObserver(getMenusCallback),
        GetMenus.Params.forForceReload(forceReload));
  }

  @Override public void getSection(final boolean forceReload, final String section,
      GetSectionControllerCallback getSectionControllerCallback) {

    getMenu(false, new GetMenusControllerCallback() {
      @Override public void onGetMenusLoaded(MenuContentData menus) {
        ElementCache elementCache = menus.getElementsCache().get(section);
        if (elementCache == null || elementCache.getRender() == null) {
          getSectionControllerCallback.onGetSectionFails(new ApiSectionNotFoundException());
        } else {
          String url = elementCache.getRender().getContentUrl();
          if (url != null) {
            getSection.execute(new SectionObserver(getSectionControllerCallback),
                GetSection.Params.forSection(forceReload, url));
          } else {
            getSectionControllerCallback.onGetSectionFails(new ApiSectionNotFoundException(
                "elementCache.getRender().getContentUrl() IS NULL"));
          }
        }
      }

      @Override public void onGetMenusFails(Exception e) {
        getSectionControllerCallback.onGetSectionFails(new ApiSectionNotFoundException(e));
      }
    });
  }

  @Override public void getDetails(boolean forceReload, String elementUrl,
      GetDetailControllerCallback getDetailControllerCallback) {
    String slug = getSlug(elementUrl);
    getDetail.execute(new DetailObserver(getDetailControllerCallback),
        GetDetail.Params.forDetail(forceReload, slug));
  }

  @Override
  public void search(String textToSearch, SearchControllerCallback searchControllerCallback) {
    searchElements.execute(new SearchObserver(searchControllerCallback),
        SearchElements.Params.forTextToSearch(true, textToSearch));
  }

  //end region
  //region observers

  private final class MenuObserver extends DefaultObserver<MenuContentData> {
    private final GetMenusControllerCallback getMenusCallback;

    public MenuObserver(GetMenusControllerCallback getMenusCallback) {
      this.getMenusCallback = getMenusCallback;
    }

    @Override public void onComplete() {

    }

    @Override public void onError(Throwable e) {
      getMenusCallback.onGetMenusFails(new ApiMenuNotFoundException(e));
    }

    @Override public void onNext(MenuContentData menuContentData) {
      getMenusCallback.onGetMenusLoaded(menuContentData);
    }
  }

  private final class SectionObserver extends DefaultObserver<ContentData> {
    private final GetSectionControllerCallback getSectionControllerCallback;

    public SectionObserver(GetSectionControllerCallback getSectionControllerCallback) {
      this.getSectionControllerCallback = getSectionControllerCallback;
    }

    @Override public void onComplete() {

    }

    @Override public void onError(Throwable e) {
      if (getSectionControllerCallback != null) {
        getSectionControllerCallback.onGetSectionFails(new ApiSectionNotFoundException(e));
      }
    }

    @Override public void onNext(ContentData contentData) {
      if (getSectionControllerCallback != null) {
        getSectionControllerCallback.onGetSectionLoaded(contentData);
      }
    }
  }

  private final class DetailObserver extends DefaultObserver<ElementData> {
    private final GetDetailControllerCallback getDetailControllerCallback;

    public DetailObserver(GetDetailControllerCallback getDetailControllerCallback) {
      this.getDetailControllerCallback = getDetailControllerCallback;
    }

    @Override public void onNext(ElementData elementData) {
      if (getDetailControllerCallback != null) {
        getDetailControllerCallback.onGetDetailLoaded(elementData.getElement());
      }
    }

    @Override public void onComplete() {
    }

    @Override public void onError(Throwable exception) {
      if (getDetailControllerCallback != null) {
        getDetailControllerCallback.onGetDetailFails(new ApiDetailNotFoundException(exception));
      }
    }
  }

  private final class SearchObserver extends DefaultObserver<ContentData> {
    private final SearchControllerCallback searchControllerCallback;

    public SearchObserver(SearchControllerCallback searchControllerCallback) {
      this.searchControllerCallback = searchControllerCallback;
    }

    @Override public void onComplete() {

    }

    @Override public void onError(Throwable e) {
      if (searchControllerCallback != null) {
        searchControllerCallback.onSearchFails(new ApiSearchNotFoundException(e));
      }
    }

    @Override public void onNext(ContentData contentData) {
      if (searchControllerCallback != null) searchControllerCallback.onSearchLoaded(contentData);
    }
  }
  //end region
}

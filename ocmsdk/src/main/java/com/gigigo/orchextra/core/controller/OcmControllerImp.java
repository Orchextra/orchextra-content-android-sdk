package com.gigigo.orchextra.core.controller;

import com.gigigo.orchextra.core.data.rxException.ApiDetailNotFoundException;
import com.gigigo.orchextra.core.data.rxException.ApiMenuNotFoundException;
import com.gigigo.orchextra.core.data.rxException.ApiSearchNotFoundException;
import com.gigigo.orchextra.core.data.rxException.ApiSectionNotFoundException;
import com.gigigo.orchextra.core.data.rxException.ClearCacheException;
import com.gigigo.orchextra.core.data.rxException.NetworkConnectionException;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheType;
import com.gigigo.orchextra.core.domain.entities.elements.ElementData;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import com.gigigo.orchextra.core.domain.rxInteractor.ClearCache;
import com.gigigo.orchextra.core.domain.rxInteractor.DefaultObserver;
import com.gigigo.orchextra.core.domain.rxInteractor.GetDetail;
import com.gigigo.orchextra.core.domain.rxInteractor.GetMenus;
import com.gigigo.orchextra.core.domain.rxInteractor.GetSection;
import com.gigigo.orchextra.core.domain.rxInteractor.PriorityScheduler;
import com.gigigo.orchextra.core.domain.rxInteractor.SearchElements;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;

public class OcmControllerImp implements OcmController {
  private static final PriorityScheduler.Priority PRIORITY_MENUS = PriorityScheduler.Priority.LOWEST;

  private static final PriorityScheduler.Priority PRIORITY_SECTIONS =
      PriorityScheduler.Priority.LOW;

  private static final PriorityScheduler.Priority PRIORITY_DETAIL =
      PriorityScheduler.Priority.MEDIUM;

  private static final PriorityScheduler.Priority PRIORITY_SEARCH =
      PriorityScheduler.Priority.LOW;

  private static final PriorityScheduler.Priority PRIORITY_CLEAR = PriorityScheduler.Priority.LOW;

  private final GetMenus getMenus;
  private final GetSection getSection;
  private final GetDetail getDetail;
  private final SearchElements searchElements;
  private final ClearCache clearCache;
  private final ConnectionUtils connectionUtils;

  public OcmControllerImp(GetMenus getMenus, GetSection getSection, GetDetail getDetail,
      SearchElements searchElements, ClearCache clearCache, ConnectionUtils connectionUtils) {

    this.getMenus = getMenus;
    this.getSection = getSection;
    this.getDetail = getDetail;
    this.searchElements = searchElements;
    this.clearCache = clearCache;

    this.connectionUtils = connectionUtils;
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
        GetMenus.Params.forForceReload(forceReload), forceReload ? PRIORITY_MENUS : PriorityScheduler.Priority.HIGH);
  }

  @Override
  public void getSection(final boolean forceReload, final String section, int imagesToDownload,
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
                GetSection.Params.forSection(forceReload, url, imagesToDownload),
                forceReload ? PRIORITY_SECTIONS : PriorityScheduler.Priority.HIGH);
          } else {
            getSectionControllerCallback.onGetSectionFails(new ApiSectionNotFoundException(
                "elementCache.getRender().getContentUrl() IS NULL"));
          }
        }
      }

      @Override public void onGetMenusFails(Exception e) {
        getSectionControllerCallback.onGetSectionFails(new ApiSectionNotFoundException(e));
        e.printStackTrace();
      }
    });
  }

  @Override public void getDetails(boolean forceReload, String elementUrl,
      GetDetailControllerCallback getDetailControllerCallback) {
    String slug = getSlug(elementUrl);
    getDetail.execute(new DetailObserver(getDetailControllerCallback),
        GetDetail.Params.forDetail(forceReload, slug), forceReload ? PRIORITY_DETAIL : PriorityScheduler.Priority.HIGHEST);
  }

  @Override
  public void search(String textToSearch, SearchControllerCallback searchControllerCallback) {
    searchElements.execute(new SearchObserver(searchControllerCallback),
        SearchElements.Params.forTextToSearch(textToSearch), PRIORITY_SEARCH);
  }

  @Override public void clearCache(boolean images, boolean data,
      final ClearCacheCallback clearCacheCallback) {
    clearCache.execute(new ClearCacheObserver(clearCacheCallback),
        ClearCache.Params.create(images, data), PRIORITY_CLEAR);

  }

  @Override public void disposeUseCases() {
    getMenus.dispose();
    getSection.dispose();
    getDetail.dispose();
    searchElements.dispose();
    clearCache.dispose();
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
      e.printStackTrace();
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
      e.printStackTrace();
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
        if (!connectionUtils.hasConnection() && (elementData.getElement().getType()
            == ElementCacheType.WEBVIEW
            || elementData.getElement().getType() == ElementCacheType.VIDEO)) {
          getDetailControllerCallback.onGetDetailNoAvailable(new NetworkConnectionException());
        } else {
          getDetailControllerCallback.onGetDetailLoaded(elementData.getElement());
        }
      }
    }

    @Override public void onComplete() {
    }

    @Override public void onError(Throwable exception) {
      if (getDetailControllerCallback != null) {
        getDetailControllerCallback.onGetDetailFails(new ApiDetailNotFoundException(exception));
      }
      exception.printStackTrace();
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
      e.printStackTrace();
    }

    @Override public void onNext(ContentData contentData) {
      if (searchControllerCallback != null) searchControllerCallback.onSearchLoaded(contentData);
    }
  }

  private final class ClearCacheObserver extends DefaultObserver<Void> {
    private final ClearCacheCallback clearCacheCallback;

    public ClearCacheObserver(ClearCacheCallback clearCacheCallback) {
      this.clearCacheCallback = clearCacheCallback;
    }

    @Override public void onComplete() {
      if (clearCacheCallback != null) clearCacheCallback.onClearCacheSuccess();
    }

    @Override public void onError(Throwable e) {
      if (clearCacheCallback != null) {
        clearCacheCallback.onClearCacheFails(new ClearCacheException(e));
      }
      e.printStackTrace();
    }
  }
  //end region
}

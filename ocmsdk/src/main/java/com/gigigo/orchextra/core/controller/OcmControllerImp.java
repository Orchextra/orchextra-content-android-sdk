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
import com.gigigo.orchextra.core.domain.entities.elements.Element;
import com.gigigo.orchextra.core.domain.entities.elements.ElementData;
import com.gigigo.orchextra.core.domain.entities.menus.DataRequest;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import com.gigigo.orchextra.core.domain.rxInteractor.ClearCache;
import com.gigigo.orchextra.core.domain.rxInteractor.DefaultObserver;
import com.gigigo.orchextra.core.domain.rxInteractor.GetDetail;
import com.gigigo.orchextra.core.domain.rxInteractor.GetMenus;
import com.gigigo.orchextra.core.domain.rxInteractor.GetSection;
import com.gigigo.orchextra.core.domain.rxInteractor.PriorityScheduler;
import com.gigigo.orchextra.core.domain.rxInteractor.SearchElements;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import com.gigigo.orchextra.core.sdk.utils.DateUtils;
import com.gigigo.orchextra.core.sdk.utils.MenuListComparator;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.ocm.dto.UiMenuData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import timber.log.Timber;

public class OcmControllerImp implements OcmController {

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

  /**
   * 1 - If Pull to refresh
   * 1.1 - Check version
   * 1.1 - Request cloud data
   * 1.2 - Return data
   *
   * 2 - It's first time
   * 2.1 - Request cloud data
   * 2.2 - Return data
   *
   * 3 - Other scenarios
   * 3.1 - Get content from cache
   * 4.2 - Return data
   * 4.3 - App is which control menu changes
   */
  @Override public void getMenu(GetMenusControllerCallback getMenusCallback) {
    retrieveMenus(getMenusCallback);
  }

  private void retrieveMenus(MenuObserver observer) {
    getMenus.execute(observer, new GetMenus.Params(), PriorityScheduler.Priority.HIGH);
  }

  private void retrieveMenus(GetMenusControllerCallback getMenusCallback) {
    retrieveMenus(new MenuObserver(new GetMenusControllerCallback() {
      @Override public void onGetMenusLoaded(UiMenuData menus) {
        getMenusCallback.onGetMenusLoaded(menus);
      }

      @Override public void onGetMenusFails(Exception e) {
        Timber.e(e, "retrieveMenus()");
      }
    }));
  }

  /**
   * Retrieve section from cache
   * if Section is for cloud then return
   * else get version
   * compare version with section version
   * if are equals return
   * else retrieve version from cloud
   */
  @Override public void getSection(DataRequest dataRequest, final String contentUrl,
      int imagesToDownload, GetSectionControllerCallback getSectionControllerCallback) {

    if (contentUrl != null) {
      switch (dataRequest) {
        case FORCE_CLOUD:
          retrieveSection(true, contentUrl, imagesToDownload, getSectionControllerCallback);
        case ONLY_CACHE:
        case FIRST_CACHE:
          retrieveSection(false, contentUrl, imagesToDownload, getSectionControllerCallback);
          break;
      }
    }
  }

  private void retrieveSection(boolean forceRelaod, String contentUrl, int imagesToDownload,
      GetSectionControllerCallback getSectionControllerCallback) {
    getSection.execute(new SectionObserver(new GetSectionControllerCallback() {
          @Override public void onGetSectionLoaded(ContentData contentData) {

            if (contentData.isFromCloud()) {
              if (getSectionControllerCallback != null) {
                getSectionControllerCallback.onGetSectionLoaded(contentData);
              }
              return;
            }
            checkVersionAndExpiredAtAndRetrieveSection(contentData, contentUrl, imagesToDownload,
                getSectionControllerCallback);
          }

          @Override public void onGetSectionFails(Exception e) {
            Timber.e(e, "retrieveSection()");
          }
        }), GetSection.Params.forSection(forceRelaod, contentUrl, imagesToDownload),
        PriorityScheduler.Priority.HIGH);
  }

  private void retrieveSectionOnlyFromCache(String contentUrl, int imagesToDownload,
      GetSectionControllerCallback getSectionControllerCallback) {

    getSection.execute(new SectionObserver(new GetSectionControllerCallback() {
          @Override public void onGetSectionLoaded(ContentData contentData) {
            if (getSectionControllerCallback != null) {
              if (!contentData.isFromCloud()) {
                getSectionControllerCallback.onGetSectionLoaded(contentData);
              } else {
                getSectionControllerCallback.onGetSectionLoaded(null);
              }
            }
          }

          @Override public void onGetSectionFails(Exception e) {
            if (getSectionControllerCallback != null) {
              getSectionControllerCallback.onGetSectionFails(e);
            }
          }
        }), GetSection.Params.forSection(false, contentUrl, imagesToDownload),
        PriorityScheduler.Priority.HIGH);
  }

  private void checkVersionAndExpiredAtAndRetrieveSection(ContentData contentData,
      String contentUrl, int imagesToDownload,
      GetSectionControllerCallback getSectionControllerCallback) {
    boolean requestFromCloud =
        hasToForceReloadBecauseVersionChangedOrSectionHasExpired(contentData);

    if (requestFromCloud) {
      getSection.execute(new SectionObserver(new GetSectionControllerCallback() {
            @Override public void onGetSectionLoaded(ContentData contentData) {
              if (getSectionControllerCallback != null) {
                getSectionControllerCallback.onGetSectionLoaded(contentData);
              }
            }

            @Override public void onGetSectionFails(Exception e) {
              if (getSectionControllerCallback != null) {
                getSectionControllerCallback.onGetSectionFails(e);
              }
            }
          }), GetSection.Params.forSection(true, contentUrl, imagesToDownload),
          PriorityScheduler.Priority.HIGH);
    } else if (getSectionControllerCallback != null) {
      getSectionControllerCallback.onGetSectionLoaded(contentData);
    }
  }

  private String getSlug(String elementUrl) {
    try {
      return elementUrl.substring(elementUrl.lastIndexOf("/") + 1, elementUrl.length());
    } catch (Exception ignored) {
      return null;
    }
  }

  @Override public void getDetails(String elementUrl,
      GetDetailControllerCallback getDetailControllerCallback) {
    String slug = getSlug(elementUrl);
    getDetail.execute(new DetailObserver(getDetailControllerCallback),
        GetDetail.Params.forDetail(false, slug), PriorityScheduler.Priority.HIGH);
  }

  @Override
  public void search(String textToSearch, SearchControllerCallback searchControllerCallback) {
    searchElements.execute(new SearchObserver(searchControllerCallback),
        SearchElements.Params.forTextToSearch(textToSearch), PriorityScheduler.Priority.LOW);
  }

  @Override public void clearCache(boolean images, boolean data,
      final ClearCacheCallback clearCacheCallback) {
    clearCache.execute(new ClearCacheObserver(clearCacheCallback),
        ClearCache.Params.create(images, data), PriorityScheduler.Priority.LOW);
  }

  @Override public void refreshMenuData() {

    //OCManager.notifyOnMenuChanged(menus);
  }

  @Override public void disposeUseCases() {
    getMenus.dispose();
    getSection.dispose();
    getDetail.dispose();
    searchElements.dispose();
    clearCache.dispose();
  }

  private boolean hasToForceReloadBecauseVersionChangedOrSectionHasExpired(
      ContentData contentData) {
    return contentData.getExpiredAt() != null && DateUtils.isAfterCurrentDate(
        contentData.getExpiredAt());
  }

  private UiMenuData transformMenu(MenuContentData menuContentData) {
    UiMenuData uiMenuData = new UiMenuData();

    List<UiMenu> menuList = new ArrayList<>();

    if (menuContentData != null
        && menuContentData.getMenuContentList() != null
        && menuContentData.getMenuContentList().size() > 0) {

      uiMenuData.setFromCloud(menuContentData.isFromCloud());

      for (Element element : menuContentData.getMenuContentList().get(0).getElements()) {
        UiMenu uiMenu = new UiMenu();

        uiMenu.setSlug(element.getSlug());
        uiMenu.setText(element.getSectionView().getText());
        uiMenu.setElementUrl(element.getElementUrl());
        uiMenu.setHasNewVersion(element.getHasNewVersion());

        if (menuContentData.getElementsCache() != null) {
          ElementCache elementCache =
              menuContentData.getElementsCache().get(element.getElementUrl());
          if (elementCache != null) {
            uiMenu.setElementCache(elementCache);
            uiMenu.setUpdateAt(elementCache.getUpdateAt());
            if (elementCache.getRender() != null) {
              uiMenu.setContentUrl(elementCache.getRender().getContentUrl());
            }
          }
        }

        menuList.add(uiMenu);
      }
    }

    Collections.sort(menuList, new MenuListComparator());
    uiMenuData.setUiMenuList(menuList);
    return uiMenuData;
  }

  //performance https://www.androiddesignpatterns.com/2013/01/inner-class-handler-memory-leak.html
  //leak with this kind inner class

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
      Timber.e(e, "onGetMenusFails");
    }

    @Override public void onNext(MenuContentData menuContentData) {
      retrieveMenu(menuContentData);
    }

    private void retrieveMenu(MenuContentData menuContentData) {
      getMenusCallback.onGetMenusLoaded(transformMenu(menuContentData));
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
        if (!connectionUtils.hasConnection() && (elementData.getElement() == null
            || elementData.getElement().getType() == ElementCacheType.WEBVIEW
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
  //end region

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
}

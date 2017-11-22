package com.gigigo.orchextra.core.controller;

import com.gigigo.orchextra.core.data.rxException.ApiDetailNotFoundException;
import com.gigigo.orchextra.core.data.rxException.ApiMenuNotFoundException;
import com.gigigo.orchextra.core.data.rxException.ApiSearchNotFoundException;
import com.gigigo.orchextra.core.data.rxException.ApiSectionNotFoundException;
import com.gigigo.orchextra.core.data.rxException.ApiVersionNotFoundException;
import com.gigigo.orchextra.core.data.rxException.ClearCacheException;
import com.gigigo.orchextra.core.data.rxException.NetworkConnectionException;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheType;
import com.gigigo.orchextra.core.domain.entities.elements.ElementData;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import com.gigigo.orchextra.core.domain.entities.version.VersionData;
import com.gigigo.orchextra.core.domain.rxInteractor.ClearCache;
import com.gigigo.orchextra.core.domain.rxInteractor.DefaultObserver;
import com.gigigo.orchextra.core.domain.rxInteractor.GetDetail;
import com.gigigo.orchextra.core.domain.rxInteractor.GetMenus;
import com.gigigo.orchextra.core.domain.rxInteractor.GetSection;
import com.gigigo.orchextra.core.domain.rxInteractor.GetVersion;
import com.gigigo.orchextra.core.domain.rxInteractor.PriorityScheduler;
import com.gigigo.orchextra.core.domain.rxInteractor.SearchElements;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import com.gigigo.orchextra.core.sdk.utils.DateUtils;
import com.gigigo.orchextra.core.sdk.utils.OcmPreferences;

public class OcmControllerImp implements OcmController {

  private final GetVersion getVersion;
  private final GetMenus getMenus;
  private final GetSection getSection;
  private final GetDetail getDetail;
  private final SearchElements searchElements;
  private final ClearCache clearCache;
  private final ConnectionUtils connectionUtils;
  private final OcmPreferences ocmPreferences;

  public OcmControllerImp(GetVersion getVersion, GetMenus getMenus, GetSection getSection,
      GetDetail getDetail, SearchElements searchElements, ClearCache clearCache,
      ConnectionUtils connectionUtils, OcmPreferences ocmPreferences) {

    this.getVersion = getVersion;
    this.getMenus = getMenus;
    this.getSection = getSection;
    this.getDetail = getDetail;
    this.searchElements = searchElements;
    this.clearCache = clearCache;

    this.connectionUtils = connectionUtils;
    this.ocmPreferences = ocmPreferences;
  }

  private String getSlug(String elementUrl) {
    try {
      return elementUrl.substring(elementUrl.lastIndexOf("/") + 1, elementUrl.length());
    } catch (Exception ignored) {
      return null;
    }
  }

  //region menus
  @Override public void getMenu(GetMenusControllerCallback getMenusCallback) {
    checkVersionChangedAndRequestMenus(getMenusCallback);
  }

  private void checkVersionChangedAndRequestMenus(GetMenusControllerCallback getMenusCallback) {
    getVersion.execute(new VersionObserver(new GetVersionControllerCallback() {
      @Override public void onGetVersionLoaded(VersionData versionData) {
        boolean forceReload = hasToForceReloadBecauseVersionChanged(versionData);
        retrieveMenus(forceReload, getMenusCallback);
      }

      @Override public void onGetVersionFails(Exception e) {
        retrieveMenus(false, getMenusCallback);
      }
    }), GetVersion.Params.forVersion(), PriorityScheduler.Priority.HIGH);
  }

  private void retrieveMenus(boolean forceReload, GetMenusControllerCallback getMenusCallback) {
    getMenus.execute(new MenuObserver(getMenusCallback),
        GetMenus.Params.forForceReload(forceReload), PriorityScheduler.Priority.HIGH);
  }

  private boolean hasToForceReloadBecauseVersionChanged(VersionData versionData) {
    boolean forceReload = false;

    String version = ocmPreferences.getVersion();

    if (versionData != null && (version == null || !version.equals(versionData.getVersion()))) {
      forceReload = true;
      ocmPreferences.saveVersion(versionData.getVersion());
    }
    return forceReload;
  }

  //end region

  @Override public void getSection(final String section, int imagesToDownload,
      GetSectionControllerCallback getSectionControllerCallback) {

    getMenus.execute(new MenuObserver(new GetMenusControllerCallback() {
      @Override public void onGetMenusLoaded(MenuContentData menus) {
        ElementCache elementCache = menus.getElementsCache().get(section);
        if (elementCache == null || elementCache.getRender() == null) {
          getSectionControllerCallback.onGetSectionFails(new ApiSectionNotFoundException());
        } else {
          String url = elementCache.getRender().getContentUrl();
          if (url != null) {
            getSection.execute(
                new SectionObserver(getSectionControllerCallback, url, imagesToDownload),
                GetSection.Params.forSection(false, url, imagesToDownload),
                PriorityScheduler.Priority.HIGH);
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
    }), GetMenus.Params.forForceReload(false), PriorityScheduler.Priority.HIGH);
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

  private final class VersionObserver extends DefaultObserver<VersionData> {
    private final GetVersionControllerCallback getVersionControllerCallback;

    public VersionObserver(GetVersionControllerCallback getVersionControllerCallback) {
      this.getVersionControllerCallback = getVersionControllerCallback;
    }

    @Override public void onError(Throwable e) {
      if (getVersionControllerCallback != null) {
        getVersionControllerCallback.onGetVersionFails(new ApiVersionNotFoundException(e));
      }
      e.printStackTrace();
    }

    @Override public void onNext(VersionData versionData) {
      if (getVersionControllerCallback != null) {
        getVersionControllerCallback.onGetVersionLoaded(versionData);
      }
    }
  }

  private final class SectionObserver extends DefaultObserver<ContentData> {

    private final GetSectionControllerCallback getSectionControllerCallback;
    private final String url;
    private final int imagesToDownload;

    public SectionObserver(GetSectionControllerCallback getSectionControllerCallback, String url,
        int imagesToDownload) {
      this.getSectionControllerCallback = getSectionControllerCallback;
      this.url = url;
      this.imagesToDownload = imagesToDownload;
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
      getVersion.execute(new VersionObserver(new GetVersionControllerCallback() {
        @Override public void onGetVersionLoaded(VersionData versionData) {
          checkVersionAndExpiredAtAndRetrieveSecion(versionData, contentData);
        }

        @Override public void onGetVersionFails(Exception e) {
          if (getSectionControllerCallback != null) {
            getSectionControllerCallback.onGetSectionLoaded(contentData);
          }
        }
      }), GetVersion.Params.forVersion(), PriorityScheduler.Priority.HIGH);
    }

    private void checkVersionAndExpiredAtAndRetrieveSecion(VersionData versionData,
        ContentData contentData) {
      boolean requestFromCloud =
          hasToForceReloadBecauseVersionChangedOrSectionHasExpired(versionData, contentData);

      if (requestFromCloud) {
        //TODO Check if can be changed with method 'getSection' call
        getSection.execute(
            new SectionObserver(getSectionControllerCallback, url, imagesToDownload),
            GetSection.Params.forSection(true, url, imagesToDownload),
            PriorityScheduler.Priority.HIGH);
      }

      if (getSectionControllerCallback != null) {
        getSectionControllerCallback.onGetSectionLoaded(contentData);
      }
    }
  }

  private boolean hasToForceReloadBecauseVersionChangedOrSectionHasExpired(VersionData versionData,
      ContentData contentData) {
    return !versionData.getVersion().equals(contentData.getVersion())
        || contentData.getExpiredAt() != null && DateUtils.isAfterCurrentDate(
        contentData.getExpiredAt());
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

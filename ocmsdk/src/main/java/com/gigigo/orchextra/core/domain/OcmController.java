package com.gigigo.orchextra.core.domain;

import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import com.gigigo.orchextra.core.domain.entities.version.VersionData;

public interface OcmController {

  void refreshAllContent();

  void refreshAllContent(boolean forceUpdate, final GetMenusControllerCallback getMenusCallback);

  void getSection(final String section, int imagesToDownload,
      final GetSectionControllerCallback getSectionControllerCallback);

  void getDetails(final String elementUrl,
      final GetDetailControllerCallback getDetailControllerCallback);

  void search(String textToSearch, SearchControllerCallback searchControllerCallback);

  void clearCache(boolean images, boolean data, final ClearCacheCallback clearCacheCallback);

  void disposeUseCases();

  // Callbacks

  interface GetVersionControllerCallback{
    void onGetVersionLoaded(VersionData versionData);
    void onGetVersionFails(Exception e);
  }
  interface GetMenusControllerCallback {
    void onGetMenusLoaded(MenuContentData menus);

    void onGetMenusFails(Exception e);
  }

  interface GetSectionControllerCallback {
    void onGetSectionLoaded(ContentData contentData);

    void onGetSectionFails(Exception e);
  }

  interface GetDetailControllerCallback {
    void onGetDetailLoaded(ElementCache elementCache);

    void onGetDetailFails(Exception e);

    void onGetDetailNoAvailable(Exception e);
  }

  interface SearchControllerCallback {
    void onSearchLoaded(ContentData contentData);

    void onSearchFails(Exception e);
  }

  interface ClearCacheCallback {
    void onClearCacheSuccess();

    void onClearCacheFails(Exception e);
  }
}

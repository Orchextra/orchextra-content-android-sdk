package com.gigigo.orchextra.core.domain;

import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.menus.DataRequest;
import com.gigigo.orchextra.ocm.dto.UiMenuData;

public interface OcmController {

  void getMenu(final GetMenusControllerCallback getMenusCallback);

  void getSection(DataRequest forceToReload, final String section, int imagesToDownload,
      final GetSectionControllerCallback getSectionControllerCallback);

  void getDetails(final String elementUrl,
      final GetDetailControllerCallback getDetailControllerCallback);

  void search(String textToSearch, SearchControllerCallback searchControllerCallback);

  void clearCache(boolean images, boolean data, final ClearCacheCallback clearCacheCallback);

  void disposeUseCases();

  void refreshMenuData();

  // Callbacks

  interface GetVersionControllerCallback {
    void onGetVersionFails(Exception e);
  }

  interface GetMenusControllerCallback {
    void onGetMenusLoaded(UiMenuData menus);

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

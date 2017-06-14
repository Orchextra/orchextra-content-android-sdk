package com.gigigo.orchextra.core.domain;

import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItem;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;

public interface OcmController {
  void getMenu(boolean forceReload, final GetMenusControllerCallback getMenusCallback);

  void getSection(boolean forceReload, final String section,
      final GetSectionControllerCallback getSectionControllerCallback);

  void getDetails(boolean forceReload, final String elementUrl,
      final GetDetailControllerCallback getDetailControllerCallback);

  void search(String textToSearch, SearchControllerCallback searchControllerCallback);

  void clearCache(boolean images, boolean data, final ClearCacheCallback clearCacheCallback);

  // Callbacks
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

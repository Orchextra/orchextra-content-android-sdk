package com.gigigo.orchextra.core.domain;

import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItem;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;

public interface OcmController {

  @Deprecated
  MenuContentData getMenu(boolean useCache);

  @Deprecated
  ElementCache getCachedElement(String elementUrl);

  @Deprecated
  ElementCache getElementCacheBySection(String section);

  @Deprecated
  String getContentUrlBySection(String section);

  @Deprecated
  ContentItem getSectionContentById(String section);

  @Deprecated
  void saveSectionContentData(String section, ContentData contentData);

  @Deprecated
  void clearCache();


  void getMenu(boolean forceReload, final GetMenusControllerCallback getMenusCallback);

  void getSection(boolean forceReload, final String section, final GetSectionControllerCallback getSectionControllerCallback);

  void getDetails(boolean forceReload, final String elementUrl, final GetDetailControllerCallback getDetailControllerCallback);

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
}

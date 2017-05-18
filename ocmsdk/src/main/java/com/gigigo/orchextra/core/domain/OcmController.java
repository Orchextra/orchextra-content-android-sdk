package com.gigigo.orchextra.core.domain;

import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItem;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;

public interface OcmController {

  MenuContentData getMenu(boolean useCache);

  ElementCache getCachedElement(String elementUrl);

  ElementCache getElementCacheBySection(String section);

  String getContentUrlBySection(String section);

  ContentItem getSectionContentById(String section);

  void saveSectionContentData(String section, ContentData contentData);

  void clearCache();
}

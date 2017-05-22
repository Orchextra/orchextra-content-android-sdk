package com.gigigo.orchextra.core.domain.repository;

import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItem;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;

/**
 * Created by francisco.hernandez on 22/5/17.
 */

public interface OcmProvider {
  MenuContentData getMenu(boolean useCache);

  ElementCache getCachedElement(String elementUrl);

  ElementCache getElementCacheBySection(String section);

  String getContentUrlBySection(String section);

  ContentItem getSectionContentById(String section);
}

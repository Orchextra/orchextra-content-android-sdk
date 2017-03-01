package com.gigigo.orchextra.core.domain;


import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItem;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;

public interface OcmController {
  void getMenu(boolean useCache, OnRetrieveMenuListener onRetrieveMenuListener);

  ElementCache getCachedElement(String elementUrl);

  String getContentUrlBySection(String section);

  ContentItem getSectionContentById(String section);

  void saveSectionContentData(String section, ContentData contentData);

  void clearCache();
}

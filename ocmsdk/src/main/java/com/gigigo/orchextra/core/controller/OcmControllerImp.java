package com.gigigo.orchextra.core.controller;

import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItem;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import com.gigigo.orchextra.core.domain.invocators.DetailContentElementInteractorInvocator;
import com.gigigo.orchextra.core.domain.invocators.GridElementsInteractorInvocator;
import com.gigigo.orchextra.core.domain.invocators.MenuInteractorInvocator;
import java.util.Map;

public class OcmControllerImp implements OcmController {

  private final MenuInteractorInvocator menuInteractorInvocator;
  private final GridElementsInteractorInvocator gridElementsInteractorInvocator;
  private final DetailContentElementInteractorInvocator detailContentElementInteractorInvocator;

  public OcmControllerImp(MenuInteractorInvocator interactorInvocation,
      GridElementsInteractorInvocator gridElementsInteractorInvocator, DetailContentElementInteractorInvocator detailContentElementInteractorInvocator) {

    this.menuInteractorInvocator = interactorInvocation;
    this.gridElementsInteractorInvocator = gridElementsInteractorInvocator;
    this.detailContentElementInteractorInvocator = detailContentElementInteractorInvocator;
  }

  @Override public MenuContentData getMenu(boolean useCache) {
    return menuInteractorInvocator.getMenu(useCache);
  }

  @Override
  public ElementCache getElementCacheBySection(String section) {
    MenuContentData savedMenuContentData = menuInteractorInvocator.getMenu(true);
    if (savedMenuContentData == null || savedMenuContentData.getElementsCache() == null) {
      return null;
    }

    return savedMenuContentData.getElementsCache().get(section);
  }

  @Override public String getContentUrlBySection(String section) {
    ElementCache elementCache = getElementCacheBySection(section);

    if (elementCache == null || elementCache.getRender() == null) {
      return null;
    }

    return elementCache.getRender().getContentUrl();
  }

  @Override public ContentItem getSectionContentById(String section) {
    return detailContentElementInteractorInvocator.getDetailSectionContentBySection(section);
  }

  @Override public void saveSectionContentData(String section, ContentData contentData) {
    detailContentElementInteractorInvocator.saveDetailSectionContentBySection(section, contentData.getContent());

    Map<String, ElementCache> elementsCache = contentData.getElementsCache();
    if (elementsCache != null) {
      for (String key : elementsCache.keySet()) {
        gridElementsInteractorInvocator.saveElementById(key, elementsCache.get(key));
      }
    }
  }

  @Override public void clearCache() {
    menuInteractorInvocator.clear();
    detailContentElementInteractorInvocator.clear();
    gridElementsInteractorInvocator.clear();
  }

  @Override public ElementCache getCachedElement(final String elementUrl) {
    try {
      String slug = getSlug(elementUrl);
      return gridElementsInteractorInvocator.getElementById(slug);
    } catch (Exception e) {
      return null;
    }
  }

  private String getSlug(String elementUrl) {

    try {
      return elementUrl.substring(elementUrl.lastIndexOf("/") + 1, elementUrl.length());
    } catch (Exception ignored) {
      return null;
    }
  }
}

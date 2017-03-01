package com.gigigo.orchextra.core.domain.entities.menus;


import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import java.util.List;
import java.util.Map;

public class MenuContentData {

  private List<MenuContent> menuContentList;

  private Map<String, ElementCache> elementsCache;

  public List<MenuContent> getMenuContentList() {
    return menuContentList;
  }

  public void setMenuContentList(List<MenuContent> menuContentList) {
    this.menuContentList = menuContentList;
  }

  public Map<String, ElementCache> getElementsCache() {
    return elementsCache;
  }

  public void setElementsCache(Map<String, ElementCache> elementsCache) {
    this.elementsCache = elementsCache;
  }
}

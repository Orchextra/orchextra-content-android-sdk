package com.gigigo.orchextra.ocm.dto;

import java.util.List;

public class UiMenuData {

  private List<UiMenu> uiMenuList;
  private boolean fromCache;

  public List<UiMenu> getUiMenuList() {
    return uiMenuList;
  }

  public void setUiMenuList(List<UiMenu> uiMenuList) {
    this.uiMenuList = uiMenuList;
  }

  public boolean isFromCache() {
    return fromCache;
  }

  public void setFromCache(boolean fromCache) {
    this.fromCache = fromCache;
  }
}

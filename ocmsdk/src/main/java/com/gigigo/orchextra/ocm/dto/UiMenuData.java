package com.gigigo.orchextra.ocm.dto;

import java.util.List;

public class UiMenuData {

  private List<UiMenu> uiMenuList;
  private boolean fromCloud;

  public List<UiMenu> getUiMenuList() {
    return uiMenuList;
  }

  public void setUiMenuList(List<UiMenu> uiMenuList) {
    this.uiMenuList = uiMenuList;
  }

  public void setFromCloud(boolean fromCloud) {
    this.fromCloud = fromCloud;
  }

  public boolean isFromCloud() {
    return fromCloud;
  }
}

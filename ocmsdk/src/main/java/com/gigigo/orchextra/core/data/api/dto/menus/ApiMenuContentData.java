package com.gigigo.orchextra.core.data.api.dto.menus;


import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCache;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class ApiMenuContentData {

  @SerializedName("menus")
  private List<ApiMenuContent> menuContentList;

  @SerializedName("elementsCache")
  private Map<String, ApiElementCache> elementsCache;

  public List<ApiMenuContent> getMenuContentList() {
    return menuContentList;
  }

  public Map<String, ApiElementCache> getElementsCache() {
    return elementsCache;
  }
}

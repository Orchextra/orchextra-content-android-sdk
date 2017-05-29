package com.gigigo.orchextra.core.data.api.dto.menus;


import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCache;
import com.gigigo.orchextra.core.data.rxCache.OcmCacheImp;
import com.google.gson.annotations.SerializedName;

import com.mskn73.kache.Kacheable;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class ApiMenuContentData{

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

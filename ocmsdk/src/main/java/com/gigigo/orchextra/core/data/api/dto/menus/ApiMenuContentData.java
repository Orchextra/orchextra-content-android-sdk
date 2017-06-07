package com.gigigo.orchextra.core.data.api.dto.menus;


import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCache;
import com.gigigo.orchextra.core.data.rxCache.OcmCacheImp;
import com.google.gson.annotations.SerializedName;

import com.mskn73.kache.Kacheable;
import com.mskn73.kache.annotations.KacheLife;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

@KacheLife(expiresTime = 1000 * 60 * 60 * 24) // 1 día
public class ApiMenuContentData implements Kacheable {

  @NotNull @Override public String getKey() {
    return OcmCacheImp.MENU_KEY;
  }

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

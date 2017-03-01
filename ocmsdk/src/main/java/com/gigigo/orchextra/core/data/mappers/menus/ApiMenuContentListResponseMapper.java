package com.gigigo.orchextra.core.data.mappers.menus;

import com.gigigo.orchextra.core.data.dto.menus.ApiMenuContent;
import com.gigigo.orchextra.core.data.mappers.elementcache.ApiElementCacheMapper;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContent;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import com.gigigo.orchextra.core.data.dto.elementcache.ApiElementCache;
import com.gigigo.orchextra.core.data.dto.menus.ApiMenuContentData;
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiMenuContentListResponseMapper
    implements ExternalClassToModelMapper<ApiMenuContentData, MenuContentData> {

  private final ApiMenuContentMapper apiMenuContentMapper;
  private final ApiElementCacheMapper apiElementCacheItemMapper;

  public ApiMenuContentListResponseMapper(ApiMenuContentMapper apiMenuContentMapper,
      ApiElementCacheMapper apiElementCacheItemMapper ) {
    this.apiMenuContentMapper = apiMenuContentMapper;
    this.apiElementCacheItemMapper = apiElementCacheItemMapper;
  }

  @Override public MenuContentData externalClassToModel(ApiMenuContentData data) {
    MenuContentData model = new MenuContentData();

    List<MenuContent> menuContentList = new ArrayList<>();
    if (data.getMenuContentList() != null) {
      for (ApiMenuContent apiMenuContent : data.getMenuContentList()) {
        menuContentList.add(apiMenuContentMapper.externalClassToModel(apiMenuContent));
      }
    }
    model.setMenuContentList(menuContentList);

    Map<String, ElementCache> elementCacheItemMap = new HashMap<>();
    if (data.getElementsCache() != null) {
      for (String key : data.getElementsCache().keySet()) {
        ApiElementCache apiElementCacheItem = data.getElementsCache().get(key);

        ElementCache elementCacheItem =
            apiElementCacheItemMapper.externalClassToModel(apiElementCacheItem);

        elementCacheItemMap.put(key, elementCacheItem);
      }
    }
    model.setElementsCache(elementCacheItemMap);

    return model;
  }
}

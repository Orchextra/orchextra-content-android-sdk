package com.gigigo.orchextra.core.data.mappers.contentdata;

import com.gigigo.orchextra.core.data.mappers.elementcache.ApiElementCacheMapper;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.data.dto.content.ApiSectionContentData;
import com.gigigo.orchextra.core.data.dto.elementcache.ApiElementCache;
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ApiContentDataResponseMapper
    implements ExternalClassToModelMapper<ApiSectionContentData, ContentData> {

  private final ApiContentItemMapper apiContentItemMapper;
  private final ApiElementCacheMapper apiElementCacheMapper;

  public ApiContentDataResponseMapper(ApiContentItemMapper apiContentItemMapper,
      ApiElementCacheMapper apiElementCacheMapper) {
    this.apiContentItemMapper = apiContentItemMapper;
    this.apiElementCacheMapper = apiElementCacheMapper;
  }

  @Override public ContentData externalClassToModel(ApiSectionContentData data) {
    ContentData model = new ContentData();

    model.setContent(apiContentItemMapper.externalClassToModel(data.getContent()));

    Map<String, ElementCache> elementCacheMap = new HashMap<>();
    if (data.getElementsCache() != null) {
      Set<String> keySet = data.getElementsCache().keySet();
      for (String key : keySet) {
        ApiElementCache apiElementCache = data.getElementsCache().get(key);

        ElementCache elementCache = apiElementCacheMapper.externalClassToModel(apiElementCache);

        elementCacheMap.put(key, elementCache);
      }
    }

    model.setElementsCache(elementCacheMap);

    return model;
  }
}

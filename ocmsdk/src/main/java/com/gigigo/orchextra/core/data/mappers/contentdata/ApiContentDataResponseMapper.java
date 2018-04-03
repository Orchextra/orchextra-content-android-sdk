package com.gigigo.orchextra.core.data.mappers.contentdata;

import android.util.Log;
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentData;
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCache;
import com.gigigo.orchextra.core.data.mappers.DbMappersKt;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ApiContentDataResponseMapper
    implements ExternalClassToModelMapper<ApiSectionContentData, ContentData> {

  private final ApiContentItemMapper apiContentItemMapper;

  public ApiContentDataResponseMapper(ApiContentItemMapper apiContentItemMapper) {
    this.apiContentItemMapper = apiContentItemMapper;
  }

  @Override public ContentData externalClassToModel(ApiSectionContentData data) {
    final long time = System.currentTimeMillis();

    ContentData model = new ContentData();

    model.setContent(apiContentItemMapper.externalClassToModel(data.getContent()));

    Map<String, ElementCache> elementCacheMap = new HashMap<>();
    if (data.getElementsCache() != null) {
      Set<String> keySet = data.getElementsCache().keySet();
      for (String key : keySet) {
        ApiElementCache apiElementCache = data.getElementsCache().get(key);

        ElementCache elementCache = DbMappersKt.toElementCache(apiElementCache); //apiElementCacheMapper.externalClassToModel(apiElementCache);

        elementCacheMap.put(key, elementCache);
      }
    }

    model.setElementsCache(elementCacheMap);

    model.setVersion(data.getVersion());
    model.setExpiredAt(data.getExpireAt());
    model.setFromCloud(data.isFromCloud());

    Log.v("TT - ApiContentData", (System.currentTimeMillis() - time) / 1000 + "");

    return model;
  }
}

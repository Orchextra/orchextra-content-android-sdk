package com.gigigo.orchextra.core.data.api.mappers.elementcache;


import com.gigigo.orchextra.core.data.api.mappers.elements.ApiElementSegmentationMapper;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheType;
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCache;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import java.util.ArrayList;
import java.util.List;

public class ApiElementCacheMapper
    implements ExternalClassToModelMapper<ApiElementCache, ElementCache> {

  private final ApiElementCacheRenderMapper apiElementCacheItemRenderMapper;
  private final ApiElementCachePreviewMapper apiElementCachePreviewMapper;
  private final ApiElementCacheShareMapper apiElementCacheShareMapper;
  private final ApiElementSegmentationMapper apiElementSegmentationMapper;

  public ApiElementCacheMapper(ApiElementCacheRenderMapper apiElementCacheItemRenderMapper,
      ApiElementCachePreviewMapper apiElementCachePreviewMapper,
      ApiElementCacheShareMapper apiElementCacheShareMapper,
      ApiElementSegmentationMapper apiElementSegmentationMapper) {
    this.apiElementCacheItemRenderMapper = apiElementCacheItemRenderMapper;
    this.apiElementCachePreviewMapper = apiElementCachePreviewMapper;
    this.apiElementCacheShareMapper = apiElementCacheShareMapper;
    this.apiElementSegmentationMapper = apiElementSegmentationMapper;
  }

  @Override public ElementCache externalClassToModel(ApiElementCache data) {
    ElementCache model = new ElementCache();

    model.setSlug(data.getSlug());
    model.setType(ElementCacheType.convertStringToEnum(data.getType()));
    model.setName(data.getName());
    model.setUpdateAt(data.getUpdatedAt());

    List<String> tagList = new ArrayList<>();
    if (data.getTags() != null) {
      for (String tag : data.getTags()) {
        tagList.add(tag);
      }
      model.setTags(tagList);
    }

    if (data.getRender() != null) {
      model.setRender(apiElementCacheItemRenderMapper.externalClassToModel(data.getRender()));
    }

    if (data.getPreview() != null) {
      model.setPreview(apiElementCachePreviewMapper.externalClassToModel(data.getPreview()));
    }

    if (data.getShare() != null) {
      model.setShare(apiElementCacheShareMapper.externalClassToModel(data.getShare()));
    }

    if (data.getSegmentation() != null) {
      model.setSegmentation(apiElementSegmentationMapper.externalClassToModel(data.getSegmentation()));
    }

    return model;
  }
}

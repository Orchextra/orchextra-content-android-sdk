package com.gigigo.orchextra.core.data.mappers.elements;

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.orchextra.core.data.dto.elements.ApiElementData;
import com.gigigo.orchextra.core.data.mappers.elementcache.ApiElementCacheMapper;
import com.gigigo.orchextra.core.domain.entities.elements.ElementData;

public class ApiElementDataMapper implements
    ExternalClassToModelMapper<ApiElementData, ElementData> {

  private final ApiElementCacheMapper apiElementCacheMapper;

  public ApiElementDataMapper(ApiElementCacheMapper apiElementCacheMapper) {
    this.apiElementCacheMapper = apiElementCacheMapper;
  }

  @Override public ElementData externalClassToModel(ApiElementData data) {
    ElementData model = new ElementData();

    model.setElement(apiElementCacheMapper.externalClassToModel(data.getElement()));

    return model;
  }
}

package com.gigigo.orchextra.core.data.api.mappers.elements;

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementCustomProperty;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElement;
import com.gigigo.orchextra.core.domain.entities.elements.ElementCustomProperties;
import com.gigigo.orchextra.core.domain.entities.elements.Element;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ApiElementMapper implements ExternalClassToModelMapper<ApiElement, Element> {

  private final ApiElementSegmentationMapper apiMenuItemSegmentation;
  private final ApiElementCustomPropertyMapper apiElementCustomPropertyMapper;
  private final ApiElementSectionViewMapper apiMenuItemViewMapper;

  public ApiElementMapper(ApiElementSegmentationMapper apiMenuItemSegmentation,
      ApiElementCustomPropertyMapper apiElementCustomPropertyMapper,
      ApiElementSectionViewMapper apiMenuItemViewMapper) {
    this.apiMenuItemSegmentation = apiMenuItemSegmentation;
    this.apiElementCustomPropertyMapper = apiElementCustomPropertyMapper;
    this.apiMenuItemViewMapper = apiMenuItemViewMapper;
  }

  @Override public Element externalClassToModel(ApiElement data) {
    Element model = new Element();

    if (data.getSlug() == null || data.getSectionView() == null || data.getElementUrl() == null) {
      return null;
    }

    model.setSlug(data.getSlug());
    model.setElementUrl(data.getElementUrl());
    model.setName(data.getName());
    model.setDates(data.getDates());

    if (data.getSegmentation() != null) {
      model.setSegmentation(apiMenuItemSegmentation.externalClassToModel(data.getSegmentation()));
    }

    ElementCustomProperties customProperties = new ElementCustomProperties();
    if(data.getCustomProperties() != null) {
      customProperties.setProperties(data.getCustomProperties().getProperties());

      model.setCustomProperties(customProperties);
    }

    if (data.getSectionView() != null) {
      model.setSectionView(apiMenuItemViewMapper.externalClassToModel(data.getSectionView()));
    }

    List<String> tagList = new ArrayList<>();
    if (data.getTags() != null) {
      for (String tag : data.getTags()) {
        tagList.add(tag);
      }
    }
    model.setTags(tagList);

    return model;
  }
}

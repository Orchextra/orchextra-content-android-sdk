package com.gigigo.orchextra.core.data.mappers.elements;

import com.gigigo.orchextra.core.data.dto.elements.ApiElement;
import com.gigigo.orchextra.core.domain.entities.elements.Element;
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import java.util.ArrayList;
import java.util.List;

public class ApiElementMapper implements ExternalClassToModelMapper<ApiElement, Element> {

  private final ApiElementSegmentationMapper apiMenuItemSegmentation;
  private final ApiElementSectionViewMapper apiMenuItemViewMapper;

  public ApiElementMapper(ApiElementSegmentationMapper apiMenuItemSegmentation,
      ApiElementSectionViewMapper apiMenuItemViewMapper) {
    this.apiMenuItemSegmentation = apiMenuItemSegmentation;
    this.apiMenuItemViewMapper = apiMenuItemViewMapper;
  }

  @Override public Element externalClassToModel(ApiElement data) {
    Element model = new Element();

    model.setSlug(data.getSlug());
    model.setElementUrl(data.getElementUrl());

    if (data.getSegmentation() != null) {
      model.setSegmentation(apiMenuItemSegmentation.externalClassToModel(data.getSegmentation()));
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

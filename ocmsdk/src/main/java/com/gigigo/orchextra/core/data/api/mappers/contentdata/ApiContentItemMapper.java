package com.gigigo.orchextra.core.data.api.mappers.contentdata;

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.orchextra.core.data.api.dto.content.ApiContentItem;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElement;
import com.gigigo.orchextra.core.data.api.mappers.elements.ApiElementMapper;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItem;
import com.gigigo.orchextra.core.domain.entities.elements.Element;
import java.util.ArrayList;
import java.util.List;

public class ApiContentItemMapper
    implements ExternalClassToModelMapper<ApiContentItem, ContentItem> {

  private final ApiContentItemLayoutMapper apiContentItemLayoutMapper;
  private final ApiElementMapper apiElementMapper;

  public ApiContentItemMapper(ApiContentItemLayoutMapper apiContentItemLayoutMapper,
      ApiElementMapper apiElementMapper) {
    this.apiContentItemLayoutMapper = apiContentItemLayoutMapper;
    this.apiElementMapper = apiElementMapper;
  }

  @Override public ContentItem externalClassToModel(ApiContentItem data) {
    ContentItem model = new ContentItem();

    model.setSlug(data.getSlug());
    model.setType(data.getType());

    List<String> tagList = new ArrayList<>();
    if (data.getTags() != null) {
      for (String tag : data.getTags()) {
        tagList.add(tag);
      }
    }
    model.setTags(tagList);

    model.setLayout(apiContentItemLayoutMapper.externalClassToModel(data.getLayout()));

    List<Element> elementList = new ArrayList<>();
    if (data.getElements() != null) {
      for (ApiElement apiElement : data.getElements()) {
        Element element = apiElementMapper.externalClassToModel(apiElement);
        if (element != null) {
          elementList.add(element);
        }
      }
    }
    model.setElements(elementList);

    return model;
  }
}

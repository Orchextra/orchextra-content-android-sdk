package com.gigigo.orchextra.core.data.api.mappers.contentdata;

import com.gigigo.orchextra.core.data.api.dto.content.ApiContentItemPattern;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItemPattern;
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;

public class ApiContentItemPatternMapper
    implements ExternalClassToModelMapper<ApiContentItemPattern, ContentItemPattern> {

  @Override public ContentItemPattern externalClassToModel(ApiContentItemPattern data) {
    ContentItemPattern model = new ContentItemPattern();

    model.setRow(data.getRow());
    model.setColumn(data.getColumn());

    return model;
  }
}

package com.gigigo.orchextra.core.data.mappers.elements;

import com.gigigo.orchextra.core.data.dto.elements.ApiElementSegmentation;
import com.gigigo.orchextra.core.domain.entities.elements.ElementSegmentation;
import com.gigigo.orchextra.core.domain.entities.menus.RequiredAuthoritation;
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;

public class ApiElementSegmentationMapper
    implements ExternalClassToModelMapper<ApiElementSegmentation, ElementSegmentation> {

  @Override public ElementSegmentation externalClassToModel(ApiElementSegmentation data) {
    ElementSegmentation model = new ElementSegmentation();

    model.setRequiredAuth(RequiredAuthoritation.convert(data.getRequiredAuth()));

    return model;
  }
}

package com.gigigo.orchextra.core.data.api.mappers.elements;

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementPropertyOption;
import com.gigigo.orchextra.core.domain.entities.elements.ElementPropertyOption;

public class ApiElementPropertyOptionMapper
    implements ExternalClassToModelMapper<ApiElementPropertyOption, ElementPropertyOption> {

  @Override public ElementPropertyOption externalClassToModel(ApiElementPropertyOption data) {
    ElementPropertyOption model = new ElementPropertyOption();
    model.setValue(data.getValue());
    model.setLabel(data.getLabel());

    return model;
  }
}

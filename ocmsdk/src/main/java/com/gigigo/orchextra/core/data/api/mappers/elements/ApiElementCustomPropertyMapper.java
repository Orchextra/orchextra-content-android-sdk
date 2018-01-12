package com.gigigo.orchextra.core.data.api.mappers.elements;

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementCustomProperty;
import com.gigigo.orchextra.core.domain.entities.elements.ElementCustomProperty;

public class ApiElementCustomPropertyMapper
    implements ExternalClassToModelMapper<ApiElementCustomProperty, ElementCustomProperty> {

  @Override public ElementCustomProperty externalClassToModel(ApiElementCustomProperty data) {
    ElementCustomProperty model = new ElementCustomProperty();

    model.setCustomProperties(data.getCustomProperties());

    return model;
  }
}

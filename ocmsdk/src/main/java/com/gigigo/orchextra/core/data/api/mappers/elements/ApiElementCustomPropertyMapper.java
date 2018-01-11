package com.gigigo.orchextra.core.data.api.mappers.elements;

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementCustomProperty;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementPropertyOption;
import com.gigigo.orchextra.core.domain.entities.elements.ElementCustomProperty;
import com.gigigo.orchextra.core.domain.entities.elements.ElementPropertyOption;
import java.util.ArrayList;
import java.util.List;

public class ApiElementCustomPropertyMapper
    implements ExternalClassToModelMapper<ApiElementCustomProperty, ElementCustomProperty> {

  private final ApiElementPropertyOptionMapper apiElementPropertyOptionMapper;

  public ApiElementCustomPropertyMapper(
      ApiElementPropertyOptionMapper apiElementPropertyOptionMapper) {
    this.apiElementPropertyOptionMapper = apiElementPropertyOptionMapper;
  }

  @Override public ElementCustomProperty externalClassToModel(ApiElementCustomProperty data) {
    ElementCustomProperty model = new ElementCustomProperty();
    model.setName(data.getName());
    model.setLabel(data.getLabel());
    model.setDescription(data.getDescription());
    model.setType(data.getType());
    model.setDef(data.getDef());

    List<ElementPropertyOption> optionsList = new ArrayList<>();
    for (ApiElementPropertyOption option : data.getOptions()) {
      optionsList.add(apiElementPropertyOptionMapper.externalClassToModel(option));
    }

    model.setOptions(optionsList);

    return model;
  }
}

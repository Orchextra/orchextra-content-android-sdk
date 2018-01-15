package com.gigigo.orchextra.core.data.api.mappers.elements

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementCustomProperty
import com.gigigo.orchextra.core.domain.entities.elements.ElementCustomProperties

class ApiElementCustomPropertyMapper : ExternalClassToModelMapper<ApiElementCustomProperty, ElementCustomProperties> {

  override fun externalClassToModel(data: ApiElementCustomProperty): ElementCustomProperties {
    val model = ElementCustomProperties()

    model.properties = data.properties ?: HashMap()

    return model
  }
}

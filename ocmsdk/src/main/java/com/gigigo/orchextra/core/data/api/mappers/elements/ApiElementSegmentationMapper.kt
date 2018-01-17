package com.gigigo.orchextra.core.data.api.mappers.elements

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementSegmentation
import com.gigigo.orchextra.core.domain.entities.elements.ElementSegmentation
import com.gigigo.orchextra.core.domain.entities.menus.RequiredAuthoritation

class ApiElementSegmentationMapper : ExternalClassToModelMapper<ApiElementSegmentation, ElementSegmentation> {

  override fun externalClassToModel(data: ApiElementSegmentation): ElementSegmentation {
    val model = ElementSegmentation()

    model.requiredAuth = RequiredAuthoritation.convert(data.requiredAuth)

    return model
  }
}

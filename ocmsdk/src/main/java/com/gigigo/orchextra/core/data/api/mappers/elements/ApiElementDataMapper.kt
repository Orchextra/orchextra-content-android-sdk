package com.gigigo.orchextra.core.data.api.mappers.elements

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementData
import com.gigigo.orchextra.core.data.api.mappers.elementcache.ApiElementCacheMapper
import com.gigigo.orchextra.core.domain.entities.elements.ElementData

class ApiElementDataMapper(
    private val apiElementCacheMapper: ApiElementCacheMapper) : ExternalClassToModelMapper<ApiElementData, ElementData> {

  override fun externalClassToModel(data: ApiElementData): ElementData {
    val model = ElementData()

    model.element = apiElementCacheMapper.externalClassToModel(data.element)

    return model
  }
}

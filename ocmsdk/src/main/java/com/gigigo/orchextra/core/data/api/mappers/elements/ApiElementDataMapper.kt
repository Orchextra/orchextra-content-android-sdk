package com.gigigo.orchextra.core.data.api.mappers.elements

import android.util.Log
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementData
import com.gigigo.orchextra.core.data.api.mappers.elementcache.ApiElementCacheMapper
import com.gigigo.orchextra.core.domain.entities.elements.ElementData

class ApiElementDataMapper(
    private val apiElementCacheMapper: ApiElementCacheMapper) : ExternalClassToModelMapper<ApiElementData, ElementData> {

  override fun externalClassToModel(data: ApiElementData): ElementData {
    val time = System.currentTimeMillis()

    val model = ElementData()

    model.element = apiElementCacheMapper.externalClassToModel(data.element)

    val currentTime = System.currentTimeMillis() - time
    Log.v("TT - ApiElementData", ("" + currentTime/1000))


    return model
  }
}

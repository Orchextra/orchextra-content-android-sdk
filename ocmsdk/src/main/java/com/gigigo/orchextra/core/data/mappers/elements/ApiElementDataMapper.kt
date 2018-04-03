package com.gigigo.orchextra.core.data.mappers.elements

import android.util.Log
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementData
import com.gigigo.orchextra.core.data.mappers.toElementCache
import com.gigigo.orchextra.core.domain.entities.elements.ElementData

class ApiElementDataMapper : ExternalClassToModelMapper<ApiElementData, ElementData> {

  override fun externalClassToModel(data: ApiElementData): ElementData {
    val time = System.currentTimeMillis()

    val model = ElementData()

    model.element = data.element.toElementCache() //apiElementCacheMapper.externalClassToModel(data.element)

    val currentTime = System.currentTimeMillis() - time
    Log.v("TT - ApiElementData", ("" + currentTime/1000))

    return model
  }
}

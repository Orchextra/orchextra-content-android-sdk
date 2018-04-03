package com.gigigo.orchextra.core.data.mappers.elements

import android.util.Log
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementSectionView
import com.gigigo.orchextra.core.domain.entities.elements.ElementSectionView

class ApiElementSectionViewMapper : ExternalClassToModelMapper<ApiElementSectionView, ElementSectionView> {

  override fun externalClassToModel(data: ApiElementSectionView): ElementSectionView {
    val time = System.currentTimeMillis()
    val model = ElementSectionView()

    with(model) {
      text = data.text
      imageUrl = data.imageUrl
      imageThumb = data.imageThumb
    }

    val currentTime = System.currentTimeMillis() - time
    Log.v("TT - ApiElemSectionView", ("" + currentTime/1000))

    return model
  }
}

package com.gigigo.orchextra.core.data.api.mappers.elementcache

import android.util.Log
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCachePreview
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheBehaviour
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCachePreview

class ApiElementCachePreviewMapper : ExternalClassToModelMapper<ApiElementCachePreview, ElementCachePreview> {

  override fun externalClassToModel(data: ApiElementCachePreview): ElementCachePreview {
    val time = System.currentTimeMillis()

    val model = ElementCachePreview()

    with(model) {
      text = data.text
      imageUrl = data.imageUrl
      imageThumb = data.imageThumb
      behaviour = ElementCacheBehaviour.convertStringToEnum(data.behaviour)
    }

    val currentTime = System.currentTimeMillis() - time
    Log.v("TT - ApiElemCachePreview", ("" + currentTime/1000))

    return model
  }
}

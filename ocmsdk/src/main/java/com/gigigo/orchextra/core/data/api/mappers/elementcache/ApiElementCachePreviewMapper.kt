package com.gigigo.orchextra.core.data.api.mappers.elementcache

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCachePreview
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheBehaviour
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCachePreview

class ApiElementCachePreviewMapper : ExternalClassToModelMapper<ApiElementCachePreview, ElementCachePreview> {

  override fun externalClassToModel(data: ApiElementCachePreview): ElementCachePreview {
    val model = ElementCachePreview()

    with(model) {
      text = data.text
      imageUrl = data.imageUrl
      imageThumb = data.imageThumb
      behaviour = ElementCacheBehaviour.convertStringToEnum(data.behaviour)
    }

    return model
  }
}

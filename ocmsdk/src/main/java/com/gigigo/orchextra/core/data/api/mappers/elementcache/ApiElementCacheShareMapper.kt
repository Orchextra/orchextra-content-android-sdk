package com.gigigo.orchextra.core.data.api.mappers.elementcache

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCacheShare
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheShare

class ApiElementCacheShareMapper : ExternalClassToModelMapper<ApiElementCacheShare, ElementCacheShare> {

  override fun externalClassToModel(data: ApiElementCacheShare): ElementCacheShare {
    val model = ElementCacheShare()

    with(model) {
      url = data.url
      text = data.text
    }

    return model
  }
}

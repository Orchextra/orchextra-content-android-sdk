package com.gigigo.orchextra.core.data.api.mappers.elementcache

import android.util.Log
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCacheShare
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheShare

class ApiElementCacheShareMapper : ExternalClassToModelMapper<ApiElementCacheShare, ElementCacheShare> {

  override fun externalClassToModel(data: ApiElementCacheShare): ElementCacheShare {
    val time = System.currentTimeMillis()

    val model = ElementCacheShare()

    with(model) {
      url = data.url
      text = data.text
    }

    val currentTime = System.currentTimeMillis() - time
    Log.v("TT - ApiElemCacheShare", ("" + currentTime/1000))

    return model
  }
}

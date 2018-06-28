package com.gigigo.orchextra.core.data.api.mappers.elementcache

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper
import com.gigigo.orchextra.core.data.api.dto.elementcache.CidKeyData
import com.gigigo.orchextra.core.domain.entities.elementcache.CidKey

class CidKeyDataMapper : ExternalClassToModelMapper<CidKeyData, CidKey> {

  override fun externalClassToModel(data: CidKeyData?): CidKey {
    val model = CidKey()

    data?.let {
      model.siteName = data.siteName
    }

    return model
  }
}
package com.gigigo.orchextra.core.data.api.mappers.elementcache

import android.util.Log
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper
import com.gigigo.orchextra.core.data.api.dto.elementcache.FederatedAuthorizationData
import com.gigigo.orchextra.core.domain.entities.elementcache.FederatedAuthorization

class FederatedAuthorizationDataMapper(
    private val mapper: CidKeyDataMapper) : ExternalClassToModelMapper<FederatedAuthorizationData, FederatedAuthorization> {

  override fun externalClassToModel(data: FederatedAuthorizationData?): FederatedAuthorization {
    val time = System.currentTimeMillis()

    val model = FederatedAuthorization()

    data?.let {
      model.isActive = data.active
      model.type = data.type
      model.keys = mapper.externalClassToModel(data.keys)
    }

    Log.v("TT - FAData", ((System.currentTimeMillis() - time) / 1000).toString() + "")

    return model
  }
}
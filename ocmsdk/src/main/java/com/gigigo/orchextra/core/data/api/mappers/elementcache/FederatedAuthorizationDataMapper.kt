package com.gigigo.orchextra.core.data.api.mappers.elementcache

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper
import com.gigigo.orchextra.core.data.api.dto.elementcache.FederatedAuthorizationData
import com.gigigo.orchextra.core.domain.entities.elementcache.FederatedAuthorization

class FederatedAuthorizationDataMapper(
    private val mapper: CidKeyDataMapper) : ExternalClassToModelMapper<FederatedAuthorizationData, FederatedAuthorization> {

  override fun externalClassToModel(data: FederatedAuthorizationData?): FederatedAuthorization {
    val model = FederatedAuthorization()

    data?.let {
      model.isActive = data.active
      model.type = data.type
      model.keys = mapper.externalClassToModel(data.keys)
    }
    return model
  }
}
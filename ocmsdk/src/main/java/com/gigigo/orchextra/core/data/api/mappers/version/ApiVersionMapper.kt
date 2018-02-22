package com.gigigo.orchextra.core.data.api.mappers.version

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper
import com.gigigo.orchextra.core.data.api.dto.versioning.ApiVersionKache
import com.gigigo.orchextra.core.domain.entities.version.VersionData
import orchextra.javax.inject.Inject
import orchextra.javax.inject.Singleton

@Singleton
class ApiVersionMapper @Inject
constructor() : ExternalClassToModelMapper<ApiVersionKache, VersionData> {

  override fun externalClassToModel(data: ApiVersionKache?): VersionData {
    val versionData = VersionData()
    data?.let {
      versionData.version = data.version
    }
    return versionData
  }
}
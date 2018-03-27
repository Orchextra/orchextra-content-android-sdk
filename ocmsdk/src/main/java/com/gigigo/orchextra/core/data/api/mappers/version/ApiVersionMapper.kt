package com.gigigo.orchextra.core.data.api.mappers.version

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper
import com.gigigo.orchextra.core.data.api.dto.versioning.ApiVersionData
import com.gigigo.orchextra.core.domain.entities.version.VersionData
import orchextra.javax.inject.Inject
import orchextra.javax.inject.Singleton

@Singleton
class ApiVersionMapper @Inject
constructor() : ExternalClassToModelMapper<ApiVersionData, VersionData> {

  override fun externalClassToModel(data: ApiVersionData?): VersionData {
    val versionData = VersionData()
    data?.let {
      versionData.version = data.version
    }
    return versionData
  }
}

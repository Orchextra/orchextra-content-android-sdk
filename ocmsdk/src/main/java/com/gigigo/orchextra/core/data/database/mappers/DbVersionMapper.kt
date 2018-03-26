package com.gigigo.orchextra.core.data.database.mappers

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper
import com.gigigo.orchextra.core.data.database.entities.DbVersionData
import com.gigigo.orchextra.core.domain.entities.version.VersionData
import orchextra.javax.inject.Inject
import orchextra.javax.inject.Singleton

@Singleton
class DbVersionMapper @Inject
constructor() : ExternalClassToModelMapper<DbVersionData, VersionData> {

  override fun externalClassToModel(data: DbVersionData?): VersionData {
    val versionData = VersionData()
    data?.let {
      versionData.version = data.version
    }
    return versionData
  }
}
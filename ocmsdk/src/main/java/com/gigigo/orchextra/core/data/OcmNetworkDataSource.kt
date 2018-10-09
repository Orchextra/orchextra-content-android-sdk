package com.gigigo.orchextra.core.data

import com.gigigo.orchextra.core.data.api.services.OcmApiService
import com.gigigo.orchextra.core.data.mappers.toContentData
import com.gigigo.orchextra.core.data.mappers.toMenuContentData
import com.gigigo.orchextra.core.data.rxException.ApiMenuNotFoundException
import com.gigigo.orchextra.core.data.rxException.ApiSectionNotFoundException
import com.gigigo.orchextra.core.data.rxException.NetworkConnectionException
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData
import orchextra.javax.inject.Inject
import orchextra.javax.inject.Singleton
import timber.log.Timber

@Singleton
class OcmNetworkDataSource @Inject constructor(private val ocmApiService: OcmApiService,
    private val dbDataSource: OcmDbDataSource) {

  @Throws(ApiMenuNotFoundException::class, NetworkConnectionException::class)
  fun getMenus(): MenuContentData {
    try {
      ocmApiService.menu.execute().body()?.let {
        dbDataSource.saveMenus(it.result)
        return it.result.toMenuContentData()
      }
    } catch (e: Exception) {
      Timber.e("getMenus()")
      throw ApiMenuNotFoundException()
    }
    throw NetworkConnectionException()
  }

  @Throws(ApiSectionNotFoundException::class, NetworkConnectionException::class)
  fun getSectionElements(section: String): ContentData {
    try {
      ocmApiService.getSectionData(section).execute().body()?.let {
        dbDataSource.putSection(it.result, section)
        return it.result.toContentData()
      }
    } catch (e: Exception) {
      Timber.e(e, "getSectionElements()")
      throw ApiSectionNotFoundException()
    }
    throw NetworkConnectionException()
  }
}
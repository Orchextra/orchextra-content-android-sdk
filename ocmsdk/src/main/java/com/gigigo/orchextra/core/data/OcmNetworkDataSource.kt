package com.gigigo.orchextra.core.data

import com.gigigo.orchextra.core.data.api.services.OcmApiService
import com.gigigo.orchextra.core.data.mappers.toMenuContentData
import com.gigigo.orchextra.core.data.rxException.ApiMenuNotFoundException
import com.gigigo.orchextra.core.data.rxException.NetworkConnectionException
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData
import orchextra.javax.inject.Inject
import orchextra.javax.inject.Singleton
import timber.log.Timber

@Singleton
class OcmNetworkDataSource @Inject constructor(private val ocmApiService: OcmApiService,
    private val dbDataSource: OcmDbDataSource) {


  fun getMenus(): MenuContentData {
    try {
      ocmApiService.menu.execute().body()?.let {
        dbDataSource.saveMenus(it.result)
        return it.result.toMenuContentData()
      }
    } catch (e: Exception) {
      Timber.e(e, "getMenus()")
      throw ApiMenuNotFoundException()
    }
    throw NetworkConnectionException()
  }
}
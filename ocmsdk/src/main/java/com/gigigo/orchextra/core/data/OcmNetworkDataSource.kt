package com.gigigo.orchextra.core.data

import com.gigigo.orchextra.core.AppExecutors
import com.gigigo.orchextra.core.data.api.services.OcmApiService
import com.gigigo.orchextra.core.data.mappers.toContentData
import com.gigigo.orchextra.core.data.mappers.toMenuContentData
import com.gigigo.orchextra.core.data.rxException.ApiMenuNotFoundException
import com.gigigo.orchextra.core.data.rxException.NetworkConnectionException
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OcmNetworkDataSource @Inject constructor(
    private val ocmApiService: OcmApiService,
    private val dbDataSource: OcmDbDataSource, private val appExecutors: AppExecutors
) {

    @Throws(ApiMenuNotFoundException::class, NetworkConnectionException::class)
    fun getMenus(): MenuContentData {
        try {
            ocmApiService.menu.execute().body()?.let {
                appExecutors.diskIO().execute {
                    dbDataSource.saveMenus(it.result)
                }
                return it.result.toMenuContentData()
            }
        } catch (e: Exception) {
            Timber.e("getMenus()")
            throw ApiMenuNotFoundException()
        }
        throw NetworkConnectionException()
    }

    @Throws(NetworkConnectionException::class)
    fun getSectionElements(section: String): ContentData {
        try {
            ocmApiService.getSectionData(section).execute().body()?.let {

                appExecutors.diskIO().execute {
                    dbDataSource.putSection(it.result, section)
                }

                return it.result.toContentData()
            }
        } catch (e: Exception) {
            Timber.e(e, "getSectionElements()")
        }
        throw NetworkConnectionException()
    }
}
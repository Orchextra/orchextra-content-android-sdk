package com.gigigo.orchextra.core.data

import com.gigigo.orchextra.core.AppExecutors
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentData
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementData
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentData
import com.gigigo.orchextra.core.data.database.OcmDatabase
import com.gigigo.orchextra.core.data.database.entities.DbElementCache
import com.gigigo.orchextra.core.data.database.entities.DbMenuContentData
import com.gigigo.orchextra.core.data.database.entities.DbMenuElementJoin
import com.gigigo.orchextra.core.data.database.entities.DbSectionElementJoin
import com.gigigo.orchextra.core.data.mappers.toContentData
import com.gigigo.orchextra.core.data.mappers.toDbElement
import com.gigigo.orchextra.core.data.mappers.toDbElementCache
import com.gigigo.orchextra.core.data.mappers.toDbMenuContent
import com.gigigo.orchextra.core.data.mappers.toDbSectionContentData
import com.gigigo.orchextra.core.data.mappers.toMenuContentData
import com.gigigo.orchextra.core.data.rxException.ApiSectionNotFoundException
import com.gigigo.orchextra.core.data.rxException.ClearCacheException
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData
import orchextra.javax.inject.Inject
import orchextra.javax.inject.Singleton
import timber.log.Timber

@Singleton
class OcmDbDataSource @Inject constructor(private val ocmDatabase: OcmDatabase,
    private val appExecutors: AppExecutors) {

  fun getMenus(): MenuContentData {

    val dbMenuContentList = ocmDatabase.menuDao().fetchMenus()
    val dbElementCacheList = ocmDatabase.elementCacheDao().fetchElementsCache()

    for (dbMenuContent in dbMenuContentList) {
      val dbElementList = ocmDatabase.elementDao().fetchMenuElements(dbMenuContent.slug)
      dbMenuContent.elements = dbElementList

      for (dbElement in dbElementList) {
        val dbScheduleDatesList = ocmDatabase.scheduleDatesDao().fetchSchedule(dbElement.slug)
        dbElement.dates = dbScheduleDatesList
      }
    }

    val dbMenuContentData = DbMenuContentData()
    dbMenuContentData.menuContentList = dbMenuContentList

    val dbElementCacheMap = HashMap<String, DbElementCache>(dbElementCacheList.size)
    for (dbElementCache in dbElementCacheList) {
      dbElementCacheMap[dbElementCache.key] = dbElementCache
    }
    dbMenuContentData.elementsCache = dbElementCacheMap

    return dbMenuContentData.toMenuContentData()
  }

  fun saveMenus(apiMenuContentData: ApiMenuContentData) {

    appExecutors.diskIO().execute {
      try {
        ocmDatabase.menuDao().deleteAll()
        ocmDatabase.elementDao().deleteAll()

        apiMenuContentData.menuContentList?.forEach { apiMenuContent ->
          val dbMenuContent = apiMenuContent.toDbMenuContent()
          ocmDatabase.menuDao().insertMenu(dbMenuContent)

          dbMenuContent.elements?.forEachIndexed { _, dbElement ->
            ocmDatabase.elementDao().insertElement(dbElement)

            for (scheduleDate in dbElement.dates) {
              scheduleDate.slug = dbElement.slug
              ocmDatabase.scheduleDatesDao().insertSchedule(scheduleDate)
            }

            val dbMenuElementJoin = DbMenuElementJoin(dbMenuContent.slug, dbElement.slug)
            ocmDatabase.elementDao().insertMenuElement(dbMenuElementJoin)
          }
        }
        var index = 0
        apiMenuContentData.elementsCache?.forEach { (key, element) ->
          val apiElementData = ApiElementData(element)
          putDetail(apiElementData, key, index)
          index++
        }
      } catch (e: Exception) {
        Timber.e(e, "saveMenus()")
      }
    }
  }

  private fun putDetail(apiElementData: ApiElementData, key: String, index: Int) {
    appExecutors.diskIO().execute {
      try {
        val elementCacheData = apiElementData.element.toDbElementCache(key, index)
        ocmDatabase.elementCacheDao().insertElementCache(elementCacheData)
      } catch (e: Exception) {
        Timber.e(e, "putDetail()")
      }
    }
  }

  @Throws(ApiSectionNotFoundException::class)
  fun getSectionElements(section: String): ContentData {

    val dbSectionContentData = ocmDatabase.sectionDao().fetchSectionContentData(section)
        ?: throw ApiSectionNotFoundException()

    val dbSectionContentDataElementList = ocmDatabase.sectionDao()
        .fetchSectionElements(dbSectionContentData.content!!.slug)
    dbSectionContentData.content!!.elements = dbSectionContentDataElementList

    val elementCaches = HashMap<String, DbElementCache>()
    for ((slug, _, _, _, elementUrl) in dbSectionContentDataElementList) {
      val elementCache = ocmDatabase.elementCacheDao().fetchElementCache(slug)
      if (elementCache != null) {
        elementUrl?.let {
          elementCaches[elementUrl] = elementCache
        }
      }
    }
    dbSectionContentData.elementsCache = elementCaches

    if (dbSectionContentData.elementsCache?.isEmpty() == true) {
      throw ClearCacheException()
    }

    return dbSectionContentData.toContentData()
  }

  fun putSection(apiSectionContentData: ApiSectionContentData, key: String) {

    appExecutors.diskIO().execute {
      try {
        val dbSectionContentData = apiSectionContentData.toDbSectionContentData(key)
        ocmDatabase.sectionDao().insertSectionContentData(dbSectionContentData)

        val sectionContentItem = apiSectionContentData.content
        if (sectionContentItem != null) {

          sectionContentItem.elements?.forEachIndexed { index, apiElement ->
            ocmDatabase.elementDao().insertElement(apiElement.toDbElement(index))
            val dbSectionElementJoin = DbSectionElementJoin(sectionContentItem.slug,
                apiElement.slug)
            ocmDatabase.sectionDao().insertSectionElement(dbSectionElementJoin)
          }
        }

        var index = 0
        if (apiSectionContentData.elementsCache != null) {
          for ((key, value) in apiSectionContentData.elementsCache) {
            val apiElementData = ApiElementData(value)
            putDetail(apiElementData, key, index)
            index++
          }
        }
      } catch (e: Exception) {
        Timber.e(e, "putSection()")
      }
    }
  }

  fun deleteElementCache() {
    ocmDatabase.elementCacheDao().deleteAll()
  }
}
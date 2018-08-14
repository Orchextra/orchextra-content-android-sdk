package com.gigigo.orchextra.core.data

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
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData
import orchextra.javax.inject.Inject
import orchextra.javax.inject.Singleton

@Singleton
class OcmDbDataSource @Inject constructor(private val ocmDatabase: OcmDatabase) {

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

    ocmDatabase.menuDao().deleteAll()

    apiMenuContentData.menuContentList?.forEach { apiMenuContent ->
      val dbMenuContent = apiMenuContent.toDbMenuContent()
      ocmDatabase.menuDao().insertMenu(dbMenuContent)

      dbMenuContent.elements?.forEachIndexed { index, dbElement ->
        dbElement.listIndex = index
        ocmDatabase.elementDao().insertElement(dbElement)

        for (scheduleDate in dbElement.dates) {
          scheduleDate.slug = dbElement.slug
          ocmDatabase.scheduleDatesDao().insertSchedule(scheduleDate)
        }

        val dbMenuElementJoin = DbMenuElementJoin(dbMenuContent.slug, dbElement.slug)
        ocmDatabase.elementDao().insertMenuElement(dbMenuElementJoin)
      }
    }

    ocmDatabase.elementCacheDao().deleteAll()
    apiMenuContentData.elementsCache?.forEach { (key, element) ->
      val apiElementData = ApiElementData(element)
      putDetail(apiElementData, key)
    }
  }

  private fun putDetail(apiElementData: ApiElementData, key: String) {
    val elementCacheData = apiElementData.element.toDbElementCache(key)
    ocmDatabase.elementCacheDao().insertElementCache(elementCacheData)
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

    return dbSectionContentData.toContentData()
  }

  fun putSection(apiSectionContentData: ApiSectionContentData, key: String) {

    val dbSectionContentData = apiSectionContentData.toDbSectionContentData(key)
    ocmDatabase.sectionDao().insertSectionContentData(dbSectionContentData)

    val sectionContentItem = apiSectionContentData.content
    if (sectionContentItem != null) {
      for (element in sectionContentItem.elements!!) {
        ocmDatabase.elementDao().insertElement(element.toDbElement())
        val dbSectionElementJoin = DbSectionElementJoin(sectionContentItem.slug, element.slug)
        ocmDatabase.sectionDao().insertSectionElement(dbSectionElementJoin)
      }
    }

    val elementsCache = apiSectionContentData.elementsCache
    if (elementsCache != null) {
      for ((key1, value) in elementsCache) {
        val apiElementData = ApiElementData(value)
        putDetail(apiElementData, key1)
      }
    }
  }
}
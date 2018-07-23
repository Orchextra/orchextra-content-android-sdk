package com.gigigo.orchextra.core.data

import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementData
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentData
import com.gigigo.orchextra.core.data.database.OcmDatabase
import com.gigigo.orchextra.core.data.database.entities.DbElementCache
import com.gigigo.orchextra.core.data.database.entities.DbMenuContentData
import com.gigigo.orchextra.core.data.database.entities.DbMenuElementJoin
import com.gigigo.orchextra.core.data.mappers.toDbElementCache
import com.gigigo.orchextra.core.data.mappers.toDbMenuContent
import com.gigigo.orchextra.core.data.mappers.toMenuContentData
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
    for (apiMenuContent in apiMenuContentData.menuContentList!!) {
      val dbMenuContent = apiMenuContent.toDbMenuContent()
      ocmDatabase.menuDao().insertMenu(dbMenuContent)

      for (dbElement in dbMenuContent.elements!!) {
        ocmDatabase.elementDao().insertElement(dbElement)

        for (scheduleDate in dbElement.dates) {
          scheduleDate.slug = dbElement.slug
          ocmDatabase.scheduleDatesDao().insertSchedule(scheduleDate)
        }

        val dbMenuElementJoin = DbMenuElementJoin(dbMenuContent.slug, dbElement.slug)
        ocmDatabase.elementDao().insertMenuElement(dbMenuElementJoin)
      }
    }

    val iterator = apiMenuContentData.elementsCache!!.entries.iterator()
    while (iterator.hasNext()) {
      val next = iterator.next()
      val apiElementData = ApiElementData(next.value)
      val key = next.key
      putDetail(apiElementData, key)
    }
  }

  private fun putDetail(apiElementData: ApiElementData, key: String) {
    val elementCacheData = apiElementData.element.toDbElementCache(key)
    ocmDatabase.elementCacheDao().insertElementCache(elementCacheData)
  }
}
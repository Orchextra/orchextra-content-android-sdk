package com.gigigo.orchextra.core.data.api.mappers.menus

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentData
import com.gigigo.orchextra.core.data.api.mappers.elementcache.ApiElementCacheMapper
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache
import com.gigigo.orchextra.core.domain.entities.menus.MenuContent
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData
import orchextra.javax.inject.Inject
import orchextra.javax.inject.Singleton
import java.util.ArrayList
import java.util.HashMap

@Singleton
class ApiMenuContentListResponseMapper @Inject
constructor(private val apiMenuContentMapper: ApiMenuContentMapper,
    private val apiElementCacheItemMapper: ApiElementCacheMapper) : ExternalClassToModelMapper<ApiMenuContentData, MenuContentData> {

  override fun externalClassToModel(data: ApiMenuContentData): MenuContentData {
    val model = MenuContentData()

    val menuContentList = ArrayList<MenuContent>()
    data.menuContentList?.let {
      for (apiMenuContent in data.menuContentList) {
        menuContentList.add(apiMenuContentMapper.externalClassToModel(apiMenuContent))
      }
    }
    model.menuContentList = menuContentList

    val elementCacheItemMap = HashMap<String, ElementCache>()
    data.elementsCache?.let {
      for (key in data.elementsCache.keys) {
        val apiElementCacheItem = data.elementsCache[key]

        apiElementCacheItem?.let {
          elementCacheItemMap[key] = apiElementCacheItemMapper.externalClassToModel(it)
        }
      }
    }
    model.elementsCache = elementCacheItemMap
    model.isFromCloud = data.isFromCloud

    return model
  }
}
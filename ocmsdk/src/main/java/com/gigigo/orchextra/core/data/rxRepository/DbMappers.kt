package com.gigigo.orchextra.core.data.rxRepository

import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCache
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContent
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentData
import com.gigigo.orchextra.core.data.api.dto.versioning.ApiVersionData
import com.gigigo.orchextra.core.data.database.entities.DbElementCache
import com.gigigo.orchextra.core.data.database.entities.DbMenuContent
import com.gigigo.orchextra.core.data.database.entities.DbMenuContentData
import com.gigigo.orchextra.core.data.database.entities.DbVersionData
import com.gigigo.orchextra.core.data.database.entities.DbVersionData.Companion.VERSION_KEY
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache
import com.gigigo.orchextra.core.domain.entities.menus.MenuContent
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData
import com.gigigo.orchextra.core.domain.entities.version.VersionData

//region VERSION
fun DbVersionData.toVersionData(): VersionData = with(this) {
  var versionData = VersionData()
  versionData.version = version
  return versionData
}

fun ApiVersionData.toDbVersionData(): DbVersionData = with(this) {
  val versionData = DbVersionData()
  versionData.id = VERSION_KEY
  versionData.version = version
  return versionData
}
//endregion

//region MENU
fun DbMenuContentData.toMenuContentData(): MenuContentData = with(this) {
  var menuContent = MenuContentData()
  menuContent.isFromCloud = isFromCloud

  var menus = ArrayList<MenuContent>()
  menuContentList?.let {
    for (menuContentItem in it) {
      menus.add(menuContentItem.toMenuContent())
    }
  }
  menuContent.menuContentList = menus

  var elements = HashMap<String, ElementCache>()
  elementsCache?.let {
    var elementsList = it.entries
    for ((key, value) in elementsList) {
      elements[key] = value.toElementCache()
    }
  }
  menuContent.elementsCache = elements

  return menuContent
}

fun ApiMenuContentData.toDbMenuContentData(): DbMenuContentData = with(this) {
  var menuContentData = DbMenuContentData()
  menuContentData.isFromCloud = false

  var menus = ArrayList<DbMenuContent>()
  menuContentList?.let {
    for (menuContentItem in it) {
      menus.add(menuContentItem.toDbMenuContent())
    }
  }
  menuContentData.menuContentList = menus

  var elements = HashMap<String, DbElementCache>()
  elementsCache?.let {
    var elementsList = it.entries
    for ((key, value) in elementsList) {
      elements[key] = value.toDbElementCache()
    }
  }
  menuContentData.elementsCache = elements

  return menuContentData
}

private fun DbMenuContent.toMenuContent(): MenuContent = with(this) {
  var menuContent = MenuContent()
  //TODO
  return menuContent
}

private fun ApiMenuContent.toDbMenuContent(): DbMenuContent {
  var menuContent = DbMenuContent()
  //TODO
  return menuContent
}
//endregion

//region ELEMENT
private fun DbElementCache.toElementCache(): ElementCache = with(this) {
  var elementCache = ElementCache()
  //TODO
  return elementCache
}

private fun ApiElementCache.toDbElementCache(): DbElementCache = with(this) {
  var elementCache = DbElementCache()
  //TODO
  return elementCache
}
//endregion
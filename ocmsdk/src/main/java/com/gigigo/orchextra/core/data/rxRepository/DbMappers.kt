package com.gigigo.orchextra.core.data.rxRepository

import com.gigigo.orchextra.core.data.api.dto.versioning.ApiVersionData
import com.gigigo.orchextra.core.data.api.dto.video.ApiVideoData
import com.gigigo.orchextra.core.data.database.entities.DbVersionData
import com.gigigo.orchextra.core.data.database.entities.DbVersionData.Companion.VERSION_KEY
import com.gigigo.orchextra.core.data.database.entities.DbVideoData
import com.gigigo.orchextra.core.data.database.entities.DbVimeoInfo
import com.gigigo.orchextra.core.domain.entities.version.VersionData
import gigigo.com.vimeolibs.VimeoInfo

//region VERSION
fun DbVersionData.toVersionData(): VersionData = with(this) {
  val versionData = VersionData()
  versionData.version = version
  return versionData
}

fun ApiVersionData.toDbVersionData(): DbVersionData = with(this) {
  val versionData = DbVersionData()
  versionData.id = VERSION_KEY
  versionData.version = version
  return versionData
}

fun ApiVersionData.toVersionData(): VersionData = with(this) {
  val versionData = VersionData()
  versionData.version = version
  return versionData
}
//endregion

/*
//region MENU
fun DbMenuContentData.toMenuContentData(): MenuContentData = with(this) {
  val menuContent = MenuContentData()
  menuContent.isFromCloud = isFromCloud

  val menus = ArrayList<MenuContent>()
  menuContentList?.let {
    for (menuContentItem in it) {
      menus.add(menuContentItem.toMenuContent())
    }
  }
  menuContent.menuContentList = menus

  val elements = HashMap<String, ElementCache>()
  elementsCache?.let {
    val elementsList = it.entries
    for ((key, value) in elementsList) {
      elements[key] = value.toElementCache()
    }
  }
  menuContent.elementsCache = elements

  return menuContent
}

fun ApiMenuContentData.toDbMenuContentData(): DbMenuContentData = with(this) {
  val menuContentData = DbMenuContentData()
  menuContentData.isFromCloud = false

  val menus = ArrayList<DbMenuContent>()
  menuContentList?.let {
    for (menuContentItem in it) {
      menus.add(menuContentItem.toDbMenuContent())
    }
  }
  menuContentData.menuContentList = menus

  val elements = HashMap<String, DbElementCache>()
  elementsCache?.let {
    val elementsList = it.entries
    for ((key, value) in elementsList) {
      elements[key] = value.toDbElementCache()
    }
  }
  menuContentData.elementsCache = elements

  return menuContentData
}

private fun DbMenuContent.toMenuContent(): MenuContent = with(this) {
  val menuContent = MenuContent()
  menuContent.slug = slug

  val elementsList = ArrayList<Element>()
  elements?.let {
    for (elementItem in it) {
      elementsList.add(elementItem.toElement())
    }
  }
  menuContent.elements = elementsList

  return menuContent
}

private fun ApiMenuContent.toDbMenuContent(): DbMenuContent {
  val menuContent = DbMenuContent()
  menuContent.slug = slug
  val elementsList = ArrayList<DbElement>()
  elements?.let {
    for (elementItem in it) {
      elementsList.add(elementItem.toDbElement())
    }
  }
  menuContent.elements = elementsList
  return menuContent
}
//endregion

//region ELEMENT
private fun DbElement.toElement(): Element = with(this) {
  val element = Element()
  element.slug = slug
  element.name = name
  element.customProperties = customProperties
  element.elementUrl = elementUrl
  element.sectionView = sectionView?.toElementSectionView()
  element.tags = tags
  //element.dates = dates
  return element
}

private fun ApiElement.toDbElement(): DbElement = with(this) {
  val element = DbElement()
  element.slug = slug
  element.name = name
  element.customProperties = customProperties
  element.elementUrl = elementUrl
  element.sectionView = sectionView?.toDbElementSectionView()
  element.tags = tags
  //element.dates = dates
  return element
}

private fun DbElementSectionView.toElementSectionView(): ElementSectionView = with(this) {
  val element = ElementSectionView()
  element.text = text
  element.imageUrl = imageUrl
  element.imageThumb = imageThumb
  return element
}

private fun ApiElementSectionView.toDbElementSectionView(): DbElementSectionView = with(this) {
  val element = DbElementSectionView()
  element.text = text
  element.imageUrl = imageUrl
  element.imageThumb = imageThumb
  return element
}


private fun DbElementCache.toElementCache(): ElementCache = with(this) {
  val elementCache = ElementCache()
  elementCache.slug = slug
  elementCache.name = name
  elementCache.customProperties = customProperties
  elementCache.preview = preview?.toElementCachePreview()
  //elementCache.render = render?.toElementCacheRender()
  elementCache.share = share?.toElementCacheShare()
  elementCache.tags = tags
  elementCache.type = ElementCacheType.convertStringToEnum(type)
  elementCache.updateAt = updatedAt
  return elementCache
}

private fun ApiElementCache.toDbElementCache(): DbElementCache = with(this) {
  val elementCache = DbElementCache()
  elementCache.slug = slug
  elementCache.name = name
  elementCache.customProperties = customProperties
  elementCache.preview = preview?.toDbElementCachePreview()
  //elementCache.render = render?.toDbElementCacheRender()
  elementCache.share = share?.toDbElementCacheShare()
  elementCache.tags = tags
  elementCache.type = type
  elementCache.updatedAt = updatedAt
  return elementCache
}

private fun ApiElementCachePreview.toDbElementCachePreview(): DbElementCachePreview = with(this) {
  val elementCache = DbElementCachePreview()
  elementCache.text = text
  elementCache.imageUrl = imageUrl
  elementCache.imageThumb = imageThumb
  elementCache.behaviour = behaviour
  return elementCache
}

private fun DbElementCachePreview.toElementCachePreview(): ElementCachePreview = with(this) {
  val elementCache = ElementCachePreview()
  elementCache.text = text
  elementCache.imageUrl = imageUrl
  elementCache.imageThumb = imageThumb
  elementCache.behaviour = ElementCacheBehaviour.convertStringToEnum(behaviour)
  return elementCache
}

private fun ApiElementCacheShare.toDbElementCacheShare(): DbElementCacheShare = with(this) {
  val elementCache = DbElementCacheShare()
  elementCache.text = text
  elementCache.url = url
  return elementCache
}

private fun DbElementCacheShare.toElementCacheShare(): ElementCacheShare = with(this) {
  val elementCache = ElementCacheShare()
  elementCache.text = text
  elementCache.url = url
  return elementCache
}

//TODO: mappers to ArticleElement<RenderType>

private fun FederatedAuthorizationData.toDbFederatedAuthorizationData(): DbFederatedAuthorizationData = with(
    this) {
  val federatedAuthorization = DbFederatedAuthorizationData()
  federatedAuthorization.type = type
  federatedAuthorization.active = active
  federatedAuthorization.keys = keys?.toDbCidKeyData()
  return federatedAuthorization
}

private fun DbFederatedAuthorizationData.toFederatedAuthorizationData(): FederatedAuthorization = with(
    this) {
  val federatedAuthorization = FederatedAuthorization()
  federatedAuthorization.type = type
  federatedAuthorization.isActive = active
  federatedAuthorization.keys = keys?.toCidKey()
  return federatedAuthorization
}

private fun CidKeyData.toDbCidKeyData(): DbCidKeyData? {
  val cidKeyData = DbCidKeyData()
  cidKeyData.siteName = siteName
  return cidKeyData
}

private fun DbCidKeyData.toCidKey(): CidKey {
  val cidKeyData = CidKey()
  cidKeyData.siteName = siteName
  return cidKeyData
}
*/
fun ApiVideoData.toVimeoInfo(): VimeoInfo = with(this) {
  val video = VimeoInfo()
  video.id = element.id
  video.thumbPath = element.thumbPath
  video.videoPath = element.videoPath
  return video
}

fun DbVideoData.toVimeoInfo(): VimeoInfo = with(this) {
  val video = VimeoInfo()
  video.id = id
  video.thumbPath = element?.thumbPath
  video.videoPath = element?.videoPath
  return video
}

fun ApiVideoData.toDbVideoData(): DbVideoData {
  val video = DbVideoData()
  video.id = element.id
  video.element = element.toDbVimeoInfo()
  video.expireAt =
      return video
}

private fun VimeoInfo.toDbVimeoInfo(): DbVimeoInfo {
  val vimeo = DbVimeoInfo()
  vimeo.videoPath = videoPath
  vimeo.thumbPath = thumbPath
  return vimeo
}


//endregion

//region SECTION
//TODO: mappers for ApiSectionContentData, ApiContentItem, ApiContentItemLayout, ApiContentItemPattern
//endregion

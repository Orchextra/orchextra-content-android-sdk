package com.gigigo.orchextra.core.data.mappers

import com.gigigo.orchextra.core.data.api.dto.article.ApiArticleElement
import com.gigigo.orchextra.core.data.api.dto.article.ApiArticleElementRender
import com.gigigo.orchextra.core.data.api.dto.content.ApiContentItem
import com.gigigo.orchextra.core.data.api.dto.content.ApiContentItemLayout
import com.gigigo.orchextra.core.data.api.dto.content.ApiContentItemPattern
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentData
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCache
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCachePreview
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCacheRender
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCacheShare
import com.gigigo.orchextra.core.data.api.dto.elementcache.CidKeyData
import com.gigigo.orchextra.core.data.api.dto.elementcache.FederatedAuthorizationData
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElement
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementData
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementSectionView
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContent
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentData
import com.gigigo.orchextra.core.data.database.entities.DbArticleElement
import com.gigigo.orchextra.core.data.database.entities.DbArticleElementRender
import com.gigigo.orchextra.core.data.database.entities.DbCidKeyData
import com.gigigo.orchextra.core.data.database.entities.DbContentItem
import com.gigigo.orchextra.core.data.database.entities.DbContentItemLayout
import com.gigigo.orchextra.core.data.database.entities.DbContentItemPattern
import com.gigigo.orchextra.core.data.database.entities.DbElement
import com.gigigo.orchextra.core.data.database.entities.DbElementCache
import com.gigigo.orchextra.core.data.database.entities.DbElementCachePreview
import com.gigigo.orchextra.core.data.database.entities.DbElementCacheRender
import com.gigigo.orchextra.core.data.database.entities.DbElementCacheShare
import com.gigigo.orchextra.core.data.database.entities.DbElementData
import com.gigigo.orchextra.core.data.database.entities.DbElementSectionView
import com.gigigo.orchextra.core.data.database.entities.DbFederatedAuthorizationData
import com.gigigo.orchextra.core.data.database.entities.DbMenuContent
import com.gigigo.orchextra.core.data.database.entities.DbMenuContentData
import com.gigigo.orchextra.core.data.database.entities.DbScheduleDates
import com.gigigo.orchextra.core.data.database.entities.DbSectionContentData
import com.gigigo.orchextra.core.data.database.entities.DbVideoData
import com.gigigo.orchextra.core.data.database.entities.DbVimeoInfo
import com.gigigo.orchextra.core.domain.entities.article.ArticleButtonElement
import com.gigigo.orchextra.core.domain.entities.article.ArticleButtonElementRender
import com.gigigo.orchextra.core.domain.entities.article.ArticleHeaderElement
import com.gigigo.orchextra.core.domain.entities.article.ArticleHeaderElementRender
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageAndTextElement
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageAndTextElementRender
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElement
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElementRender
import com.gigigo.orchextra.core.domain.entities.article.ArticleRichTextElement
import com.gigigo.orchextra.core.domain.entities.article.ArticleRichTextElementRender
import com.gigigo.orchextra.core.domain.entities.article.ArticleTextAndImageElement
import com.gigigo.orchextra.core.domain.entities.article.ArticleTextAndImageElementRender
import com.gigigo.orchextra.core.domain.entities.article.ArticleVimeoVideoElement
import com.gigigo.orchextra.core.domain.entities.article.ArticleVimeoVideoElementRender
import com.gigigo.orchextra.core.domain.entities.article.ArticleYoutubeVideoElement
import com.gigigo.orchextra.core.domain.entities.article.ArticleYoutubeVideoElementRender
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleButtonSize
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleButtonType
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElementRender
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleTypeSection
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItem
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItemLayout
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItemPattern
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItemTypeLayout
import com.gigigo.orchextra.core.domain.entities.elementcache.CidKey
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheBehaviour
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCachePreview
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheRender
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheShare
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheType
import com.gigigo.orchextra.core.domain.entities.elementcache.FederatedAuthorization
import com.gigigo.orchextra.core.domain.entities.elementcache.VideoFormat
import com.gigigo.orchextra.core.domain.entities.elements.Element
import com.gigigo.orchextra.core.domain.entities.elements.ElementData
import com.gigigo.orchextra.core.domain.entities.elements.ElementSectionView
import com.gigigo.orchextra.core.domain.entities.menus.MenuContent
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData
import gigigo.com.vimeolibs.VimeoInfo
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale

fun ApiMenuContentData.toMenuContentData(): MenuContentData = with(this) {
  val menuContent = MenuContentData()
  menuContent.isFromCloud = true

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

fun DbMenuContentData.toMenuContentData(): MenuContentData = with(this) {
  val menuContent = MenuContentData()
  menuContent.isFromCloud = false

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

fun ApiMenuContent.toDbMenuContent(): DbMenuContent {
  val menuContent = DbMenuContent()
  menuContent.slug = slug
  val elementsList = ArrayList<DbElement>()
  elements?.let {
    for (elementItem in it) {
      elementsList.add(elementItem.toDbElement(-1))
    }
  }
  menuContent.elements = elementsList
  return menuContent
}

fun MenuContent.toDbMenuContent(): DbMenuContent {
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

fun ApiMenuContent.toMenuContent(): MenuContent {
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
//endregion

//region ELEMENT
fun ApiElementData.toElementData(): ElementData {
  val elementData = ElementData()
  elementData.element = element.toElementCache()
  return elementData
}

fun ApiElement.toDbElement(index: Int): DbElement = with(this) {
  val element = DbElement()
  element.slug = slug
  element.name = name
  element.customProperties = customProperties?.toDbCustomProperties()
  element.elementUrl = elementUrl
  element.contentVersion = contentVersion
  element.sectionView = sectionView?.toDbElementSectionView()
  element.tags = tags
  element.dates = dates?.toDbScheduleDates(slug) ?: emptyList()
  element.listIndex = index
  return element
}

fun Element.toDbElement(): DbElement = with(this) {
  val element = DbElement()
  element.listIndex = index
  element.slug = slug
  element.name = name
  element.customProperties = customProperties?.toDbCustomProperties()
  element.elementUrl = elementUrl
  element.contentVersion = contentVersion
  element.sectionView = sectionView?.toDbElementSectionView()
  element.tags = tags
  element.dates = dates.toDbScheduleDates(slug)
  return element
}

fun ApiElement.toElement(): Element = with(this) {
  val element = Element()
  element.slug = slug
  element.name = name
  element.customProperties = customProperties
  element.elementUrl = elementUrl
  element.contentVersion = contentVersion
  element.sectionView = sectionView?.toElementSectionView()
  element.tags = tags
  element.dates = dates ?: emptyList()
  return element
}

/*
fun Element.toDbElement(): DbElement = with(this) {
  val element = DbElement()
  element.slug = slug
  element.name = name
  element.customProperties = customProperties?.toDbCustomProperties()
  element.elementUrl = elementUrl
  element.sectionView = sectionView?.toDbElementSectionView()
  element.tags = tags
  element.dates = dates.toDbScheduleDates(slug)
  return element
}
*/

private fun DbElement.toElement(): Element = with(this) {
  val element = Element()
  element.index = listIndex
  element.slug = slug
  element.name = name
  element.customProperties = customProperties
  element.elementUrl = elementUrl
  element.sectionView = sectionView?.toElementSectionView()
  element.tags = tags
  element.dates = dates.toScheduleDates()
  element.contentVersion = contentVersion

  return element
}

private fun ApiElementSectionView.toDbElementSectionView(): DbElementSectionView = with(this) {
  val element = DbElementSectionView()
  element.text = text
  element.imageUrl = imageUrl
  element.imageThumb = imageThumb
  return element
}

private fun ElementSectionView.toDbElementSectionView(): DbElementSectionView = with(this) {
  val element = DbElementSectionView()
  element.text = text
  element.imageUrl = imageUrl
  element.imageThumb = imageThumb
  return element
}

fun ApiElementSectionView.toElementSectionView(): ElementSectionView = with(this) {
  val element = ElementSectionView()
  element.text = text
  element.imageUrl = imageUrl
  element.imageThumb = imageThumb
  return element
}

/*
private fun ElementSectionView.toDbElementSectionView(): DbElementSectionView = with(this) {
  val element = DbElementSectionView()
  element.text = text
  element.imageUrl = imageUrl
  element.imageThumb = imageThumb
  return element
}
*/

private fun DbElementSectionView.toElementSectionView(): ElementSectionView = with(this) {
  val element = ElementSectionView()
  element.text = text
  element.imageUrl = imageUrl
  element.imageThumb = imageThumb
  return element
}


fun List<List<String>>.toDbScheduleDates(slug: String): List<DbScheduleDates> = with(this) {
  var scheduleDates: List<DbScheduleDates> = emptyList()
  scheduleDates = map {
    it.size
    try {
      var start = it[0]
      var end = it[1]

      val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
      var dateStart = formatter.parse(start)
      var dateEnd = formatter.parse(end)

      DbScheduleDates(slug, dateStart?.time, dateEnd?.time)
    } catch (e: Exception) {

    }
  } as List<DbScheduleDates>

  return scheduleDates
}

private fun List<DbScheduleDates>.toScheduleDates(): List<List<String>> = with(this) {
  var scheduleDates: List<List<String>> = emptyList()
  map { listOf(it.dateStart, it.dateEnd) }
  return scheduleDates
}


fun ApiElementCache.toElementCache(): ElementCache = with(this) {
  val elementCache = ElementCache()
  elementCache.slug = slug
  elementCache.name = name
  elementCache.customProperties = customProperties
  elementCache.preview = preview?.toElementCachePreview()
  elementCache.render = render?.toElementCacheRender()
  elementCache.share = share?.toElementCacheShare()
  elementCache.tags = tags
  elementCache.type = ElementCacheType.convertStringToEnum(type)
  elementCache.updateAt = updatedAt
  return elementCache
}

fun ApiElementCache.toDbElementCache(key: String, index: Int): DbElementCache = with(this) {
  val elementCache = DbElementCache()
  elementCache.key = key
  elementCache.slug = slug
  elementCache.name = name
  elementCache.customProperties = customProperties?.toDbCustomProperties()
  elementCache.preview = preview?.toDbElementCachePreview()
  elementCache.render = render?.toDbElementCacheRender()
  elementCache.share = share?.toDbElementCacheShare()
  elementCache.tags = tags
  elementCache.type = type
  elementCache.updatedAt = updatedAt
  elementCache.listIndex = index
  return elementCache
}

fun DbElementData.toElementData(): ElementData = with(this) {
  val elementData = ElementData()
  elementData.element = element?.toElementCache()
  return elementData
}

fun DbElementCache.toElementCache(): ElementCache = with(this) {
  val elementCache = ElementCache()
  elementCache.slug = slug
  elementCache.name = name
  elementCache.customProperties = customProperties
  elementCache.preview = preview?.toElementCachePreview()
  elementCache.render = render?.toElementCacheRender()
  elementCache.share = share?.toElementCacheShare()
  elementCache.tags = tags
  elementCache.type = ElementCacheType.convertStringToEnum(type)
  elementCache.updateAt = updatedAt
  elementCache.index = listIndex
  return elementCache
}

fun ApiElementCachePreview.toElementCachePreview(): ElementCachePreview = with(this) {
  val elementCache = ElementCachePreview()
  elementCache.text = text
  elementCache.imageUrl = imageUrl
  elementCache.imageThumb = imageThumb
  elementCache.behaviour = ElementCacheBehaviour.convertStringToEnum(behaviour)
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

/*
private fun ElementCachePreview.toDbElementCachePreview(): DbElementCachePreview = with(this) {
  val elementCache = DbElementCachePreview()
  elementCache.text = text
  elementCache.imageUrl = imageUrl
  elementCache.imageThumb = imageThumb
  elementCache.behaviour = behaviour?.cacheBehaviour
  return elementCache
}
*/

private fun DbElementCachePreview.toElementCachePreview(): ElementCachePreview = with(this) {
  val elementCache = ElementCachePreview()
  elementCache.text = text
  elementCache.imageUrl = imageUrl
  elementCache.imageThumb = imageThumb
  elementCache.behaviour = ElementCacheBehaviour.convertStringToEnum(behaviour)
  return elementCache
}

fun ApiElementCacheRender.toElementCacheRender(): ElementCacheRender = with(this) {
  val elementCacheRender = ElementCacheRender()
  elementCacheRender.contentUrl = contentUrl
  elementCacheRender.url = url
  elementCacheRender.title = title

  val elementsCache = ArrayList<ArticleElement<ArticleElementRender>>()
  elements?.forEach {
    elementsCache.add(it.toArticleElement())
  }
  elementCacheRender.elements = elementsCache

  elementCacheRender.schemeUri = schemeUri
  elementCacheRender.source = source
  elementCacheRender.format = VideoFormat.convertStringToType(format)
  elementCacheRender.federatedAuth = federatedAuth?.toFederatedAuthorization()
  return elementCacheRender
}

private fun ApiElementCacheRender.toDbElementCacheRender(): DbElementCacheRender = with(this) {
  val elementCacheRender = DbElementCacheRender()
  elementCacheRender.contentUrl = contentUrl
  elementCacheRender.url = url
  elementCacheRender.title = title

  val elementsCache = ArrayList<DbArticleElement>()
  elements?.forEach {
    elementsCache.add(it.toDbArticleElement())
  }
  elementCacheRender.elements = elementsCache

  elementCacheRender.schemeUri = schemeUri
  elementCacheRender.source = source
  elementCacheRender.format = format
  elementCacheRender.federatedAuth = federatedAuth?.toDbFederatedAuthorizationData()
  return elementCacheRender
}

/*
private fun ElementCacheRender.toDbElementCacheRender(): DbElementCacheRender = with(this) {
  var elementCacheRender = DbElementCacheRender()
  elementCacheRender.contentUrl = contentUrl
  elementCacheRender.url = url
  elementCacheRender.title = title

  val elementsCache = ArrayList<DbArticleElement>()
  elements?.forEach {
    elementsCache.add(it.toDbArticleElement())
  }
  elementCacheRender.elements = elementsCache

  elementCacheRender.schemeUri = schemeUri
  elementCacheRender.source = source
  elementCacheRender.format = format?.videoFormat
  elementCacheRender.federatedAuth = federatedAuth?.toDbFederatedAuthorizationData()
  return elementCacheRender
}
*/

private fun DbElementCacheRender.toElementCacheRender(): ElementCacheRender = with(this) {
  var elementCacheRender = ElementCacheRender()
  elementCacheRender.contentUrl = contentUrl
  elementCacheRender.url = url
  elementCacheRender.title = title

  val elementsCache = ArrayList<ArticleElement<ArticleElementRender>>()
  elements?.forEach {
    elementsCache.add(it.toArticleElement())
  }
  elementCacheRender.elements = elementsCache

  elementCacheRender.schemeUri = schemeUri
  elementCacheRender.source = source
  elementCacheRender.format = VideoFormat.convertStringToType(format)
  elementCacheRender.federatedAuth = federatedAuth?.toFederatedAuthorization()
  return elementCacheRender
}

fun ApiElementCacheShare.toElementCacheShare(): ElementCacheShare = with(this) {
  val elementCache = ElementCacheShare()
  elementCache.text = text
  elementCache.url = url
  return elementCache
}

private fun ApiElementCacheShare.toDbElementCacheShare(): DbElementCacheShare = with(this) {
  val elementCache = DbElementCacheShare()
  elementCache.text = text
  elementCache.url = url
  return elementCache
}

/*
private fun ElementCacheShare.toDbElementCacheShare(): DbElementCacheShare = with(this) {
  val elementCache = DbElementCacheShare()
  elementCache.text = text
  elementCache.url = url
  return elementCache
}
*/

private fun DbElementCacheShare.toElementCacheShare(): ElementCacheShare = with(this) {
  val elementCache = ElementCacheShare()
  elementCache.text = text
  elementCache.url = url
  return elementCache
}

private fun Map<String, Any>.toDbCustomProperties(): Map<String, String> = with(this) {
  val map = HashMap<String, String>()
  for (next in iterator()) {
    val (key, value) = next
    map[key] = value.toString()
  }
  return map
}

fun <T> ApiArticleElement.toArticleElement(): ArticleElement<T> = with(this) {
  val articleElement = ArticleElement<T>()
  articleElement.customProperties = customProperties

  val articleType = ArticleTypeSection.convertStringToEnum(type)
  val articleElementRender = when (articleType) {
    ArticleTypeSection.HEADER -> {
      var elementRender = ArticleHeaderElementRender()
      with(elementRender) {
        html = render?.html
        imageUrl = render?.imageUrl
        imageThumb = render?.imageThumb
      }
      elementRender
    }
    ArticleTypeSection.IMAGE -> {
      val elementRender = ArticleImageElementRender()
      with(elementRender) {
        imageUrl = render?.imageUrl
        imageThumb = render?.imageThumb
        elementUrl = render?.elementUrl
      }
      elementRender
    }
    ArticleTypeSection.VIDEO -> {
      val videoFormat = VideoFormat.convertStringToType(render?.format)
      val elementRender = when (videoFormat) {
        VideoFormat.YOUTUBE -> {
          val videoElementRender = ArticleYoutubeVideoElementRender()
          videoElementRender.source = render?.source
          videoElementRender as T
        }
        VideoFormat.VIMEO -> {
          val videoElementRender = ArticleVimeoVideoElementRender()
          videoElementRender.source = render?.source
          videoElementRender as T
        }
        else -> null
      }
      elementRender
    }
    ArticleTypeSection.RICH_TEXT -> {
      val elementRender = ArticleRichTextElementRender()
      elementRender.html = render?.text
      elementRender
    }
    ArticleTypeSection.IMAGE_AND_TEXT -> {
      val elementRender = ArticleImageAndTextElementRender()
      with(elementRender) {
        text = render?.text
        imageUrl = render?.imageUrl
        ratios = render?.ratios
      }
      elementRender
    }
    ArticleTypeSection.TEXT_AND_IMAGE -> {
      val elementRender = ArticleTextAndImageElementRender()
      with(elementRender) {
        text = render?.text
        imageUrl = render?.imageUrl
        ratios = render?.ratios
      }
      elementRender
    }
    ArticleTypeSection.BUTTON -> {
      val elementRender = ArticleButtonElementRender()
      with(elementRender) {
        type = ArticleButtonType.convertFromString(render?.type)
        size = ArticleButtonSize.convertFromString(render?.size)
        elementUrl = render?.elementUrl
        text = render?.text
        textColor = render?.textColor
        bgColor = render?.bgColor
        imageUrl = render?.imageUrl
      }
      elementRender
    }
    else -> {
      ArticleButtonElementRender()
    }
  }
  articleElement.render = articleElementRender as T
  return articleElement
}

private fun ApiArticleElement.toDbArticleElement(): DbArticleElement = with(this) {
  val articleElement = DbArticleElement()
  articleElement.type = type
  articleElement.customProperties = customProperties?.toDbCustomProperties()
  articleElement.render = render?.toDbArticleElementRender()
  return articleElement
}

/*
private fun <T> ArticleElement<T>.toDbArticleElement(): DbArticleElement = with(this) {
  val articleElement = DbArticleElement()
  articleElement.customProperties = customProperties?.toDbCustomProperties()

  val articleElementRender = DbArticleElementRender()
  val articleRender = render
  when (articleRender) {
    is ArticleHeaderElementRender -> {
      articleElement.type = ArticleTypeSection.HEADER.typeSection

      articleElementRender.html = articleRender.html
      articleElementRender.imageUrl = articleRender.imageUrl
      articleElementRender.imageThumb = articleRender.imageThumb
    }
    is ArticleImageElementRender -> {
      articleElement.type = ArticleTypeSection.IMAGE.typeSection

      articleElementRender.elementUrl = articleRender.elementUrl
      articleElementRender.imageUrl = articleRender.imageUrl
      articleElementRender.imageThumb = articleRender.imageThumb
    }
    is ArticleVimeoVideoElementRender -> {
      articleElement.type = ArticleTypeSection.VIDEO.typeSection
      articleElementRender.format = VideoFormat.VIMEO.videoFormat

      articleElementRender.source = articleRender.source
    }
    is ArticleYoutubeVideoElementRender -> {
      articleElement.type = ArticleTypeSection.VIDEO.typeSection
      articleElementRender.format = VideoFormat.YOUTUBE.videoFormat

      articleElementRender.source = articleRender.source
    }
    is ArticleRichTextElementRender -> {
      articleElement.type = ArticleTypeSection.RICH_TEXT.typeSection

      articleElementRender.text = articleRender.html
    }
    is ArticleImageAndTextElementRender -> {
      articleElement.type = ArticleTypeSection.IMAGE_AND_TEXT.typeSection

      articleElementRender.text = articleRender.text
      articleElementRender.imageUrl = articleRender.imageUrl
      articleElementRender.ratios = articleRender.ratios
    }
    is ArticleTextAndImageElementRender -> {
      articleElement.type = ArticleTypeSection.TEXT_AND_IMAGE.typeSection

      articleElementRender.text = articleRender.text
      articleElementRender.imageUrl = articleRender.imageUrl
      articleElementRender.ratios = articleRender.ratios
    }
    is ArticleButtonElementRender -> {
      articleElement.type = ArticleTypeSection.BUTTON.typeSection

      articleElementRender.type = articleRender.type?.typeButton
      articleElementRender.size = articleRender.size?.size
      articleElementRender.elementUrl = articleRender.elementUrl
      articleElementRender.text = articleRender.text
      articleElementRender.textColor = articleRender.textColor
      articleElementRender.bgColor = articleRender.bgColor
      articleElementRender.imageUrl = articleRender.imageUrl
    }
    else -> {
    }
  }
  articleElement.render = articleElementRender

  return articleElement
}
*/

private fun <T> DbArticleElement.toArticleElement(): ArticleElement<T> = with(this) {
  lateinit var articleElement: ArticleElement<T>

  val articleType = ArticleTypeSection.convertStringToEnum(type)
  val articleElementRender = when (articleType) {
    ArticleTypeSection.HEADER -> {
      articleElement = ArticleHeaderElement() as ArticleElement<T>
      val headerRender = ArticleHeaderElementRender()
      headerRender.html = render?.html
      headerRender.imageUrl = render?.imageUrl
      headerRender.imageThumb = render?.imageThumb
      headerRender as T
    }
    ArticleTypeSection.IMAGE -> {
      articleElement = ArticleImageElement() as ArticleElement<T>
      val imageRender = ArticleImageElementRender()
      imageRender.elementUrl = render?.elementUrl
      imageRender.imageUrl = render?.imageUrl
      imageRender.imageThumb = render?.imageThumb
      imageRender as T
    }
    ArticleTypeSection.VIDEO -> {
      val videoFormat = VideoFormat.convertStringToType(render?.format)
      val videoRender = when (videoFormat) {
        VideoFormat.YOUTUBE -> {
          articleElement = ArticleYoutubeVideoElement() as ArticleElement<T>
          val videoElementRender = ArticleYoutubeVideoElementRender()
          videoElementRender.source = render?.source
          videoElementRender as T
        }
        VideoFormat.VIMEO -> {
          articleElement = ArticleVimeoVideoElement() as ArticleElement<T>
          val videoElementRender = ArticleVimeoVideoElementRender()
          videoElementRender.source = render?.source
          videoElementRender as T
        }
        else -> null
      }
      videoRender as T
    }
    ArticleTypeSection.RICH_TEXT -> {
      articleElement = ArticleRichTextElement() as ArticleElement<T>
      val richTextRender = ArticleRichTextElementRender()
      richTextRender.html = render?.text
      richTextRender as T
    }
    ArticleTypeSection.IMAGE_AND_TEXT -> {
      articleElement = ArticleImageAndTextElement() as ArticleElement<T>
      val imageTextRender = ArticleImageAndTextElementRender()
      imageTextRender.text = render?.text
      imageTextRender.imageUrl = render?.imageUrl
      imageTextRender.ratios = render?.ratios
      imageTextRender as T
    }
    ArticleTypeSection.TEXT_AND_IMAGE -> {
      articleElement = ArticleTextAndImageElement() as ArticleElement<T>
      val textImageRender = ArticleTextAndImageElementRender()
      textImageRender.text = render?.text
      textImageRender.imageUrl = render?.imageUrl
      textImageRender.ratios = render?.ratios
      textImageRender as T
    }
    ArticleTypeSection.BUTTON -> {
      articleElement = ArticleButtonElement() as ArticleElement<T>
      val buttonRender = ArticleButtonElementRender()
      buttonRender.type = ArticleButtonType.convertFromString(render?.type)
      buttonRender.size = ArticleButtonSize.convertFromString(render?.size)
      buttonRender.elementUrl = render?.elementUrl
      buttonRender.text = render?.text
      buttonRender.textColor = render?.textColor
      buttonRender.bgColor = render?.bgColor
      buttonRender.imageUrl = render?.imageUrl
      buttonRender as T
    }
    ArticleTypeSection.NONE -> {
      articleElement = ArticleElement()
      null
    }
  }
  articleElement.render = articleElementRender

  articleElement.customProperties = customProperties
  return articleElement
}

/*
private fun ApiArticleElementRender.toArticleElementRender(): ArticleElementRender = with(this) {
  val articleType = ArticleTypeSection.convertStringToEnum(type)
  val articleElementRender = when(articleType) {
    ArticleTypeSection.HEADER -> {
      val headerRender = ArticleHeaderElementRender()
      headerRender.html = html
      headerRender.imageUrl = imageUrl
      headerRender.imageThumb = imageThumb
      headerRender
    }
    ArticleTypeSection.IMAGE -> {
      val imageRender = ArticleImageElementRender()
      imageRender.elementUrl = elementUrl
      imageRender.imageUrl = imageUrl
      imageRender.imageThumb = imageThumb
      imageRender
    }
    ArticleTypeSection.VIDEO -> {
      val videoFormat = VideoFormat.convertStringToType(format)
      val elementRender = when(videoFormat) {
        VideoFormat.YOUTUBE -> {
          val videoElement = ArticleYoutubeVideoElementRender()
          videoElement.source = source
        }
        VideoFormat.VIMEO -> {
          val videoElement = ArticleVimeoVideoElementRender()
          videoElement.source = source
        }
        else -> null
      }
      elementRender
    }
    ArticleTypeSection.RICH_TEXT -> {
      val richTextRender = ArticleRichTextElementRender()
      richTextRender.html = html
      richTextRender
    }
    ArticleTypeSection.IMAGE_AND_TEXT -> {
      val imageTextRender = ArticleImageAndTextElementRender()
      imageTextRender.text = text
      imageTextRender.imageUrl = imageUrl
      imageTextRender.ratios = ratios
      imageTextRender
    }
    ArticleTypeSection.TEXT_AND_IMAGE -> {
      val textImageRender = ArticleTextAndImageElementRender()
      textImageRender.text = text
      textImageRender.imageUrl = imageUrl
      textImageRender.ratios = ratios
      textImageRender
    }
    ArticleTypeSection.BUTTON -> {
      val buttonRender = ArticleButtonElementRender()
      buttonRender.type = ArticleButtonType.convertFromString(type)
      buttonRender.size = ArticleButtonSize.convertFromString(size)
      buttonRender.elementUrl = elementUrl
      buttonRender.text = text
      buttonRender.textColor = textColor
      buttonRender.bgColor = bgColor
      buttonRender.imageUrl = imageUrl
      buttonRender
    }
    ArticleTypeSection.NONE -> { null }
  }

  return articleElementRender as ArticleElementRender
}
*/

private fun ApiArticleElementRender.toDbArticleElementRender(): DbArticleElementRender = with(
    this) {
  val articleElementRender = DbArticleElementRender()
  articleElementRender.text = text
  articleElementRender.imageUrl = imageUrl
  articleElementRender.elementUrl = elementUrl
  articleElementRender.html = html
  articleElementRender.format = format
  articleElementRender.source = source
  articleElementRender.imageThumb = imageThumb
  articleElementRender.ratios = ratios
  articleElementRender.type = type
  articleElementRender.size = size
  articleElementRender.textColor = textColor
  articleElementRender.bgColor = bgColor
  return articleElementRender
}

/*
private fun ArticleElementRender.toDbArticleElementRender(): DbArticleElementRender = with(this) {
  val articleElementRender = DbArticleElementRender()
  when(this) {
    is ArticleHeaderElementRender -> {}
    is ArticleImageElementRender -> {}
    is ArticleVimeoVideoElementRender, is ArticleYoutubeVideoElementRender -> {}
    is ArticleRichTextElementRender -> {}
    is ArticleImageAndTextElementRender -> {}
    is ArticleTextAndImageElementRender -> {}
    is ArticleButtonElementRender -> {}
    else -> {}
  }
  return articleElementRender
}
*/
/*
private fun DbArticleElementRender.toArticleElementRender(): ArticleElementRender = with(this) {
  val articleElementRender : ArticleElementRender

  val articleType = ArticleTypeSection.convertStringToEnum(type)
  when(articleType) {
    ArticleTypeSection.HEADER -> { articleElementRender = ArticleHeaderElementRender() }
    ArticleTypeSection.IMAGE -> { articleElementRender = ArticleHeaderElementRender() }
    ArticleTypeSection.VIDEO -> { articleElementRender = ArticleHeaderElementRender() }
    ArticleTypeSection.RICH_TEXT -> { articleElementRender = ArticleHeaderElementRender() }
    ArticleTypeSection.IMAGE_AND_TEXT -> { articleElementRender = ArticleHeaderElementRender() }
    ArticleTypeSection.TEXT_AND_IMAGE -> { articleElementRender = ArticleHeaderElementRender() }
    ArticleTypeSection.BUTTON -> { articleElementRender = ArticleHeaderElementRender() }
    ArticleTypeSection.NONE -> { articleElementRender = ArticleHeaderElementRender() }
  }
  //TODO
  return articleElementRender
}
*/

fun FederatedAuthorizationData.toFederatedAuthorization(): FederatedAuthorization = with(this) {
  val federatedAuthorization = FederatedAuthorization()
  federatedAuthorization.type = type
  federatedAuthorization.isActive = active
  federatedAuthorization.keys = keys?.toCidKey()
  return federatedAuthorization
}

fun FederatedAuthorizationData.toDbFederatedAuthorizationData(): DbFederatedAuthorizationData = with(
    this) {
  val federatedAuthorization = DbFederatedAuthorizationData()
  federatedAuthorization.type = type
  federatedAuthorization.active = active
  federatedAuthorization.keys = keys?.toDbCidKeyData()
  return federatedAuthorization
}

/*
private fun FederatedAuthorization.toDbFederatedAuthorizationData(): DbFederatedAuthorizationData = with(this) {
  val federatedAuthorization = DbFederatedAuthorizationData()
  federatedAuthorization.type = type
  federatedAuthorization.active = isActive
  federatedAuthorization.keys = keys.toDbCidKeyData()
  return federatedAuthorization
}
*/

private fun DbFederatedAuthorizationData.toFederatedAuthorization(): FederatedAuthorization = with(
    this) {
  val federatedAuthorization = FederatedAuthorization()
  federatedAuthorization.type = type
  federatedAuthorization.isActive = active
  federatedAuthorization.keys = keys?.toCidKey()
  return federatedAuthorization
}

private fun CidKeyData.toCidKey(): CidKey? {
  val cidKeyData = CidKey()
  cidKeyData.siteName = siteName
  return cidKeyData
}

private fun CidKeyData.toDbCidKeyData(): DbCidKeyData? {
  val cidKeyData = DbCidKeyData()
  cidKeyData.siteName = siteName
  return cidKeyData
}

/*
private fun CidKey.toDbCidKeyData(): DbCidKeyData? {
  val cidKeyData = DbCidKeyData()
  cidKeyData.siteName = siteName
  return cidKeyData
}
*/

private fun DbCidKeyData.toCidKey(): CidKey {
  val cidKeyData = CidKey()
  cidKeyData.siteName = siteName
  return cidKeyData
}
//endregion

//region SECTION
fun ApiSectionContentData.toDbSectionContentData(key: String): DbSectionContentData = with(this) {
  val sectionContentData = DbSectionContentData()
  sectionContentData.key = key
  sectionContentData.content = content?.toDbContentItem()
  sectionContentData.version = version

  try {
    sectionContentData.expireAt = expireAt?.toLong()
  } catch (e: NumberFormatException) {
    Timber.e(e, "toDbSectionContentData()")
  }

  val elementMap = HashMap<String, DbElementCache>()

  var index = 0

  elementsCache?.forEach {
    val auxElement = it.value.toDbElementCache(key, index)
    elementMap[it.key] = auxElement
    index++
  }
  sectionContentData.elementsCache = elementMap

  return sectionContentData
}

fun ApiSectionContentData.toContentData(): ContentData = with(this) {
  val sectionContentData = ContentData()
  sectionContentData.content = content?.toContentItem()
  sectionContentData.version = version
  sectionContentData.expiredAt = expireAt.toString()
  sectionContentData.isFromCloud = true

  val elementMap = HashMap<String, ElementCache>()

  elementsCache?.forEach {
    val auxElement = it.value.toElementCache()
    elementMap[it.key] = auxElement
  }
  sectionContentData.elementsCache = elementMap

  return sectionContentData
}

fun DbSectionContentData.toContentData(): ContentData = with(this) {
  val sectionContentData = ContentData()
  sectionContentData.content = content?.toContentItem()
  sectionContentData.version = version
  sectionContentData.expiredAt = expireAt.toString()
  sectionContentData.isFromCloud = false

  val elementMap = HashMap<String, ElementCache>()

  elementsCache?.forEach {
    val auxElement = it.value.toElementCache()
    elementMap[it.key] = auxElement
  }
  sectionContentData.elementsCache = elementMap

  return sectionContentData
}

fun ApiContentItem.toDbContentItem(): DbContentItem = with(this) {
  val contentItem = DbContentItem()
  contentItem.slug = slug
  contentItem.type = type
  contentItem.tags = tags
  contentItem.layout = layout?.toDbContentItemLayout()

  val elementList = ArrayList<DbElement>()
  elements?.forEach {
    elementList.add(it.toDbElement(-1))
  }
  contentItem.elements = elementList

  return contentItem
}

fun ApiContentItem.toContentItem(): ContentItem = with(this) {
  val contentItem = ContentItem()
  contentItem.slug = slug
  contentItem.type = type
  contentItem.tags = tags
  contentItem.layout = layout?.toContentItemLayout()

  val elementList = ArrayList<Element>()
  elements?.forEach {
    elementList.add(it.toElement())
  }
  contentItem.elements = elementList

  return contentItem
}

fun DbContentItem.toContentItem(): ContentItem = with(this) {
  val contentItem = ContentItem()
  contentItem.slug = slug
  contentItem.type = type
  contentItem.tags = tags
  contentItem.layout = layout?.toContentItemLayout()

  val elementList = ArrayList<Element>()
  elements?.forEach {
    elementList.add(it.toElement())
  }
  contentItem.elements = elementList

  return contentItem
}

fun ApiContentItemLayout.toDbContentItemLayout(): DbContentItemLayout = with(this) {
  val contentItemLayout = DbContentItemLayout()
  contentItemLayout.name = name
  contentItemLayout.type = type

  val contentItemPatternList = ArrayList<DbContentItemPattern>()
  pattern?.forEach {
    contentItemPatternList.add(it.toDbContentItemPattern())
  }
  contentItemLayout.pattern = contentItemPatternList

  return contentItemLayout
}

fun ApiContentItemLayout.toContentItemLayout(): ContentItemLayout = with(this) {
  val contentItemLayout = ContentItemLayout()
  contentItemLayout.name = name
  contentItemLayout.type = ContentItemTypeLayout.convertFromString(type)

  val contentItemPatternList = ArrayList<ContentItemPattern>()
  pattern?.forEach {
    contentItemPatternList.add(it.toContentItemPattern())
  }
  contentItemLayout.pattern = contentItemPatternList

  return contentItemLayout
}

fun DbContentItemLayout.toContentItemLayout(): ContentItemLayout = with(this) {
  val contentItemLayout = ContentItemLayout()
  contentItemLayout.name = name
  contentItemLayout.type = ContentItemTypeLayout.convertFromString(type)

  val contentItemPatternList = ArrayList<ContentItemPattern>()
  pattern?.forEach {
    contentItemPatternList.add(it.toContentItemPattern())
  }
  contentItemLayout.pattern = contentItemPatternList

  return contentItemLayout
}

fun ApiContentItemPattern.toDbContentItemPattern(): DbContentItemPattern = with(this) {
  val contentItemPattern = DbContentItemPattern()
  contentItemPattern.row = row
  contentItemPattern.column = column
  return contentItemPattern
}

fun ApiContentItemPattern.toContentItemPattern(): ContentItemPattern = with(this) {
  val contentItemPattern = ContentItemPattern()
  contentItemPattern.row = row
  contentItemPattern.column = column
  return contentItemPattern
}

fun DbContentItemPattern.toContentItemPattern(): ContentItemPattern = with(this) {
  val contentItemPattern = ContentItemPattern()
  contentItemPattern.row = row
  contentItemPattern.column = column
  return contentItemPattern
}
//endregion

//region VIDEO
/*
fun ApiVideoData.toVimeoInfo(): VimeoInfo = with(this) {
  val video = VimeoInfo()
  video.id = element.id
  video.thumbPath = element.thumbPath
  video.videoPath = element.videoPath
  return video
}
*/
/*
fun ApiVideoData.toDbVideoData(): DbVideoData = with(this) {
  val video = DbVideoData()
  video.id = element.id
  video.element = element.toDbVimeoInfo()
  return video
}
*/

fun DbVideoData.toVimeoInfo(): VimeoInfo = with(this) {
  val video = VimeoInfo()
  video.id = id
  video.thumbPath = element?.thumbPath
  video.videoPath = element?.videoPath
  return video
}

private fun VimeoInfo.toDbVimeoInfo(): DbVimeoInfo = with(this) {
  val vimeo = DbVimeoInfo()
  vimeo.videoPath = videoPath
  vimeo.thumbPath = thumbPath
  return vimeo
}

fun VimeoInfo.toDbVideoData(): DbVideoData = with(this) {
  val video = DbVideoData()
  video.id = id
  video.element = toDbVimeoInfo()
  return video
}
//endregion
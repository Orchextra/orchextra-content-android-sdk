package com.gigigo.orchextra.core.data.mappers

import com.gigigo.orchextra.core.data.api.dto.article.ApiArticleElement
import com.gigigo.orchextra.core.data.api.dto.article.ApiArticleElementRender
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCache
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCachePreview
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCacheRender
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCacheShare
import com.gigigo.orchextra.core.data.api.dto.elementcache.CidKeyData
import com.gigigo.orchextra.core.data.api.dto.elementcache.FederatedAuthorizationData
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementData
import com.gigigo.orchextra.core.data.api.dto.versioning.ApiVersionData
import com.gigigo.orchextra.core.data.api.dto.video.ApiVideoData
import com.gigigo.orchextra.core.data.database.entities.DbArticleElement
import com.gigigo.orchextra.core.data.database.entities.DbArticleElementRender
import com.gigigo.orchextra.core.data.database.entities.DbCidKeyData
import com.gigigo.orchextra.core.data.database.entities.DbElementCache
import com.gigigo.orchextra.core.data.database.entities.DbElementCachePreview
import com.gigigo.orchextra.core.data.database.entities.DbElementCacheRender
import com.gigigo.orchextra.core.data.database.entities.DbElementCacheShare
import com.gigigo.orchextra.core.data.database.entities.DbFederatedAuthorizationData
import com.gigigo.orchextra.core.data.database.entities.DbVersionData
import com.gigigo.orchextra.core.data.database.entities.DbVersionData.Companion.VERSION_KEY
import com.gigigo.orchextra.core.data.database.entities.DbVideoData
import com.gigigo.orchextra.core.data.database.entities.DbVimeoInfo
import com.gigigo.orchextra.core.data.getTodayPlusDays
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
import com.gigigo.orchextra.core.domain.entities.elementcache.CidKey
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheBehaviour
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCachePreview
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheRender
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheShare
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheType
import com.gigigo.orchextra.core.domain.entities.elementcache.FederatedAuthorization
import com.gigigo.orchextra.core.domain.entities.elementcache.VideoFormat
import com.gigigo.orchextra.core.domain.entities.elements.ElementData
import com.gigigo.orchextra.core.domain.entities.version.VersionData
import gigigo.com.vimeolibs.VimeoInfo
import java.util.Calendar

//region VERSION
fun ApiVersionData.toDbVersionData(): DbVersionData = with(this) {
  val versionData = DbVersionData()
  versionData.id = VERSION_KEY
  versionData.version = version
  return versionData
}

fun VersionData.toDbVersionData(): DbVersionData = with(this) {
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

fun DbVersionData.toVersionData(): VersionData = with(this) {
  val versionData = VersionData()
  versionData.version = version
  return versionData
}

//endregion

//region MENU
/*
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
*/
//endregion

//region ELEMENT
/*
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
*/

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

/*
private fun ApiElementCache.toDbElementCache(): DbElementCache = with(this) {
  val elementCache = DbElementCache()
  elementCache.slug = slug
  elementCache.name = name
  elementCache.customProperties = customProperties?.toDbCustomProperties()
  elementCache.preview = preview?.toDbElementCachePreview()
  elementCache.render = render?.toDbElementCacheRender()
  elementCache.share = share?.toDbElementCacheShare()
  elementCache.tags = tags
  elementCache.type = type
  elementCache.updatedAt = updatedAt
  return elementCache
}
*/

fun ElementCache.toDbElementCache(): DbElementCache = with(this) {
  val elementCache = DbElementCache()
  elementCache.slug = slug
  elementCache.name = name
  elementCache.customProperties = customProperties?.toDbCustomProperties()
  elementCache.preview = preview?.toDbElementCachePreview()
  elementCache.render = render?.toDbElementCacheRender()
  elementCache.share = share?.toDbElementCacheShare()
  elementCache.tags = tags
  elementCache.type = type?.type
  elementCache.updatedAt = updateAt
  return elementCache
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
/*
private fun ApiElementCachePreview.toDbElementCachePreview(): DbElementCachePreview = with(this) {
  val elementCache = DbElementCachePreview()
  elementCache.text = text
  elementCache.imageUrl = imageUrl
  elementCache.imageThumb = imageThumb
  elementCache.cacheBehaviour = cacheBehaviour
  return elementCache
}
*/
private fun ElementCachePreview.toDbElementCachePreview(): DbElementCachePreview = with(this) {
  val elementCache = DbElementCachePreview()
  elementCache.text = text
  elementCache.imageUrl = imageUrl
  elementCache.imageThumb = imageThumb
  elementCache.behaviour = behaviour?.cacheBehaviour
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
/*
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
*/
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
/*
private fun ApiElementCacheShare.toDbElementCacheShare(): DbElementCacheShare = with(this) {
  val elementCache = DbElementCacheShare()
  elementCache.text = text
  elementCache.url = url
  return elementCache
}
*/
private fun ElementCacheShare.toDbElementCacheShare(): DbElementCacheShare = with(this) {
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

private fun Map<String, Any>.toDbCustomProperties(): Map<String, String> = with(this) {
  val map = HashMap<String, String>()
  for (next in iterator()) {
    val (key,value) = next
    map[key] = value.toString()
  }
  return map
}

fun <T>ApiArticleElement.toArticleElement(): ArticleElement<T> = with(this) {
  val articleElement = ArticleElement<T>()
  articleElement.customProperties = customProperties

  val articleType = ArticleTypeSection.convertStringToEnum(type)
  val articleElementRender = when(articleType) {
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
      val elementRender = when(videoFormat) {
        VideoFormat.YOUTUBE -> {
          val videoElement = ArticleYoutubeVideoElementRender()
          videoElement.source = render?.source
        }
        VideoFormat.VIMEO -> {
          val videoElement = ArticleVimeoVideoElementRender()
          videoElement.source = render?.source
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
    else -> {  ArticleButtonElementRender()}
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

private fun <T>ArticleElement<T>.toDbArticleElement(): DbArticleElement = with(this) {
  val articleElement = DbArticleElement()
  articleElement.customProperties = customProperties?.toDbCustomProperties()

  val articleElementRender = DbArticleElementRender()
  val articleRender = render
  when(articleRender) {
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
    else -> {}
  }
  articleElement.render = articleElementRender

  return articleElement
}

private fun <T>DbArticleElement.toArticleElement(): ArticleElement<T> = with(this) {
  lateinit var articleElement: ArticleElement<T>

  val articleType = ArticleTypeSection.convertStringToEnum(type)
  val articleElementRender = when(articleType) {
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
      val videoRender = when(videoFormat) {
        VideoFormat.YOUTUBE -> {
          articleElement = ArticleYoutubeVideoElement() as ArticleElement<T>
          val videoElement = ArticleYoutubeVideoElementRender()
          videoElement.source = render?.source
        }
        VideoFormat.VIMEO -> {
          articleElement = ArticleVimeoVideoElement() as ArticleElement<T>
          val videoElement = ArticleVimeoVideoElementRender()
          videoElement.source = render?.source
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

private fun ApiArticleElementRender.toDbArticleElementRender(): DbArticleElementRender = with(this) {
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

fun FederatedAuthorizationData.toDbFederatedAuthorizationData(): DbFederatedAuthorizationData = with(this) {
  val federatedAuthorization = DbFederatedAuthorizationData()
  federatedAuthorization.type = type
  federatedAuthorization.active = active
  federatedAuthorization.keys = keys?.toDbCidKeyData()
  return federatedAuthorization
}

private fun FederatedAuthorization.toDbFederatedAuthorizationData(): DbFederatedAuthorizationData = with(this) {
  val federatedAuthorization = DbFederatedAuthorizationData()
  federatedAuthorization.type = type
  federatedAuthorization.active = isActive
  federatedAuthorization.keys = keys.toDbCidKeyData()
  return federatedAuthorization
}

private fun DbFederatedAuthorizationData.toFederatedAuthorization(): FederatedAuthorization = with(this) {
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

private fun CidKey.toDbCidKeyData(): DbCidKeyData? {
  val cidKeyData = DbCidKeyData()
  cidKeyData.siteName = siteName
  return cidKeyData
}

private fun DbCidKeyData.toCidKey(): CidKey {
  val cidKeyData = CidKey()
  cidKeyData.siteName = siteName
  return cidKeyData
}
//endregion

//region SECTION
//TODO: mappers for ApiSectionContentData, ApiContentItem, ApiContentItemLayout, ApiContentItemPattern
//endregion

//region ELEMENT
fun ApiElementData.toElementData(): ElementData {
  val elementData = ElementData()
  elementData.element = element.toElementCache()
  return elementData
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

private fun VimeoInfo.toDbVimeoInfo(): DbVimeoInfo = with(this){
  val vimeo = DbVimeoInfo()
  vimeo.videoPath = videoPath
  vimeo.thumbPath = thumbPath
  return vimeo
}

fun VimeoInfo.toDbVideoData(): DbVideoData = with(this){
  val video = DbVideoData()
  video.id = id
  video.element = toDbVimeoInfo()
  return video
}
//endregion
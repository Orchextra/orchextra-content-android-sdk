package com.gigigo.orchextra.core.data.api.mappers.article

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper
import com.gigigo.orchextra.core.data.api.dto.article.ApiArticleElement
import com.gigigo.orchextra.core.data.api.dto.article.ApiArticleElementRender
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
import com.gigigo.orchextra.core.domain.entities.elementcache.VideoFormat

class ApiArticleElementMapper : ExternalClassToModelMapper<ApiArticleElement, ArticleElement<ArticleElementRender>> {

  override fun externalClassToModel(
      data: ApiArticleElement): ArticleElement<ArticleElementRender>? {

    val articleTypeSection = ArticleTypeSection.convertStringToEnum(data.type)

    val model = createArticleElementByType(articleTypeSection, data.render)
    data.render?.let { render ->
      model.render = convertArticleRenderByType(articleTypeSection, render)

    }
    data.customProperties?.let {
      model.customProperties = data.customProperties
    }

    return model
  }

  private fun createArticleElementByType(articleTypeSection: ArticleTypeSection,
      render: ApiArticleElementRender?): ArticleElement<ArticleElementRender> {
    return when (articleTypeSection) {
      ArticleTypeSection.HEADER -> ArticleHeaderElement() as ArticleElement<ArticleElementRender>
      ArticleTypeSection.IMAGE -> ArticleImageElement() as ArticleElement<ArticleElementRender>
      ArticleTypeSection.VIDEO -> createArticleVideoByType(render)
      ArticleTypeSection.RICH_TEXT -> ArticleRichTextElement() as ArticleElement<ArticleElementRender>
      ArticleTypeSection.IMAGE_AND_TEXT -> ArticleImageAndTextElement() as ArticleElement<ArticleElementRender>
      ArticleTypeSection.TEXT_AND_IMAGE -> ArticleTextAndImageElement() as ArticleElement<ArticleElementRender>
      ArticleTypeSection.BUTTON -> ArticleButtonElement() as ArticleElement<ArticleElementRender>
      else -> ArticleElement()
    }
  }

  private fun createArticleVideoByType(
      render: ApiArticleElementRender?): ArticleElement<ArticleElementRender> {
    val videoFormat = VideoFormat.convertStringToType(render?.format)

    return when (videoFormat) {
      VideoFormat.YOUTUBE -> return ArticleYoutubeVideoElement() as ArticleElement<ArticleElementRender>
      VideoFormat.VIMEO -> return ArticleVimeoVideoElement() as ArticleElement<ArticleElementRender>
      else -> ArticleElement()
    }
  }

  private fun convertArticleRenderByType(articleTypeSection: ArticleTypeSection,
      render: ApiArticleElementRender): ArticleElementRender? {
    return when (articleTypeSection) {
      ArticleTypeSection.HEADER -> return getArticleHeaderElementRender(render)
      ArticleTypeSection.IMAGE -> return getArticleImageElementRender(render)
      ArticleTypeSection.VIDEO -> return getArticleVideoElementRender(render)
      ArticleTypeSection.RICH_TEXT -> return getArticleRichTextElementRender(render)
      ArticleTypeSection.IMAGE_AND_TEXT -> return getArticleImageAndTextElementRender(render)
      ArticleTypeSection.TEXT_AND_IMAGE -> return getArticleTextAndImageElementRender(render)
      ArticleTypeSection.BUTTON -> return getArticleButtonElementRender(render)
      else -> null
    }
  }

  private fun getArticleHeaderElementRender(
      data: ApiArticleElementRender): ArticleHeaderElementRender {
    val elementRender = ArticleHeaderElementRender()
    with(elementRender) {
      html = data.text
      imageUrl = data.imageUrl
      imageThumb = data.imageThumb
    }
    return elementRender
  }

  private fun getArticleImageElementRender(
      data: ApiArticleElementRender): ArticleImageElementRender {
    val elementRender = ArticleImageElementRender()
    with(elementRender) {
      imageUrl = data.imageUrl
      imageThumb = data.imageThumb
      elementUrl = data.elementUrl
    }
    return elementRender
  }

  private fun getArticleVideoElementRender(render: ApiArticleElementRender): ArticleElementRender? {
    val videoFormat = VideoFormat.convertStringToType(render.format)

    return when (videoFormat) {
      VideoFormat.YOUTUBE -> getArticleYoutubeVideoElement(render)
      VideoFormat.VIMEO -> getArticleVimeoVideoElement(render)
      else -> null
    }
  }

  private fun getArticleVimeoVideoElement(
      render: ApiArticleElementRender): ArticleVimeoVideoElementRender {
    val elementRender = ArticleVimeoVideoElementRender()
    elementRender.source = render.source
    return elementRender
  }

  private fun getArticleYoutubeVideoElement(
      render: ApiArticleElementRender): ArticleYoutubeVideoElementRender {
    val elementRender = ArticleYoutubeVideoElementRender()
    elementRender.source = render.source
    return elementRender
  }

  private fun getArticleRichTextElementRender(
      render: ApiArticleElementRender): ArticleRichTextElementRender {
    val element = ArticleRichTextElementRender()
    element.html = render.text
    return element
  }

  private fun getArticleImageAndTextElementRender(
      render: ApiArticleElementRender): ArticleImageAndTextElementRender {
    val elementRender = ArticleImageAndTextElementRender()
    with(elementRender) {
      text = render.text
      imageUrl = render.imageUrl
      ratios = render.ratios
    }
    return elementRender
  }

  private fun getArticleTextAndImageElementRender(
      render: ApiArticleElementRender): ArticleTextAndImageElementRender {
    val elementRender = ArticleTextAndImageElementRender()
    with(elementRender) {
      text = render.text
      imageUrl = render.imageUrl
      ratios = render.ratios
    }
    return elementRender
  }

  private fun getArticleButtonElementRender(
      render: ApiArticleElementRender): ArticleButtonElementRender {
    val elementRender = ArticleButtonElementRender()
    with(elementRender) {
      type = ArticleButtonType.convertFromString(render.type)
      size = ArticleButtonSize.convertFromString(render.size)
      elementUrl = render.elementUrl
      text = render.text
      textColor = render.textColor
      bgColor = render.bgColor
      imageUrl = render.imageUrl
    }

    return elementRender
  }
}

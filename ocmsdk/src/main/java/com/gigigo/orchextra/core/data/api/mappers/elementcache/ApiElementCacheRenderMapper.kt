package com.gigigo.orchextra.core.data.api.mappers.elementcache

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCacheRender
import com.gigigo.orchextra.core.data.api.mappers.article.ApiArticleElementMapper
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElementRender
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheRender
import com.gigigo.orchextra.core.domain.entities.elementcache.VideoFormat
import java.util.*

class ApiElementCacheRenderMapper(private val apiArticleElementMapper: ApiArticleElementMapper,
    private val federatedAuthorizationDataMapper: FederatedAuthorizationDataMapper) : ExternalClassToModelMapper<ApiElementCacheRender, ElementCacheRender> {

  override fun externalClassToModel(data: ApiElementCacheRender): ElementCacheRender {
    val model = ElementCacheRender()

    with(model) {
      contentUrl = data.contentUrl
      url = data.url
      title = data.title
      source = data.source
      schemeUri = data.schemeUri
      federatedAuth = federatedAuthorizationDataMapper.externalClassToModel(data.federatedAuth)
    }

    data.format?.let {
      model.format = VideoFormat.convertStringToType(data.format)
    }

    val articleElementList = ArrayList<ArticleElement<ArticleElementRender>>()
    if (data.elements != null) {
      for (apiArticleElement in data.elements) {
        val articleElement = apiArticleElementMapper.externalClassToModel(apiArticleElement)

        articleElement?.let {
          articleElementList.add(articleElement)
        }
      }
    }

    model.elements = articleElementList

    return model
  }
}

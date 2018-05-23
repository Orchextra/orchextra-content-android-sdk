package com.gigigo.orchextra.core.domain.entities.elementcache


import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElementRender
import java.io.Serializable

class ElementCacheRender : Serializable {
  var contentUrl: String? = null
  var url: String? = null

  var title: String? = null
  var elements: List<ArticleElement<ArticleElementRender>>? = null

  var format: VideoFormat? = null
  var source: String? = null

  var schemeUri: String? = null

  var federatedAuth: FederatedAuthorization? = null
}
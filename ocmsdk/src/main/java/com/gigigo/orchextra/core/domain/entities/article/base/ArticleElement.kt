package com.gigigo.orchextra.core.domain.entities.article.base

import java.io.Serializable

open class ArticleElement<RenderType> : Serializable {
  var customProperties: Map<String, Any>? = null
  var render: RenderType? = null
}

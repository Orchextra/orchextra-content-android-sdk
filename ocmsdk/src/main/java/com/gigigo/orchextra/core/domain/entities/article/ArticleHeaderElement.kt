package com.gigigo.orchextra.core.domain.entities.article

import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElementRender

class ArticleHeaderElement : ArticleElement<ArticleHeaderElementRender>()

class ArticleHeaderElementRender : ArticleElementRender() {
  var html: String? = null
  var imageUrl: String? = null
  var imageThumb: String? = null
}

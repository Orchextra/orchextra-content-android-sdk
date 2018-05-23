package com.gigigo.orchextra.core.domain.entities.article

import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElementRender

class ArticleImageElement : ArticleElement<ArticleImageElementRender>()

class ArticleImageElementRender : ArticleElementRender() {

  var elementUrl: String? = null
  var imageUrl: String? = null
  var imageThumb: String? = null
}

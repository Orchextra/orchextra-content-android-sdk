package com.gigigo.orchextra.core.domain.entities.article

import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElementRender

class ArticleTextAndImageElement : ArticleElement<ArticleTextAndImageElementRender>()

class ArticleTextAndImageElementRender : ArticleElementRender() {

  var text: String? = null
  var imageUrl: String? = null
  var ratios: List<Float>? = null
}

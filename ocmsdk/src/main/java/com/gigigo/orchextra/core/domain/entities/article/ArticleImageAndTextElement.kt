package com.gigigo.orchextra.core.domain.entities.article

import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElementRender

class ArticleImageAndTextElement : ArticleElement<ArticleImageAndTextElementRender>()

class ArticleImageAndTextElementRender : ArticleElementRender() {

  var text: String? = null
  var imageUrl: String? = null
  var ratios: List<Float>? = null
}

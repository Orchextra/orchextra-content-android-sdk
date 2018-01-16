package com.gigigo.orchextra.core.domain.entities.article

import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElementRender

class ArticleVimeoVideoElement : ArticleElement<ArticleVimeoVideoElementRender>()

class ArticleVimeoVideoElementRender : ArticleElementRender() {

  var source: String? = null
}

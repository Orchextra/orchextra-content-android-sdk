package com.gigigo.orchextra.core.domain.entities.article

import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElementRender

class ArticleYoutubeVideoElement : ArticleElement<ArticleYoutubeVideoElementRender>()

class ArticleYoutubeVideoElementRender : ArticleElementRender() {

  var source: String? = null
}

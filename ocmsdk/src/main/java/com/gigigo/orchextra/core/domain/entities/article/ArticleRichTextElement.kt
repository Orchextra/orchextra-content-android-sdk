package com.gigigo.orchextra.core.domain.entities.article

import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElementRender

class ArticleRichTextElement : ArticleElement<ArticleRichTextElementRender>()

class ArticleRichTextElementRender : ArticleElementRender() {
  var html: String? = null
}

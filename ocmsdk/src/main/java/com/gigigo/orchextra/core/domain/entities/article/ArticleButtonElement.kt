package com.gigigo.orchextra.core.domain.entities.article

import com.gigigo.orchextra.core.domain.entities.article.base.ArticleButtonSize
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleButtonType
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElementRender

class ArticleButtonElement : ArticleElement<ArticleButtonElementRender>()

class ArticleButtonElementRender : ArticleElementRender() {
  var type: ArticleButtonType? = null
  var size: ArticleButtonSize? = null
  var elementUrl: String? = null
  var text: String? = null
  var textColor: String? = null
  var bgColor: String? = null
  var imageUrl: String? = null
}
